package APPLICATION;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.sql.*;

public class Dashboard implements ActionListener {

    JFrame jFrame2;
    JLabel jtittle;
    JButton jBAddTran, jBViewR, jBbudgetp;
    JTextField jTF_bal, jTFtotal, jTFexp;
    Statement stmt;
    Connection con;
    ResultSet rs, pe, pp;
    public String username;
    public String password;

    Font font, font2;

    private void initUI() {
        font = new Font("Consolas", Font.BOLD, 18);
        font2 = new Font("Oswald", Font.BOLD, 22);

        jFrame2 = new JFrame("Dashboard");
        jFrame2.setBounds(350, 80, 800, 750);
        jFrame2.getContentPane().setBackground(new Color(240, 248, 255)); // Light background

        jtittle = new JLabel("Personal Finance Management ");
        jtittle.setFont(new Font("Serif", Font.BOLD, 26));
        jtittle.setForeground(new Color(25, 25, 112)); // Dark Blue
        jtittle.setBounds(220, 5, 700, 100);

        // --- Text Fields with background ---
        jTF_bal = new JTextField();
        jTF_bal.setBounds(60, 120, 170, 80);
        jTF_bal.setBackground(new Color(224, 255, 255)); // Light Cyan
        jTF_bal.setHorizontalAlignment(JTextField.CENTER);

        jTFtotal = new JTextField();
        jTFtotal.setBounds(300, 120, 170, 80);
        jTFtotal.setBackground(new Color(240, 255, 240)); // Light Green
        jTFtotal.setHorizontalAlignment(JTextField.CENTER);

        jTFexp = new JTextField();
        jTFexp.setBounds(530, 120, 170, 80);
        jTFexp.setBackground(new Color(255, 228, 225)); // Light Red
        jTFexp.setHorizontalAlignment(JTextField.CENTER);

        // --- Labels ---
        JLabel lbl_exp = new JLabel("TOTAL BALANCE");
        lbl_exp.setFont(font2);
        lbl_exp.setForeground(new Color(0, 102, 102));
        lbl_exp.setBounds(75, 210, 200, 30);

        JLabel lbl_income = new JLabel("TOTAL INCOME");
        lbl_income.setFont(font2);
        lbl_income.setForeground(new Color(34, 139, 34));
        lbl_income.setBounds(315, 210, 200, 30);

        JLabel lbl_bal = new JLabel("TOTAL EXPENSE");
        lbl_bal.setFont(font2);
        lbl_bal.setForeground(new Color(178, 34, 34));
        lbl_bal.setBounds(540, 210, 200, 30);

        // --- Buttons with colors ---
        // --- Buttons with colors ---
        jBAddTran = new JButton("ADD TRANSACTION");
        jBAddTran.setFont(font);
        jBAddTran.setBounds(280, 300, 250, 70);  // shifted to middle
        jBAddTran.setBackground(new Color(70, 130, 180)); // Steel Blue
        jBAddTran.setForeground(Color.WHITE);

        jBViewR = new JButton("VIEW REPORT");
        jBViewR.setFont(font);
        jBViewR.setBounds(280, 400, 250, 70);  // shifted to middle
        jBViewR.setBackground(new Color(34, 139, 34)); // Green
        jBViewR.setForeground(Color.WHITE);

        jBbudgetp = new JButton("BUDGET PLANNING");
        jBbudgetp.setFont(font);
        jBbudgetp.setBounds(280, 500, 250, 70);  // shifted to middle
        jBbudgetp.setBackground(new Color(255, 140, 0)); // Orange
        jBbudgetp.setForeground(Color.WHITE);


        // --- Add to Frame ---
        jFrame2.setLayout(null);
        jFrame2.add(jtittle);
        jFrame2.add(jTF_bal);
        jFrame2.add(jTFtotal);
        jFrame2.add(jTFexp);
        jFrame2.add(lbl_exp);
        jFrame2.add(lbl_income);
        jFrame2.add(lbl_bal);
        jFrame2.add(jBAddTran);
        jFrame2.add(jBViewR);
        jFrame2.add(jBbudgetp);

        jBAddTran.addActionListener(this);
        jBViewR.addActionListener(this);

        jFrame2.setVisible(true);
    }

    public Dashboard() {
        initUI(); // Use colored UI here
    }

    public Dashboard(String name, String pass, Connection con, Statement stmt) throws SQLException {
        username = name;
        password = pass;
        this.con = con;
        this.stmt = stmt;

        // 1. Get the logged-in user's ID
        String getUserIdQuery = "SELECT user_id FROM Users WHERE username=? AND password=?";
        PreparedStatement pst1 = con.prepareStatement(getUserIdQuery);
        pst1.setString(1, name);
        pst1.setString(2, pass);
        ResultSet rsUser = pst1.executeQuery();

        int userId = -1;
        if (rsUser.next()) {
            userId = rsUser.getInt("user_id");
        }

        // 2. Fetch Finance Summary (expense only)
        String qry = "SELECT expense FROM FinanceSummary WHERE user_id=?";
        PreparedStatement pst2 = con.prepareStatement(qry);
        pst2.setInt(1, userId);
        ResultSet rs = pst2.executeQuery();

        double expense = 0.0;
        if (rs.next()) {
            expense = rs.getDouble("expense");
        }

        // 3. Fetch total income from Users table
        String incomeQry = "SELECT total_income FROM Users WHERE user_id=?";
        PreparedStatement pst3 = con.prepareStatement(incomeQry);
        pst3.setInt(1, userId);
        ResultSet rsIncome = pst3.executeQuery();

        double income = 0.0;
        if (rsIncome.next()) {
            income = rsIncome.getDouble("total_income");
        }

        // ✅ Calculate balance
        double balance = income - expense;

        // --- Load UI ---
        initUI();
        jTFtotal.setFont(font2);
        jTFexp.setFont(font2);
        jTF_bal.setFont(font2);

        jTFtotal.setText("   " + income);   // Total Income
        jTFexp.setText("   " + expense);   // Total Expense
        jTF_bal.setText("   " + balance);  // Balance = income - expense
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == jBAddTran) {
                Transaction tr = new Transaction(username, password, con, stmt);
            }
            if (e.getSource()==jBViewR){
                ViewReport vr= new ViewReport(username, password, con, stmt);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
