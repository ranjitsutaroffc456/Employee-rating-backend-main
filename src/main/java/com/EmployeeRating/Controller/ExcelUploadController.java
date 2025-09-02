package com.EmployeeRating.Controller;

import com.EmployeeRating.Service.ExcelUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/excel")
public class ExcelUploadController {

    @Autowired
    private ExcelUploadService excelUploadService;

    // POST endpoint to upload Excel file
    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("❌ Please select an Excel file to upload.");
            }

            excelUploadService.uploadExcel(file);
            return ResponseEntity.ok("✅ Excel uploaded and users saved to DB successfully.");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Failed to upload Excel: " + e.getMessage());
        }
    }
}
