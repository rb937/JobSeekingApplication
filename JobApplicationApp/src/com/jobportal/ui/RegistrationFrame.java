package com.jobportal.ui;

import com.jobportal.data.DataStore;
import com.jobportal.model.User;
import com.jobportal.model.User.UserType;

import javax.swing.*;
import java.awt.*;

public class RegistrationFrame extends JFrame {
    private JTextField usernameField, emailField, fullNameField;
    private JPasswordField passwordField, confirmPasswordField;
    private JComboBox<String> userTypeCombo;

    public RegistrationFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Job Portal - Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(155, 89, 182);
                Color color2 = new Color(142, 68, 173);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(null);

        JLabel titleLabel = new JLabel("CREATE ACCOUNT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(140, 20, 250, 40);
        mainPanel.add(titleLabel);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setBounds(50, 80, 400, 380);
        formPanel.setLayout(null);
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));

        addLabel(formPanel, "Full Name:", 20);
        fullNameField = addTextField(formPanel, 45);

        addLabel(formPanel, "Username:", 85);
        usernameField = addTextField(formPanel, 110);

        addLabel(formPanel, "Email:", 150);
        emailField = addTextField(formPanel, 175);

        addLabel(formPanel, "Password:", 215);
        passwordField = addPasswordField(formPanel, 240);

        addLabel(formPanel, "Confirm Password:", 280);
        confirmPasswordField = addPasswordField(formPanel, 305);

        JLabel typeLabel = new JLabel("I am a:");
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        typeLabel.setBounds(30, 345, 100, 25);
        formPanel.add(typeLabel);

        userTypeCombo = new JComboBox<>(new String[] { "Job Seeker", "Employer" });
        userTypeCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        userTypeCombo.setBounds(120, 345, 250, 30);
        formPanel.add(userTypeCombo);

        mainPanel.add(formPanel);

        JButton registerButton = new JButton("REGISTER");
        registerButton.setBounds(50, 475, 180, 40);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(46, 204, 113));
        registerButton.setForeground(Color.BLACK);
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> handleRegistration());
        mainPanel.add(registerButton);

        JButton backButton = new JButton("BACK TO LOGIN");
        backButton.setBounds(270, 475, 180, 40);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(231, 76, 60));
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> backToLogin());
        mainPanel.add(backButton);

        add(mainPanel);
    }

    private void addLabel(JPanel panel, String text, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setBounds(30, y, 150, 25);
        panel.add(label);
    }

    private JTextField addTextField(JPanel panel, int y) {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBounds(30, y, 340, 30);
        panel.add(field);
        return field;
    }

    private JPasswordField addPasswordField(JPanel panel, int y) {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBounds(30, y, 340, 30);
        panel.add(field);
        return field;
    }

    private void handleRegistration() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!",
                    "Password Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (DataStore.findUserByUsername(username) != null) {
            JOptionPane.showMessageDialog(this, "Username already exists!",
                    "Username Taken", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UserType userType = userTypeCombo.getSelectedIndex() == 0 ? UserType.JOB_SEEKER : UserType.EMPLOYER;

        User user = new User(username, password, email, fullName, userType);

        if (DataStore.registerUser(user)) {
            JOptionPane.showMessageDialog(this,
                    "Registration successful!\nYou can now login.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            backToLogin();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void backToLogin() {
        new LoginFrame().setVisible(true);
        dispose();
    }
}
