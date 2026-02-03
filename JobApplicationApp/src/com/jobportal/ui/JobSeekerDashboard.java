package com.jobportal.ui;

import com.jobportal.data.DataStore;
import com.jobportal.model.Application;
import com.jobportal.model.Job;
import com.jobportal.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class JobSeekerDashboard extends JFrame {
    private User currentUser;
    private JTable jobsTable;
    private DefaultTableModel tableModel;
    private JTabbedPane tabbedPane;

    public JobSeekerDashboard(User user) {
        this.currentUser = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("Job Seeker Dashboard - " + currentUser.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(46, 204, 113));
        headerPanel.setPreferredSize(new Dimension(1000, 80));
        headerPanel.setLayout(null);

        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setBounds(20, 15, 400, 30);
        headerPanel.add(welcomeLabel);

        JLabel roleLabel = new JLabel("Job Seeker Dashboard");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        roleLabel.setForeground(Color.BLACK);
        roleLabel.setBounds(20, 45, 200, 20);
        headerPanel.add(roleLabel);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(880, 25, 100, 35);
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> logout());
        headerPanel.add(logoutButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.addTab("Browse Jobs", createBrowsePanel());
        tabbedPane.addTab("My Applications", createApplicationsPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createBrowsePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Available Jobs");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(title, BorderLayout.NORTH);

        String[] columns = { "Job ID", "Title", "Company", "Location", "Salary", "Status" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jobsTable = new JTable(tableModel);
        jobsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        jobsTable.setRowHeight(30);
        jobsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        jobsTable.getTableHeader().setBackground(new Color(46, 204, 113));
        jobsTable.getTableHeader().setForeground(Color.BLACK);

        loadAllJobs();
        panel.add(new JScrollPane(jobsTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.BLACK);

        JButton viewButton = createButton("View Details", new Color(52, 152, 219));
        viewButton.addActionListener(e -> viewJobDetails());
        buttonPanel.add(viewButton);

        JButton applyButton = createButton("Apply for Job", new Color(46, 204, 113));
        applyButton.addActionListener(e -> applyForJob());
        buttonPanel.add(applyButton);

        JButton refreshButton = createButton("Refresh", new Color(149, 165, 166));
        refreshButton.addActionListener(e -> loadAllJobs());
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createApplicationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("My Job Applications");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(title, BorderLayout.NORTH);

        String[] columns = { "App ID", "Job Title", "Company", "Applied On", "Status", "Resume" };
        DefaultTableModel appModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable appTable = new JTable(appModel);
        appTable.setFont(new Font("Arial", Font.PLAIN, 13));
        appTable.setRowHeight(30);
        appTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        appTable.getTableHeader().setBackground(new Color(46, 204, 113));
        appTable.getTableHeader().setForeground(Color.BLACK);

        List<Application> applications = DataStore.getApplicationsByJobSeeker(currentUser.getUserId());

        for (Application app : applications) {
            Job job = DataStore.findJobById(app.getJobId());
            String resumeStatus = app.getResumePath() != null ? "✓ Uploaded" : "✗ Not uploaded";

            appModel.addRow(new Object[] {
                    app.getApplicationId(),
                    job.getJobTitle(),
                    job.getCompanyName(),
                    app.getAppliedDate(),
                    app.getStatus(),
                    resumeStatus
            });
        }

        JScrollPane scrollPane = new JScrollPane(appTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button to view own resume
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton viewResumeButton = createButton("View My Resume", new Color(52, 152, 219));
        viewResumeButton.addActionListener(e -> {
            int row = appTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select an application!");
                return;
            }

            int appId = (int) appModel.getValueAt(row, 0);
            Application app = DataStore.getApplicationById(appId);

            if (app.getResumePath() == null) {
                JOptionPane.showMessageDialog(this, "No resume uploaded for this application.");
                return;
            }

            try {
                java.io.File resumeFile = new java.io.File(app.getResumePath());
                if (resumeFile.exists()) {
                    java.awt.Desktop.getDesktop().open(resumeFile);
                } else {
                    JOptionPane.showMessageDialog(this, "Resume file not found!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error opening resume: " + ex.getMessage());
            }
        });
        buttonPanel.add(viewResumeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(180, 40));
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadAllJobs() {
        tableModel.setRowCount(0);
        List<Job> jobs = DataStore.getAllJobs();

        for (Job job : jobs) {
            boolean applied = DataStore.hasApplied(job.getJobId(), currentUser.getUserId());
            String status = applied ? "Applied ✓" : "Not Applied";

            tableModel.addRow(new Object[] {
                    job.getJobId(), job.getJobTitle(), job.getCompanyName(),
                    job.getLocation(), job.getSalary(), status
            });
        }
    }

    private void viewJobDetails() {
        int selectedRow = jobsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job!");
            return;
        }

        int jobId = (int) tableModel.getValueAt(selectedRow, 0);
        Job job = DataStore.findJobById(jobId);

        JDialog dialog = new JDialog(this, "Job Details", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        detailsPanel.setBackground(Color.BLACK);

        addDetailRow(detailsPanel, "Job Title:", job.getJobTitle());
        addDetailRow(detailsPanel, "Company:", job.getCompanyName());
        addDetailRow(detailsPanel, "Location:", job.getLocation());
        addDetailRow(detailsPanel, "Salary:", job.getSalary());
        addDetailRow(detailsPanel, "Posted:", job.getPostedDate());

        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Arial", Font.BOLD, 14));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(descLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JTextArea descArea = new JTextArea(job.getDescription());
        descArea.setFont(new Font("Arial", Font.PLAIN, 13));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBackground(new Color(236, 240, 241));
        descArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        descScroll.setPreferredSize(new Dimension(550, 150));
        detailsPanel.add(descScroll);

        dialog.add(new JScrollPane(detailsPanel), BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.setPreferredSize(new Dimension(120, 35));
        closeButton.setBackground(new Color(149, 165, 166));
        closeButton.setForeground(Color.BLACK);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void addDetailRow(JPanel panel, String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setBackground(Color.BLACK);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Arial", Font.BOLD, 14));
        labelComp.setPreferredSize(new Dimension(130, 25));
        row.add(labelComp);

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Arial", Font.PLAIN, 14));
        row.add(valueComp);

        panel.add(row);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void applyForJob() {
        int selectedRow = jobsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job!");
            return;
        }

        int jobId = (int) tableModel.getValueAt(selectedRow, 0);
        String jobTitle = (String) tableModel.getValueAt(selectedRow, 1);

        if (DataStore.hasApplied(jobId, currentUser.getUserId())) {
            JOptionPane.showMessageDialog(this, "Already applied!",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ask if user wants to upload resume
        int uploadChoice = JOptionPane.showConfirmDialog(this,
                "Do you want to upload a resume/CV?",
                "Upload Resume",
                JOptionPane.YES_NO_OPTION);

        String resumePath = null;
        if (uploadChoice == JOptionPane.YES_OPTION) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Resume/CV");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(java.io.File f) {
                    String name = f.getName().toLowerCase();
                    return f.isDirectory() || name.endsWith(".pdf") ||
                            name.endsWith(".doc") || name.endsWith(".docx");
                }

                public String getDescription() {
                    return "Resume Files (*.pdf, *.doc, *.docx)";
                }
            });

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                java.io.File selectedFile = fileChooser.getSelectedFile();

                // Copy file to resumes folder
                try {
                    java.io.File resumeDir = new java.io.File("jobportal_data/resumes");
                    if (!resumeDir.exists()) {
                        resumeDir.mkdirs();
                    }

                    String fileName = "resume_" + currentUser.getUserId() + "_" +
                            System.currentTimeMillis() + "_" + selectedFile.getName();
                    java.io.File destFile = new java.io.File(resumeDir, fileName);

                    // Copy file
                    java.nio.file.Files.copy(selectedFile.toPath(), destFile.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                    resumePath = destFile.getPath();
                    JOptionPane.showMessageDialog(this, "Resume uploaded successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error uploading resume: " + ex.getMessage(),
                            "Upload Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // Submit application with or without resume
        Application app = resumePath != null ? new Application(jobId, currentUser.getUserId(), resumePath)
                : new Application(jobId, currentUser.getUserId());

        if (DataStore.submitApplication(app)) {
            String message = resumePath != null ? "Application with resume submitted successfully!"
                    : "Application submitted successfully!";
            JOptionPane.showMessageDialog(this, message, "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            loadAllJobs();
            tabbedPane.setComponentAt(1, createApplicationsPanel());
        } else {
            JOptionPane.showMessageDialog(this, "Failed to submit application!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Logout?",
                "Confirm", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
}