package com.jobportal.ui;

import com.jobportal.data.DataStore;
import com.jobportal.model.Application;
import com.jobportal.model.Job;
import com.jobportal.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployerDashboard extends JFrame {
    private User currentUser;
    private JTable jobsTable;
    private DefaultTableModel tableModel;

    public EmployerDashboard(User user) {
        this.currentUser = user;
        initComponents();
        loadJobs();
    }

    private void initComponents() {
        setTitle("Employer Dashboard - " + currentUser.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setPreferredSize(new Dimension(900, 80));
        headerPanel.setLayout(null);

        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setBounds(20, 15, 400, 30);
        headerPanel.add(welcomeLabel);

        JLabel roleLabel = new JLabel("Employer Dashboard");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        roleLabel.setForeground(Color.BLACK);
        roleLabel.setBounds(20, 45, 200, 20);
        headerPanel.add(roleLabel);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(780, 25, 100, 35);
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> logout());
        headerPanel.add(logoutButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center with table
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.BLACK);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel tableTitle = new JLabel("My Posted Jobs");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 18));
        centerPanel.add(tableTitle, BorderLayout.NORTH);

        String[] columns = { "Job ID", "Title", "Company", "Location", "Salary", "Applications" };
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
        jobsTable.getTableHeader().setBackground(new Color(52, 152, 219));
        jobsTable.getTableHeader().setForeground(Color.BLACK);

        centerPanel.add(new JScrollPane(jobsTable), BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.BLACK);

        JButton postButton = createButton("Post New Job", new Color(46, 204, 113));
        postButton.addActionListener(e -> postJob());
        buttonPanel.add(postButton);

        JButton viewButton = createButton("View Applications", new Color(52, 152, 219));
        viewButton.addActionListener(e -> viewApplications());
        buttonPanel.add(viewButton);

        JButton refreshButton = createButton("Refresh", new Color(149, 165, 166));
        refreshButton.addActionListener(e -> loadJobs());
        buttonPanel.add(refreshButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
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

    private void loadJobs() {
        tableModel.setRowCount(0);
        List<Job> jobs = DataStore.getJobsByEmployerId(currentUser.getUserId());

        for (Job job : jobs) {
            int count = DataStore.getApplicationCount(job.getJobId());
            tableModel.addRow(new Object[] {
                    job.getJobId(), job.getJobTitle(), job.getCompanyName(),
                    job.getLocation(), job.getSalary(), count
            });
        }
    }

    private void postJob() {
        JDialog dialog = new JDialog(this, "Post New Job", true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(null);

        JLabel titleLabel = new JLabel("Job Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(20, 15, 200, 30);
        dialog.add(titleLabel);

        JLabel jobTitleLabel = new JLabel("Job Title:");
        jobTitleLabel.setBounds(20, 60, 100, 25);
        dialog.add(jobTitleLabel);

        JTextField jobTitleField = new JTextField();
        jobTitleField.setBounds(130, 60, 340, 30);
        dialog.add(jobTitleField);

        JLabel companyLabel = new JLabel("Company:");
        companyLabel.setBounds(20, 100, 100, 25);
        dialog.add(companyLabel);

        JTextField companyField = new JTextField();
        companyField.setBounds(130, 100, 340, 30);
        dialog.add(companyField);

        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setBounds(20, 140, 100, 25);
        dialog.add(locationLabel);

        JTextField locationField = new JTextField();
        locationField.setBounds(130, 140, 340, 30);
        dialog.add(locationField);

        JLabel salaryLabel = new JLabel("Salary Range:");
        salaryLabel.setBounds(20, 180, 100, 25);
        dialog.add(salaryLabel);

        JTextField salaryField = new JTextField();
        salaryField.setBounds(130, 180, 340, 30);
        dialog.add(salaryField);

        JLabel descLabel = new JLabel("Description:");
        descLabel.setBounds(20, 220, 100, 25);
        dialog.add(descLabel);

        JTextArea descArea = new JTextArea();
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBounds(130, 220, 340, 100);
        dialog.add(descScroll);

        JButton postButton = new JButton("POST JOB");
        postButton.setBounds(130, 340, 150, 40);
        postButton.setBackground(new Color(46, 204, 113));
        postButton.setForeground(Color.BLACK);
        postButton.setFocusPainted(false);
        postButton.addActionListener(e -> {
            String title = jobTitleField.getText().trim();
            String company = companyField.getText().trim();
            String location = locationField.getText().trim();
            String salary = salaryField.getText().trim();
            String description = descArea.getText().trim();

            if (title.isEmpty() || company.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill required fields!");
                return;
            }

            Job job = new Job(currentUser.getUserId(), title, company, description, location, salary);
            DataStore.addJob(job);
            JOptionPane.showMessageDialog(dialog, "Job posted successfully!");
            loadJobs();
            dialog.dispose();
        });
        dialog.add(postButton);

        JButton cancelButton = new JButton("CANCEL");
        cancelButton.setBounds(300, 340, 150, 40);
        cancelButton.setBackground(new Color(149, 165, 166));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> dialog.dispose());
        dialog.add(cancelButton);

        dialog.setVisible(true);
    }

    private void viewApplications() {
        int selectedRow = jobsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job!");
            return;
        }

        int jobId = (int) tableModel.getValueAt(selectedRow, 0);
        String jobTitle = (String) tableModel.getValueAt(selectedRow, 1);

        List<Application> applications = DataStore.getApplicationsByJobId(jobId);

        if (applications.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No applications yet.");
            return;
        }

        JDialog dialog = new JDialog(this, "Applications: " + jobTitle, true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        String[] columns = { "Applicant", "Email", "Applied On", "Status" };
        DefaultTableModel appModel = new DefaultTableModel(columns, 0);

        for (Application app : applications) {
            User applicant = DataStore.findUserById(app.getJobSeekerId());
            appModel.addRow(new Object[] {
                    applicant.getFullName(), applicant.getEmail(),
                    app.getAppliedDate(), app.getStatus()
            });
        }

        JTable appTable = new JTable(appModel);
        appTable.setRowHeight(30);
        dialog.add(new JScrollPane(appTable));
        dialog.setVisible(true);
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