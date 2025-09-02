package com.EmployeeRating.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Entity.Rating;

public class ExcelGenerator {
	public static byte[] generateExcelInMemory(Employee employee) throws IOException {
		return new byte[0];
	}

	public static byte[] generateExcelForEmployee(Employee employee) throws IOException {
		return new byte[0];
	}

	public static byte[] generateExcelForEmployees(List<Employee> employees) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Employees");

		// Create header style
		CellStyle headerStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerStyle.setFont(headerFont);

		// Header row
		Row header = sheet.createRow(0);
		String[] columns = new String[]{"Employee ID", "Employee Name", "Project Name", "Rating", "Score"};
		for (int i = 0; i < columns.length; i++) {
			Cell cell = header.createCell(i);
			cell.setCellValue(columns[i]);
			cell.setCellStyle(headerStyle);
			sheet.setColumnWidth(i, 20 * 256); // 20 characters width
		}

		int rowIdx = 1;
		for (Employee employee : employees) {
			Row row = sheet.createRow(rowIdx++);

			String employeeId = employee.getEmployeeId();
			String employeeName = employee.getEmployeeName();
			String projectName = employee.getProjectName();

			Integer ratingValue = null;
			Rating rating = employee.getRating();
			if (rating != null) {
				ratingValue = rating.getDailyRating();
			}

			String score = "";
			if (ratingValue != null) {
				if (ratingValue <= 2) {
					score = "0%";
				} else if (ratingValue <= 4) {
					score = "50%";
				} else { // 5 or higher
					score = "100%";
				}
			}

			row.createCell(0).setCellValue(employeeId != null ? employeeId : "");
			row.createCell(1).setCellValue(employeeName != null ? employeeName : "");
			row.createCell(2).setCellValue(projectName != null ? projectName : "");
			if (ratingValue != null) {
				row.createCell(3).setCellValue(ratingValue);
			} else {
				row.createCell(3).setCellValue("");
			}
			row.createCell(4).setCellValue(score);
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			workbook.write(bos);
			return bos.toByteArray();
		} finally {
			bos.close();
			workbook.close();
		}
	}

	public static byte[] generateSimpleExcel() throws IOException {
		return new byte[0];
	}

	public static byte[] generateForProjectManager(List<Employee> employees)
			throws IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
		return new byte[0];
	}

	public static byte[] generateReadOnly(List<Employee> employees) throws IOException {
		return new byte[0];
	}
}
