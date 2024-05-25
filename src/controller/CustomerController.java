/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet; 
import database.Connect;
import java.sql.Statement;
import java.util.ArrayList;
import model.Customer;

/**
 *
 * @author Sachii
 */
public class CustomerController {
    
    
    
    public static boolean addCustomer(Customer customer) {
        String sql = "INSERT INTO customer (name, email, contact, username, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set the parameters
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getContact());
            pstmt.setString(4, customer.getUsername());
            pstmt.setString(5, customer.getPassword());
            // Execute the insert statement
            pstmt.executeUpdate();
            return true; // Customer added successfully
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false; // Error occurred while adding customer
        }
    }
    
    public static Customer customerLogin(String username, String password) {
        String sql = "SELECT * FROM customer WHERE username = ? AND password = ?";
        Customer cus = new Customer();
        
        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set the parameters
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("cid");
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String contact = rs.getString("contact");
                    
                    cus = new Customer(id, name, email, contact, username, password);
                    return cus;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        // Return null if login fails or an error occurs
        return null;
    }
    
    public static Customer customerDetails(int id) {
        String sql = "SELECT * FROM customer WHERE cid = ?";
        Customer cus = new Customer();
        
        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set the parameters
            pstmt.setInt(1, id);
            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String contact = rs.getString("contact");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    cus = new Customer(id, name, email, contact, username, password);
                    return cus;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        // Return null if login fails or an error occurs
        return cus;
    }
    
    public static ArrayList<Customer> serachCustomer(String sname) {
        String sql = "select * from customer where name like '"+sname+"%'";
        Customer cus = new Customer();
        ArrayList<Customer>cusList = new ArrayList<>();
        try (Connection conn = Connect.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
       
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("cid");
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String contact = rs.getString("contact");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    cus = new Customer(id, name, email, contact, username, password);
                    cusList.add(cus);
                }
                return cusList;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        // Return null if login fails or an error occurs
        return cusList;
    }
    
    public static boolean updateCustomerDetails(Customer updatedCustomer) {
        String sql = "UPDATE customer SET name=?, email=?, contact=?, username=?, password=? WHERE cid=?";

        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set the parameters
            pstmt.setString(1, updatedCustomer.getName());
            pstmt.setString(2, updatedCustomer.getEmail());
            pstmt.setString(3, updatedCustomer.getContact());
            pstmt.setString(4, updatedCustomer.getUsername());
            pstmt.setString(5, updatedCustomer.getPassword());
            pstmt.setInt(6, updatedCustomer.getcId());

            // Execute the update query
            int rowsUpdated = pstmt.executeUpdate();

            // Return true if at least one row is updated
            return rowsUpdated > 0;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            // Return false if an error occurs
            return false;
        }
    }

    public static boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM customer WHERE cid = ?";
        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set the parameter
            pstmt.setInt(1, customerId);
            // Execute the delete statement
            int rowsAffected = pstmt.executeUpdate();
            // Check if any rows were affected (i.e., if the customer was deleted)
            return rowsAffected > 0;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false; // Error occurred while deleting customer
        }
    }
    
    public static ArrayList<Customer> displayCus(){
        Customer cus = new Customer();
        ArrayList<Customer>cusList = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        try {   
            Connection conn = Connect.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Loop through the result set
            while (rs.next()) {
                int id = rs.getInt("cid");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String contact = rs.getString("contact");
                String username = rs.getString("username");
                
                cus = new Customer(id, name, email, contact, username);
                cusList.add(cus);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        return cusList;
    }
    
}
