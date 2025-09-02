package com.EmployeeRating.util;

import java.io.ByteArrayOutputStream;

import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Entity.Rating;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PdfGenerator {
	public static byte[] generatePdf(Employee employee) {
		Document document = new Document();
		Rating rating = employee.getRating(); 
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		PdfWriter write = PdfWriter.getInstance(document,out );
		try {
			//connect the blank document to output stream
			PdfWriter.getInstance(document, out);
			
			//Oeping the document to do all these file editing
			document.open();
			
			//Adding the top as employee details
			Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD,16);
			Paragraph title = new Paragraph("Employee Details",font);
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);
			
			//Adding space
			document.add(new Paragraph(" "));
			
			//Creating table with 6 column
			PdfPTable table = new PdfPTable(7); // 6 columns
			table.setWidthPercentage(100);
			
			table.addCell("Daily Rating");
			table.addCell("Employee ID");
			table.addCell("Rating Date");
			table.addCell("Rated By");
			table.addCell("N/A");
			table.addCell("N/A");
			table.addCell("N/A");

			// Now add employee data in a single row - using new daily rating fields
			
			    table.addCell(String.valueOf(rating.getDailyRating() != null ? rating.getDailyRating() : "N/A"));
			    table.addCell(String.valueOf(rating.getEmployeeId() != null ? rating.getEmployeeId() : "N/A"));
			    table.addCell(String.valueOf(rating.getRatingDate() != null ? rating.getRatingDate() : "N/A"));
			    table.addCell(String.valueOf(rating.getRatedBy() != null ? rating.getRatedBy() : "N/A"));
			    table.addCell("N/A");
			    table.addCell("N/A");
			    table.addCell("N/A");

			
	         document.add(table);
	         document.close();
	     } catch (Exception e) {
	         e.printStackTrace();
	     }
		return out.toByteArray();
	}
}
