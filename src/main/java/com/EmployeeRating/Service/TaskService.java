package com.EmployeeRating.Service;

import com.EmployeeRating.Dto.EmployeeTasksSummaryDto;
import java.util.List;

public interface TaskService {
    EmployeeTasksSummaryDto getTasksSummaryByEmployeeId(String employeeId);
    List<EmployeeTasksSummaryDto> getTasksSummariesByTeamLeadEmail(String teamLeadEmail);
}


