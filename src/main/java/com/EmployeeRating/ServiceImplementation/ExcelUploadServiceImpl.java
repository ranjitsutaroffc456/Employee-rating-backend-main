package com.EmployeeRating.ServiceImplementation;

import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Repository.EmployeeRepo;
import com.EmployeeRating.Service.ExcelUploadService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class ExcelUploadServiceImpl implements ExcelUploadService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Override
    public void uploadExcel(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);

            // Skip header (row 0)
            System.out.println("Total rows in sheet: " + sheet.getLastRowNum());
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    System.out.println("Row " + (i + 1) + " is null, skipping");
                    continue;
                }

                // Read from correct columns: B=1, C=2, D=3 (0-indexed)
                Cell cellB = row.getCell(1); // Emp ID (Column B)
                Cell cellC = row.getCell(2); // Name (Column C)  
                Cell cellD = row.getCell(3); // Role (Column D)
                
                String empId = getCellValue(cellB);
                String name = getCellValue(cellC);
                String role = getCellValue(cellD);
                
                // Debug cell info for first few rows
                if (i <= 5) {
                    System.out.println("Row " + (i + 1) + " - Cell B type: " + (cellB != null ? cellB.getCellType() : "null") + 
                                     ", Cell C type: " + (cellC != null ? cellC.getCellType() : "null") + 
                                     ", Cell D type: " + (cellD != null ? cellD.getCellType() : "null"));
                }

                // Debug logging for first few rows
                if (i <= 5) {
                    System.out.println("Row " + (i + 1) + " - EmpID: [" + empId + "], Name: [" + name + "], Role: [" + role + "]");
                }

                // Validate required fields BEFORE creating employee object
                if (empId == null || empId.trim().isEmpty() || 
                    name == null || name.trim().isEmpty() || 
                    role == null || role.trim().isEmpty()) {
                    System.out.println("Row " + (i + 1) + ": Skipping row with empty values - EmpID: [" + empId + "], Name: [" + name + "], Role: [" + role + "]");
                    continue;
                }

                // Check if employee ID already exists
                if (employeeRepo.findByEmployeeId(empId.trim()).isPresent()) {
                    System.out.println("Row " + (i + 1) + ": Employee ID " + empId + " already exists - skipping");
                    continue;
                }

                try {
                    // Create new Employee only after validation passes
                    Employee employee = new Employee();
                    employee.setEmployeeId(empId.trim());
                    employee.setEmployeeName(name.trim());
                    employee.setPassword("Rumango@123");  // default password
                    employee.setEmployeeRole(role.trim());
                    
                    // Set valid default values for required fields (NOT NULL constraints)
                    employee.setEmployeeEmail("temp@company.com"); // Valid email format
                    employee.setProjectManagerName("TBD");         // Valid name placeholder
                    employee.setProjectManagerEmail("pm@company.com"); // Valid email format
                    
                    // Set nullable Boolean fields to null (not false)
                    employee.setPmSubmitted(null);
                    employee.setIsTLSubmitted(null);
                    employee.setIsHrSend(null);
                    employee.setIsPmoSubmitted(null);
                    employee.setNoticePeriod(null);
                    employee.setProbationaPeriod(null);
                    
                    employeeRepo.save(employee);
                    System.out.println("Row " + (i + 1) + ": Successfully saved employee " + empId);
                    
                } catch (Exception e) {
                    System.out.println("Row " + (i + 1) + ": Failed to save employee " + empId + " - " + e.getMessage());
                    throw new RuntimeException("Failed to save employee " + empId + " at row " + (i + 1) + ": " + e.getMessage());
                }
            }

            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage(), e);
        }
    }

    // ✅ Safe cell reader
    private String getCellValue(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return null;
            default:
                return null;
        }
    }
}
