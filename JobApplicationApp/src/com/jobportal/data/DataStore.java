package com.jobportal.data;

import com.jobportal.model.Application;
import com.jobportal.model.Job;
import com.jobportal.model.User;
import com.jobportal.model.User.UserType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    // In-memory storage using ArrayLists
    private static List<User> users = new ArrayList<>();
    private static List<Job> jobs = new ArrayList<>();
    private static List<Application> applications = new ArrayList<>();

    // File paths for persistent storage
    private static final String DATA_DIR = "jobportal_data";
    private static final String USERS_FILE = DATA_DIR + "/users.dat";
    private static final String JOBS_FILE = DATA_DIR + "/jobs.dat";
    private static final String APPLICATIONS_FILE = DATA_DIR + "/applications.dat";

    // Initialize - Load data from files or create sample data
    static {
        loadAllData();
    }

    // Load all data from files
    private static void loadAllData() {
        createDataDirectory();

        // Try to load from files
        boolean dataLoaded = false;

        try {
            users = loadFromFile(USERS_FILE);
            jobs = loadFromFile(JOBS_FILE);
            applications = loadFromFile(APPLICATIONS_FILE);

            if (!users.isEmpty() || !jobs.isEmpty()) {
                dataLoaded = true;
                System.out.println("✓ Data loaded from files");
            }
        } catch (Exception e) {
            System.out.println("ℹ No existing data found, creating sample data...");
        }

        // If no data loaded, create sample data
        if (!dataLoaded) {
            createSampleData();
            saveAllData();
        }
    }

    // Create sample data for first-time use
    private static void createSampleData() {
        // Add sample employer
        User employer = new User("employer1", "pass123", "employer@company.com", "ABC Company HR", UserType.EMPLOYER);
        users.add(employer);

        // Add sample job seeker
        User jobSeeker = new User("seeker1", "pass123", "seeker@email.com", "John Doe", UserType.JOB_SEEKER);
        users.add(jobSeeker);

        // Add sample jobs
        jobs.add(new Job(employer.getUserId(), "Java Developer", "ABC Ltd",
                "Looking for experienced Java developer with 3+ years experience", "Mumbai", "8-12 LPA"));
        jobs.add(new Job(employer.getUserId(), "Frontend Developer", "ABC Ltd",
                "React and Angular developer needed", "Delhi", "6-10 LPA"));
    }

    // Create data directory if it doesn't exist
    private static void createDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    // Save all data to files
    private static void saveAllData() {
        try {
            saveToFile(users, USERS_FILE);
            saveToFile(jobs, JOBS_FILE);
            saveToFile(applications, APPLICATIONS_FILE);
        } catch (Exception e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    // Generic method to save list to file
    private static void saveToFile(List<?> data, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
        }
    }

    // Generic method to load list from file
    @SuppressWarnings("unchecked")
    private static <T> List<T> loadFromFile(String filename) throws IOException, ClassNotFoundException {
        File file = new File(filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<T>) ois.readObject();
        }
    }

    // User Methods
    public static boolean registerUser(User user) {
        if (findUserByUsername(user.getUsername()) != null) {
            return false; // Username already exists
        }
        users.add(user);
        saveAllData(); // Save after adding
        return true;
    }

    public static User loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public static User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static User findUserById(int userId) {
        for (User user : users) {
            if (user.getUserId() == userId) {
                return user;
            }
        }
        return null;
    }

    // Job Methods
    public static boolean addJob(Job job) {
        jobs.add(job);
        saveAllData(); // Save after adding
        return true;
    }

    public static List<Job> getAllJobs() {
        return new ArrayList<>(jobs);
    }

    public static List<Job> getJobsByEmployerId(int employerId) {
        List<Job> employerJobs = new ArrayList<>();
        for (Job job : jobs) {
            if (job.getEmployerId() == employerId) {
                employerJobs.add(job);
            }
        }
        return employerJobs;
    }

    public static Job findJobById(int jobId) {
        for (Job job : jobs) {
            if (job.getJobId() == jobId) {
                return job;
            }
        }
        return null;
    }

    public static boolean deleteJob(int jobId) {
        boolean removed = jobs.removeIf(job -> job.getJobId() == jobId);
        if (removed) {
            saveAllData(); // Save after deleting
        }
        return removed;
    }

    // Application Methods
    public static boolean submitApplication(Application application) {
        // Check if already applied
        if (hasApplied(application.getJobId(), application.getJobSeekerId())) {
            return false;
        }
        applications.add(application);
        saveAllData(); // Save after adding
        return true;
    }

    public static boolean updateApplicationStatus(int applicationId, String newStatus) {
        for (Application app : applications) {
            if (app.getApplicationId() == applicationId) {
                app.setStatus(newStatus);
                saveAllData();
                return true;
            }
        }
        return false;
    }

    public static Application getApplicationById(int applicationId) {
        for (Application app : applications) {
            if (app.getApplicationId() == applicationId) {
                return app;
            }
        }
        return null;
    }

    public static boolean hasApplied(int jobId, int jobSeekerId) {
        for (Application app : applications) {
            if (app.getJobId() == jobId && app.getJobSeekerId() == jobSeekerId) {
                return true;
            }
        }
        return false;
    }

    public static List<Application> getApplicationsByJobSeeker(int jobSeekerId) {
        List<Application> userApps = new ArrayList<>();
        for (Application app : applications) {
            if (app.getJobSeekerId() == jobSeekerId) {
                userApps.add(app);
            }
        }
        return userApps;
    }

    public static List<Application> getApplicationsByJobId(int jobId) {
        List<Application> jobApps = new ArrayList<>();
        for (Application app : applications) {
            if (app.getJobId() == jobId) {
                jobApps.add(app);
            }
        }
        return jobApps;
    }

    public static int getApplicationCount(int jobId) {
        int count = 0;
        for (Application app : applications) {
            if (app.getJobId() == jobId) {
                count++;
            }
        }
        return count;
    }

    // Utility methods for testing
    public static int getTotalUsers() {
        return users.size();
    }

    public static int getTotalJobs() {
        return jobs.size();
    }

    public static int getTotalApplications() {
        return applications.size();
    }
}