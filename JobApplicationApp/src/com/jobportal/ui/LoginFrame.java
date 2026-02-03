package com.jobportal.ui;

import com.jobportal.data.DataStore;
import com.jobportal.model.User;
import com.jobportal.model.User.UserType;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Job Portal - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(52, 152, 219);
                Color color2 = new Color(41, 128, 185);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(null);

        JLabel titleLabel = new JLabel("ONLINE JOB PORTAL");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(90, 20, 300, 40);
        mainPanel.add(titleLabel);

        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBounds(50, 80, 350, 180);
        loginPanel.setLayout(null);
        loginPanel.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setBounds(30, 20, 100, 25);
        loginPanel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBounds(30, 50, 290, 30);
        loginPanel.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passLabel.setBounds(30, 90, 100, 25);
        loginPanel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBounds(30, 120, 290, 30);
        loginPanel.add(passwordField);

        mainPanel.add(loginPanel);

        JButton loginButton = new JButton("LOGIN");
        loginButton.setBounds(50, 275, 160, 35);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(46, 204, 113));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> handleLogin());
        mainPanel.add(loginButton);

        JButton registerButton = new JButton("REGISTER");
        registerButton.setBounds(240, 275, 160, 35);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(52, 152, 219));
        registerButton.setForeground(Color.BLACK);
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> openRegistration());
        mainPanel.add(registerButton);

        add(mainPanel);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password!",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = DataStore.loginUser(username, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this, "Welcome, " + user.getFullName() + "!",
                    "Login Successful", JOptionPane.INFORMATION_MESSAGE);

            if (user.getUserType() == UserType.EMPLOYER) {
                new EmployerDashboard(user).setVisible(true);
            } else {
                new JobSeekerDashboard(user).setVisible(true);
            }

            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password!",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    private void openRegistration() {
        new RegistrationFrame().setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
