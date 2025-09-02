package APPLICATION;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Transaction implements ActionListener {

    JFrame jFrame3;
    JTextField textDate, textAmount;
    JButton jBAdd, jBCancel;
    JLabel lblDate, lblType, lblCategory, lblAmount, lblNote;
    JComboBox<String> comboBoxType, comboBoxCate;
    JTextArea textNote;

    Connection con;
    Statement stmt;
    ResultSet rs;

    private static String username;
    private static String password;

    Font font1 = new Font("Segoe UI", Font.BOLD, 15);
    Font font2 = new Font("Segoe UI", Font.PLAIN, 14);

    // constructor for normal use
    public Transaction() {
        initUI();
    }

    // constructor for db connection usage
    public Transaction(String name, String pass, Connection con, Statement stmt) throws SQLException {
        this.con = con;
        this.stmt = stmt;
        username = name;
        password = pass;
        initUI();

        // load last date if available
        String qry = "SELECT date FROM Transactions t JOIN Users u ON t.user_id = u.user_id " +
                "WHERE u.username=? AND u.password=? ORDER BY t.transaction_id DESC LIMIT 1";
        PreparedStatement ps = con.prepareStatement(qry);
        ps.setString(1, name);
        ps.setString(2, pass);
        rs = ps.executeQuery();

        if (rs.next()) {
            textDate.setText(rs.getString("date"));
        } else {
            textDate.setText("YYYY-MM-DD");
        }
    }

    private void initUI() {
        jFrame3 = new JFrame("Add Transaction");
        jFrame3.setBounds(450, 120, 400, 420);
        jFrame3.setLayout(null);
        jFrame3.getContentPane().setBackground(new Color(240, 248, 255));

        // Labels
        lblDate = new JLabel("Date:");
        lblDate.setBounds(50, 50, 100, 30);
        lblDate.setFont(font1);

        lblType = new JLabel("Type:");
        lblType.setBounds(50, 90, 100, 30);
        lblType.setFont(font1);

        lblCategory = new JLabel("Category:");
        lblCategory.setBounds(50, 130, 100, 30);
        lblCategory.setFont(font1);

        lblAmount = new JLabel("Amount:");
        lblAmount.setBounds(50, 170, 100, 30);
        lblAmount.setFont(font1);

        lblNote = new JLabel("Note:");
        lblNote.setBounds(50, 210, 100, 30);
        lblNote.setFont(font1);

        // Inputs
        textDate = new JTextField();
        textDate.setBounds(150, 50, 180, 25);
        textDate.setFont(font2);

        String[] typee = {" ", "Income", "Expense"};
        comboBoxType = new JComboBox<>(typee);
        comboBoxType.setBounds(150, 90, 180, 25);
        comboBoxType.setFont(font2);

        String[] items = {" ", "Food", "Rent", "Transport", "Other", "Total"};
        comboBoxCate = new JComboBox<>(items);
        comboBoxCate.setBounds(150, 130, 180, 25);
        comboBoxCate.setFont(font2);

        textAmount = new JTextField();
        textAmount.setBounds(150, 170, 180, 25);
        textAmount.setFont(font2);

        textNote = new JTextArea();
        textNote.setBounds(150, 210, 180, 60);
        textNote.setFont(font2);
        textNote.setLineWrap(true);
        textNote.setWrapStyleWord(true);

        // Buttons
        jBAdd = new JButton("Add");
        jBAdd.setBounds(100, 300, 80, 35);
        jBAdd.setBackground(new Color(0, 153, 76));
        jBAdd.setForeground(Color.WHITE);
        jBAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jBAdd.addActionListener(this);

        jBCancel = new JButton("Cancel");
        jBCancel.setBounds(200, 300, 100, 35);
        jBCancel.setBackground(new Color(204, 0, 0));
        jBCancel.setForeground(Color.WHITE);
        jBCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jBCancel.addActionListener(this);

        // Add components
        jFrame3.add(lblDate);
        jFrame3.add(lblType);
        jFrame3.add(lblCategory);
        jFrame3.add(lblAmount);
        jFrame3.add(lblNote);

        jFrame3.add(textDate);
        jFrame3.add(comboBoxType);
        jFrame3.add(comboBoxCate);
        jFrame3.add(textAmount);
        jFrame3.add(textNote);

        jFrame3.add(jBAdd);
        jFrame3.add(jBCancel);

        jFrame3.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jBAdd) {
            handleTransaction();
        } else if (e.getSource() == jBCancel) {
            jFrame3.dispose();

        }
    }

    private void handleTransaction() {
        try {
            String date = textDate.getText();
            String type = (String) comboBoxType.getSelectedItem();
            String category = (String) comboBoxCate.getSelectedItem();
            float amount = Float.parseFloat(textAmount.getText());
            String note = textNote.getText();

            // get user_id from Users
            String getUserQry = "SELECT user_id FROM Users WHERE username=? AND password=?";
            PreparedStatement psUser = con.prepareStatement(getUserQry);
            psUser.setString(1, username);
            psUser.setString(2, password);
            ResultSet rsUser = psUser.executeQuery();

            if (rsUser.next()) {
                int userId = rsUser.getInt("user_id");

                // insert into Transactions
                String insertQry = "INSERT INTO Transactions (user_id, date, type, category, amount, note, income) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(insertQry);
                ps.setInt(1, userId);
                ps.setString(2, date);
                ps.setString(3, type);
                ps.setString(4, category);
                ps.setFloat(5, amount);
                ps.setString(6, note);

                // income value
                if ("Income".equalsIgnoreCase(type)) {
                    ps.setFloat(7, amount);
                } else {
                    ps.setFloat(7, 0);
                }
                ps.executeUpdate();

                // update Users.total_income if type=Income
                if ("Income".equalsIgnoreCase(type)) {
                    String updateUserQry = "UPDATE Users SET total_income = total_income + ? WHERE user_id = ?";
                    PreparedStatement ps2 = con.prepareStatement(updateUserQry);
                    ps2.setFloat(1, amount);
                    ps2.setInt(2, userId);
                    ps2.executeUpdate();
                }

                JOptionPane.showMessageDialog(jFrame3, "Transaction added successfully!");
            } else {
                JOptionPane.showMessageDialog(jFrame3, "User not found.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(jFrame3, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new Transaction();
    }
}
