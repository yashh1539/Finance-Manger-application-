package APPLICATION;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LogIn implements ActionListener {
    JFrame jFrame1 = null;
    JLabel jLabel_name, jLabel_passward = null;
    JButton jButton_login, jButton_signup;
    JTextField jTextField_name;
    Statement stmt;
    Connection con;
    ResultSet rs;
    Font font, font2;
    JPasswordField jPasswordField_password;

    public LogIn() {
        font = new Font("Consolas", Font.BOLD, 22);
        font2 = new Font("Consolas", Font.PLAIN, 16);

        jFrame1 = new JFrame("LogIn Page");
        jFrame1.setBounds(400, 200, 350, 280);
        jFrame1.getContentPane().setBackground(Color.WHITE); // White background

        jLabel_name = new JLabel("Username:");
        jLabel_name.setBounds(40, 50, 100, 30);
        jLabel_name.setFont(font2);

        jLabel_passward = new JLabel("Password:");
        jLabel_passward.setBounds(40, 100, 100, 30);
        jLabel_passward.setFont(font2);

        jTextField_name = new JTextField();
        jTextField_name.setBounds(140, 50, 150, 30);
        jTextField_name.setFont(font2);
        jTextField_name.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 2));

        jPasswordField_password = new JPasswordField();
        jPasswordField_password.setBounds(140, 100, 150, 30);
        jPasswordField_password.setFont(font2);
        jPasswordField_password.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 2));
        jPasswordField_password.setEchoChar('*');

        // Modern Blue Buttons
        jButton_login = new JButton("Login");
        jButton_login.setBounds(40, 160, 110, 35);
        jButton_login.setBackground(new Color(30, 144, 255));
        jButton_login.setForeground(Color.WHITE);
        jButton_login.setFont(new Font("Arial", Font.BOLD, 14));
        jButton_login.setFocusPainted(false);
        jButton_login.setBorder(BorderFactory.createEmptyBorder());

        jButton_signup = new JButton("Sign Up");
        jButton_signup.setBounds(180, 160, 110, 35);
        jButton_signup.setBackground(new Color(34, 139, 34));
        jButton_signup.setForeground(Color.WHITE);
        jButton_signup.setFont(new Font("Arial", Font.BOLD, 14));
        jButton_signup.setFocusPainted(false);
        jButton_signup.setBorder(BorderFactory.createEmptyBorder());

        jFrame1.add(jLabel_name);
        jFrame1.add(jLabel_passward);
        jFrame1.add(jTextField_name);
        jFrame1.add(jPasswordField_password);
        jFrame1.add(jButton_login);
        jFrame1.add(jButton_signup);

        jFrame1.setLayout(null);
        jFrame1.setVisible(true);

        jButton_login.addActionListener(this);
        jButton_signup.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == jButton_signup) {
                SignIn ss = new SignIn();
                jFrame1.setVisible(false);
            }
            if (stmt == null) {
                connectToDB();
            }
            if (e.getSource() == jButton_login) {
                verification();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void connectToDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/financemanagement?useTimezone=true&serverTimezone=UTC",
                    "root", "sa123");
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void verification() throws SQLException {
        String username = jTextField_name.getText();
        String password = jPasswordField_password.getText();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(jFrame1, "Enter login ID and password.");
        } else {
            String qry = "SELECT * FROM users WHERE username='" + username + "' AND password='" + password + "';";
            rs = stmt.executeQuery(qry);

            if (!rs.next()) {
                JOptionPane.showMessageDialog(jFrame1, "Login Failed. Invalid username or password.");
            } else {
                JOptionPane.showMessageDialog(jFrame1, "Login Successful");
                Dashboard db = new Dashboard(username, password, con, stmt);

                jFrame1.setVisible(false);
            }
        }
    }
}
