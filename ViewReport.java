package APPLICATION;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewReport {
    JFrame frame;
    JTable table;
    JScrollPane scrollPane;

    Connection con;
    Statement stmt;
    String username;
    String password;

    public ViewReport(String username, String password, Connection con, Statement stmt) {
        this.username = username;
        this.password = password;
        this.con = con;
        this.stmt = stmt;

        initUI();
        loadTransactions();
    }

    private void initUI() {
        frame = new JFrame("Your Transactions");
        frame.setSize(700, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        table = new JTable();
        scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void loadTransactions() {
        try {
            // get user_id
            String getUserQry = "SELECT user_id FROM Users WHERE username=? AND password=?";
            PreparedStatement psUser = con.prepareStatement(getUserQry);
            psUser.setString(1, username);
            psUser.setString(2, password);
            ResultSet rsUser = psUser.executeQuery();

            if (rsUser.next()) {
                int userId = rsUser.getInt("user_id");

                // fetch only this user's transactions
                String qry = "SELECT date, type, category, amount, note, income " +
                        "FROM Transactions WHERE user_id=? ORDER BY date DESC";
                PreparedStatement ps = con.prepareStatement(qry);
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();

                // convert ResultSet to JTable
                table.setModel(buildTableModel(rs));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    // helper to convert ResultSet into TableModel
    public static javax.swing.table.TableModel buildTableModel(ResultSet rs) throws SQLException {
        java.sql.ResultSetMetaData metaData = rs.getMetaData();

        // column names
        java.util.Vector<String> columnNames = new java.util.Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data
        java.util.Vector<java.util.Vector<Object>> data = new java.util.Vector<>();
        while (rs.next()) {
            java.util.Vector<Object> vector = new java.util.Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new javax.swing.table.DefaultTableModel(data, columnNames);
    }
}
