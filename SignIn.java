package APPLICATION;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SignIn implements ActionListener {
    JFrame jFrame1 = null;
    JLabel jLabel_name, jLabel_email, jLabel_contact, jLabel_password = null;
    JButton jButton_signin;
    JTextField jTextField_name, jTextField_email, jTextField_contact;
    Statement stmt;
    Connection con;
    ResultSet rs;
    Font font, font2;
    JPasswordField jPasswordField_password;

    public SignIn() {
        font = new Font("Consolas", Font.BOLD, 22);
        font2 = new Font("Consolas", Font.PLAIN, 16);

        // Frame
        jFrame1 = new JFrame("Sign In Page");
        jFrame1.setBounds(400, 200, 450, 400);
        jFrame1.getContentPane().setBackground(new Color(240, 248, 255)); // Light blue background

        // Username
        jLabel_name = new JLabel("USERNAME:");
        jLabel_name.setBounds(50, 40, 120, 30);
        jLabel_name.setFont(font2);
        jTextField_name = new JTextField();
        jTextField_name.setBounds(180, 40, 200, 30);

        // Email
        jLabel_email = new JLabel("EMAIL:");
        jLabel_email.setBounds(50, 90, 120, 30);
        jLabel_email.setFont(font2);
        jTextField_email = new JTextField();
        jTextField_email.setBounds(180, 90, 200, 30);

        // Contact
        jLabel_contact = new JLabel("CONTACT:");
        jLabel_contact.setBounds(50, 140, 120, 30);
        jLabel_contact.setFont(font2);
        jTextField_contact = new JTextField();
        jTextField_contact.setBounds(180, 140, 200, 30);

        // Password
        jLabel_password = new JLabel("PASSWORD:");
        jLabel_password.setBounds(50, 190, 120, 30);
        jLabel_password.setFont(font2);
        jPasswordField_password = new JPasswordField();
        jPasswordField_password.setBounds(180, 190, 200, 30);
        jPasswordField_password.setEchoChar('*');

        // Sign In Button
        jButton_signin = new JButton("Sign In");
        jButton_signin.setBounds(150, 260, 140, 40);
        jButton_signin.setBackground(new Color(70, 130, 180)); // Steel blue
        jButton_signin.setForeground(Color.WHITE);
        jButton_signin.setFont(new Font("Consolas", Font.BOLD, 18));
        jButton_signin.setFocusPainted(false);

        // Adding components
        jFrame1.add(jLabel_name);
        jFrame1.add(jTextField_name);

        jFrame1.add(jLabel_email);
        jFrame1.add(jTextField_email);

        jFrame1.add(jLabel_contact);
        jFrame1.add(jTextField_contact);

        jFrame1.add(jLabel_password);
        jFrame1.add(jPasswordField_password);

        jFrame1.add(jButton_signin);

        jFrame1.setLayout(null);
        jFrame1.setVisible(true);

        jButton_signin.addActionListener(this);
    }

    public void connectToDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/financemanagement?useTimezone=true&serverTimezone=UTC",
                    "root", "sa123");
            System.out.println("Database connected!");
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (con == null) {
                connectToDB();
            }

            if (e.getSource() == jButton_signin) {
                String username = jTextField_name.getText();
                String email = jTextField_email.getText();
                String contact = jTextField_contact.getText();
                String password = new String(jPasswordField_password.getPassword());

                if (username.isEmpty() || email.isEmpty() || contact.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(jFrame1, "⚠️ Please fill all fields!");
                    return;
                }

                String query = "INSERT INTO Users (username, email, contact, password) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(query);
                pstmt.setString(1, username);
                pstmt.setString(2, email);
                pstmt.setString(3, contact);
                pstmt.setString(4, password);

                int rows = pstmt.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(jFrame1, "✅ User registered successfully!");
                    jTextField_name.setText("");
                    jTextField_email.setText("");
                    jTextField_contact.setText("");
                    jPasswordField_password.setText("");
                    jFrame1.dispose();   // close SignIn
                    new LogIn();         // open Login
                } else {
                    JOptionPane.showMessageDialog(jFrame1, "❌ Error: Could not register user.");
                }
                pstmt.close();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(jFrame1, "Database Error: " + ex.getMessage());
        }
    }
}
