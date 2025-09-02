package com.EmployeeRating.ServiceImplementation;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Entity.Rating;
import com.EmployeeRating.Model.FileAttachmentModel;
import com.EmployeeRating.Repository.EmployeeRepo;
import com.EmployeeRating.Repository.RatingRepo;
import com.EmployeeRating.Service.EmailSchedulerService;
import com.EmployeeRating.Service.EmailSenderService;

@Service
public class EmailSchedulerServiceImple implements EmailSchedulerService {
	@Autowired
	EmailSenderService emailSenderService;
	@Autowired
	EmployeeRepo employeeRepo;
	@Autowired
	EntityManager entityManager;
	@Autowired
	RatingRepo ratingRepo;

	public String getHtmlTemplate(String filename) throws Exception {
		ClassPathResource resource = new ClassPathResource("static/" + filename);
		return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
	}
	// @Scheduled(cron = "0 0 11 25 * ?") have to change it to 25th of every month
	@Scheduled(cron = "0 0 15 * * ?")
	@Override
	public void sendEmailParticular() {
		try {
			String htmlTemplate = getHtmlTemplate("email-template(rating page).html");
			// Collect all distinct team lead emails
			List<String> teamLeadEmails = employeeRepo.findAll().stream()
					.map(Employee::getTeamLeadEmail)
					.filter(email -> email != null && !email.isEmpty())
					.distinct()
					.collect(Collectors.toList());

			for (String teamLeadEmail : teamLeadEmails) {
				String htmlContent = htmlTemplate;
				// Replace CTA link with TL-specific link
				htmlContent = htmlContent.replace("<a href=\"#\" class=\"btn\">Please Give the Employee Ratings</a>",
						"<a href=\"https://employee-rating-six.vercel.app/employee?teamLeadEmail=" + teamLeadEmail + "\" class=\"btn\">Please Give the Employee Ratings</a>");
				// Generic salutation
				htmlContent = htmlContent.replace("${name}", "TEAM LEAD");

				FileAttachmentModel model = new FileAttachmentModel();
				model.setToEmail(teamLeadEmail);
				model.setSubject("Rate your employees");
				model.setBody(htmlContent);
				emailSenderService.sendEmailWithAttachmentToTl(model);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Scheduled(cron = "0 0 12 26 * ?")
	@Override
	public void sendEmailToPm() {
		FileAttachmentModel model = new FileAttachmentModel();
		List<Employee> employees = employeeRepo.findAll();
		LocalDate today = LocalDate.now();
		// Group eligible employees by PM email and send one email per PM
		Map<String, List<Employee>> employeesByPm = employees.stream()
				.filter(employee -> (employee.getRating() != null && employee.getRating().getTlSubmitDate() != null
						&& employee.getRating().getSendDateToPm() == null))
				.collect(Collectors.groupingBy(Employee::getProjectManagerEmail));

		for (Map.Entry<String, List<Employee>> entry : employeesByPm.entrySet()) {
			String pmEmail = entry.getKey();
			List<Employee> pmEmployees = entry.getValue();
			if (pmEmployees == null || pmEmployees.isEmpty())
				continue;
			try {
				String htmlContent = getHtmlTemplate("email-template(rating page).html");
				// Replace CTA link with PM-specific link
				htmlContent = htmlContent.replace("<a href=\"#\" class=\"btn\">Please Give the Employee Ratings</a>",
						"<a href=\"https://employee-rating-six.vercel.app/project-manager?projectManagerEmail=" + pmEmail + "\" class=\"btn\">Please Give the Employee Ratings</a>");
				// Personalize name with PM name if available
				String pmName = pmEmployees.get(0).getProjectManagerName() != null ? pmEmployees.get(0).getProjectManagerName().toUpperCase() : "PROJECT MANAGER";
				htmlContent = htmlContent.replace("${name}", pmName);

				model.setToEmail(pmEmail);
				model.setSubject("Rate your employees");
				model.setBody(htmlContent);
				emailSenderService.sendEmailWithAttachmentToPm(model);

				// Mark send date for all employees under this PM
				for (Employee employee : pmEmployees) {
					Rating rating = employee.getRating();
					if (rating != null) {
						rating.setSendDateToPm(LocalDate.now());
						ratingRepo.save(rating);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// @Scheduled(cron = "0 0 13 27 * ?")
	@Override
	public void sendEmailToPmo() {
		// PMO email functionality not implemented - commented out as requested
		// FileAttachmentModel model = new FileAttachmentModel();
		// List<Employee> employees = employeeRepo.findAll();
		// LocalDate today = LocalDate.now();
		// // Group eligible employees by PMO email and send one email per PMO
		// Map<String, List<Employee>> employeesByPmo = employees.stream()
		// 		.filter(employee -> (employee.getRating() != null && employee.getRating().getPmSubmitDate() != null
		// 				&& employee.getRating().getSendDateToPmo() == null
		// 				&& !(employee.isNoticePeriod() || employee.isProbationaPeriod())))
		// 		.collect(Collectors.groupingBy(Employee::getPmoEmail));

		// for (Map.Entry<String, List<Employee>> entry : employeesByPmo.entrySet()) {
		// 	String pmoEmail = entry.getKey();
		// 	List<Employee> pmoEmployees = entry.getValue();
		// 	if (pmoEmployees == null || pmoEmployees.isEmpty())
		// 		continue;
		// 	try {
		// 		String htmlContent = getHtmlTemplate("email-template(rating page).html");
		// 		// Replace CTA link with PMO-specific link
		// 		htmlContent = htmlContent.replace("<a href=\"#\" class=\"btn\">Please Give the Employee Ratings</a>",
		// 				"<a href=\"https://employee-rating-six.vercel.app/pmo?pmoEmail=" + pmoEmail + "\" class=\"btn\">Please Give the Employee Ratings</a>");
		// 		// Personalize name with PMO name if available
		// 		String pmoName = pmoEmployees.get(0).getPmoName() != null ? pmoEmployees.get(0).getPmoName().toUpperCase() : "PMO";
		// 		htmlContent = htmlContent.replace("${name}", pmoName);

		// 		model.setToEmail(pmoEmail);
		// 		model.setSubject("Rate your employees");
		// 		model.setBody(htmlContent);
		// 		emailSenderService.sendEmailWithAttachmentToPmo(model);

		// 		// Mark send date for all employees under this PMO
		// 		for (Employee employee : pmoEmployees) {
		// 			Rating rating = employee.getRating();
		// 			if (rating != null) {
		// 				rating.setSendDateToPmo(LocalDate.now());
		// 				ratingRepo.save(rating);
		// 			}
		// 		}
		// 	} catch (Exception e) {
		// 		e.printStackTrace();
		// 	}
		// }
	}

	// @Scheduled(cron = "0 0 14 28 * ?")
	@Override
	public void sendEmailToHr() {
		// HR email functionality not implemented - commented out as requested
		// FileAttachmentModel model = new FileAttachmentModel();
		// List<Employee> employees = employeeRepo.findAll();
		// LocalDate today = LocalDate.now();
		// // Filter employees eligible for HR email
		// List<Employee> hrEligibleEmployees = employees.stream()
		// 		.filter(employee -> (employee.getRating() != null && employee.getRating().getPmoSubmitDate() != null
		// 				&& employee.getRating().getSendToHr() == null
		// 				&& !(employee.isNoticePeriod() || employee.isProbationaPeriod())))
		// 		.collect(Collectors.toList());

		// if (hrEligibleEmployees.isEmpty())
		// 	return;

		// try {
		// 	String htmlContent = getHtmlTemplate("email-template(rating page).html");
		// 	// Replace CTA link with HR-specific link
		// 	htmlContent = htmlContent.replace("<a href=\"#\" class=\"btn\">Please Give the Employee Ratings</a>",
		// 			"<a href=\"https://employee-rating-six.vercel.app/hr\" class=\"btn\">Please Give the Employee Ratings</a>");
		// 	htmlContent = htmlContent.replace("${name}", "HR");

		// 	model.setToEmail("hr@rumango.com"); // Replace with actual HR email
		// 	model.setSubject("Employee ratings ready for review");
		// 	model.setBody(htmlContent);
		// 	emailSenderService.sendEmailWithAttachmentToHr(model);

		// 	// Mark send date for all eligible employees
		// 	for (Employee employee : hrEligibleEmployees) {
		// 		Rating rating = employee.getRating();
		// 		if (rating != null) {
		// 			rating.setSendToHr(LocalDate.now());
		// 			ratingRepo.save(rating);
		// 		}
		// 	}
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }
	}
}
