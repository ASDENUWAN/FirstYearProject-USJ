/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import database.Connect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.Table;

/**
 *
 * @author Sachii
 */
public class TableController {

   
    
    public static boolean addTable(int no,int seats) {
        try {
            String sql = "INSERT INTO tables (tableNo,seats) VALUES (?, ?)";

            Connection conn = Connect.getConnection(); // Assuming Connect.getConnection() returns a valid JDBC connection
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, no);
            pstmt.setInt(2, seats);
            int res = pstmt.executeUpdate();
            if (res > 0)
                return true;
        } catch (SQLException ex) {
            System.out.println("Error inserting row: " + ex.getMessage());
        }
        return false;
    }
    
    public static boolean updateTableSeats(int tableNo, int seats) {
        try {
            String sql = "UPDATE tables SET seats = ? WHERE tableNo = ?";

            Connection conn = Connect.getConnection(); // Assuming Connect.getConnection() returns a valid JDBC connection
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, seats);
            pstmt.setInt(2, tableNo);
            int res = pstmt.executeUpdate();
            if (res > 0)
                return true;
        } catch (SQLException ex) {
            System.out.println("Error updating table seats: " + ex.getMessage());
        }
        return false;
    }

    public static boolean deleteTable(int no) {
        String sql = "DELETE FROM tables WHERE tableNo = ?";
        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set the parameters
            pstmt.setInt(1, no);
            // Execute the delete statement
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }
    
    public static Table getTable(int no) {
       
        String sql = "SELECT * FROM tables WHERE tableNo = ?";
        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set the parameters
            pstmt.setInt(1, no);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Check if a table with the given number exists
                if (rs.next()) {
                    // Retrieve data from the result set and create a Table object
                    int tableNo = rs.getInt("tableNo");
                    int seats = rs.getInt("seats");

                  Table t = new Table(tableNo, seats);
                  return t;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

     
    public static ArrayList<Table> getTableBySeats(int seats) {
        Table table;
        ArrayList<Table>tableList = new ArrayList<Table>();
        try {
            String sql = "SELECT * FROM tables WHERE seats like '"+seats+"%'";

            Connection conn = Connect.getConnection(); // Assuming Connect.getConnection() returns a valid JDBC connection
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int tableno = rs.getInt("tableNo"); // Assuming the column name for table ID is "tableId"
                
                table = new Table(tableno, seats);
                tableList.add(table);
            }
            return tableList;
        } catch (SQLException ex) {
            System.out.println("Error retrieving table details: " + ex.getMessage());
        }
        return tableList;
    }

    public static ArrayList<Table> displayTables(){
        Table table;
        ArrayList<Table>tableList = new ArrayList<Table>();
        String sql = "SELECT * FROM tables";
        try {   
            Connection conn = Connect.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Loop through the result set
            while (rs.next()) {
                int no = rs.getInt("tableNo");
                int seats = rs.getInt("seats");
                
                table = new Table(no, seats);
                tableList.add(table);
            }
            return tableList;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return tableList;
    }
    
}
