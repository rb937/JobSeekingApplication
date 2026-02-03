package com.jobportal.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Job implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int idCounter = 1;

    private int jobId;
    private int employerId;
    private String jobTitle;
    private String companyName;
    private String description;
    private String location;
    private String salary;
    private String postedDate;

    public Job(int employerId, String jobTitle, String companyName, String description, String location,
            String salary) {
        this.jobId = idCounter++;
        this.employerId = employerId;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.description = description;
        this.location = location;
        this.salary = salary;
        this.postedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    // Getters
    public int getJobId() {
        return jobId;
    }

    public int getEmployerId() {
        return employerId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getSalary() {
        return salary;
    }

    public String getPostedDate() {
        return postedDate;
    }

    // Setters
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}