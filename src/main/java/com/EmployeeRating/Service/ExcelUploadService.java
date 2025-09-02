package com.EmployeeRating.Service;

import org.springframework.web.multipart.MultipartFile;

public interface ExcelUploadService {
	
	public void uploadExcel(MultipartFile file) throws Exception;

}
