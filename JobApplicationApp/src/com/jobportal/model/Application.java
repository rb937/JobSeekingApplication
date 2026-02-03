package com.jobportal.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Application implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int idCounter = 1;
    
    private int applicationId;
    private int jobId;
    private int jobSeekerId;
    private String appliedDate;
    private String status;
    private String resumePath;  // Path to uploaded resume

    public Application(int jobId, int jobSeekerId) {
        this.applicationId = idCounter++;
        this.jobId = jobId;
        this.jobSeekerId = jobSeekerId;
        this.appliedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        this.status = "Pending";
        this.resumePath = null;
    }

    public Application(int jobId, int jobSeekerId, String resumePath) {
        this.applicationId = idCounter++;
        this.jobId = jobId;
        this.jobSeekerId = jobSeekerId;
        this.appliedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        this.status = "Pending";
        this.resumePath = resumePath;
    }

    // Getters
    public int getApplicationId() { return applicationId; }
    public int getJobId() { return jobId; }
    public int getJobSeekerId() { return jobSeekerId; }
    public String getAppliedDate() { return appliedDate; }
    public String getStatus() { return status; }
    public String getResumePath() { return resumePath; }

    // Setters
    public void setStatus(String status) { this.status = status; }
    public void setResumePath(String resumePath) { this.resumePath = resumePath; }
}