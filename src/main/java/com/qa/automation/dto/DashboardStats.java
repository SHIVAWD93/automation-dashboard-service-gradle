package com.qa.automation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStats {
    private long totalTesters;
    private long totalProjects;
    private long totalTestCases;
    private long automatedCount;
    private long manualCount;
    private long inProgressCount;
    private long completedCount;
}
