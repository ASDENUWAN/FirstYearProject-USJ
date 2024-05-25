/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.Connection;
import java.sql.PreparedStatement; 
import java.sql.ResultSet; 
import java.sql.SQLException;
import java.sql.Statement;
import database.Connect;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import model.Booking;
import model.Customer;
import model.Table;

/**
 *
 * @author Sachii
 */


public class ReservationController {
    
    public ReservationController() {
    }

    // Check if a table is available for the given date and time
    private static boolean isTableAvailable(Table table, LocalDateTime startTime, LocalDateTime endTime) {
        // Check if the table is already booked during the specified time
        for (Booking booking : getAllBookings()) {
            if (booking.getTable().getTableNumber()==table.getTableNumber()&&
                !(startTime.isAfter(booking.getEndTime())||startTime.isEqual(booking.getEndTime())) &&
                !(endTime.isBefore(booking.getStartTime())||endTime.isEqual(booking.getStartTime()))) {
                return false; // Table is not available
            }
        }
        return true; // Table is available
    }
    
    private static boolean isTableAvailableForNewTime(int  id,int tableno, LocalDateTime startTime, LocalDateTime endTime) {
        // Check if the table is already booked during the specified time
        
        Table table = TableController.getTable(tableno);
        
        for (Booking booking : getAllBookings()) {
            if (booking.getBid()!=id&&booking.getTable().getTableNumber()==table.getTableNumber()&&
                !(startTime.isAfter(booking.getEndTime())||startTime.isEqual(booking.getEndTime())) &&
                !(endTime.isBefore(booking.getStartTime())||endTime.isEqual(booking.getStartTime()))) {
                return false; // Table is not available
            }
        }
        return true; // Table is available
    }
    
    public static boolean updateReservation(int bookingId,int tableno, LocalDateTime newStartTime, LocalDateTime newEndTime) {
    
        if (isTableAvailableForNewTime(bookingId,tableno, newStartTime, newEndTime)) {
            try {
                String sql = "UPDATE booking SET start_time = ?, end_time = ? WHERE bid = ?";
                Connection conn = Connect.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, newStartTime.toLocalTime().toString());
                pstmt.setString(2, newEndTime.toLocalTime().toString());
                pstmt.setInt(3, bookingId);
                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    return true; // Update successful
                } else {
                    return false; // Update failed
                }
            } catch (SQLException e) {
                System.out.println("Error updating reservation: " + e.getMessage());
            }
        } 
        return false;
    }

    public static List<Table> getAvailableTables(int seatsNo, LocalDateTime startTime, LocalDateTime endTime) {
        List<Table> availableTables = new ArrayList<>();
        
        // Iterate through all tables to check availability
        for (Table table : TableController.displayTables()) {
            // Check if the table has enough seats
            if (table.getCapacity() == seatsNo) {
                boolean isAvailable = true;
                // Check if the table is already booked during the specified time
                LocalDate currentDate = startTime.toLocalDate();
                while(!currentDate.isAfter(endTime.toLocalDate())){
                    for (Booking booking : getAllBookings()) {
                        if (booking.getTable().getTableNumber() == table.getTableNumber() &&
                                !(startTime.isAfter(booking.getEndTime()) || startTime.isEqual(booking.getEndTime())) &&
                                !(endTime.isBefore(booking.getStartTime())||endTime.isEqual(booking.getStartTime()))) {
                            isAvailable = false; // Table is not available
                            break;
                        }
                    }
                    currentDate = currentDate.plusDays(1);
                }
                if (isAvailable) {
                        availableTables.add(table); // Add available table to the list
                }
            }
        }
        return availableTables;
    }

    public static List<Table> getAvailableTablesForSpecificDays(int seatsNo, LocalTime startTime, LocalTime endTime,LocalDate startDate, LocalDate endDate, DayOfWeek[] daysOfWeek) {
        List<Table> availableTables = new ArrayList<>();
        
        // Iterate through all tables to check availability
        for (Table table : TableController.displayTables()) {
            // Check if the table has enough seats
            if (table.getCapacity() == seatsNo) {
                boolean isAvailable = true;
                // Iterate through each day within the specified time period
                LocalDate currentDate = startDate;
                while (!currentDate.isAfter(endDate) ){
                    // Check if the current day is in the specified days of the week
                    if (isDayOfWeekInArray(currentDate.getDayOfWeek(), daysOfWeek)) {
                        // Check if the table is already booked during the specified time on the current day
                        for (Booking booking : getAllBookings()) {
                            LocalDateTime startDateTime = LocalDateTime.of(currentDate, startTime);
                            LocalDateTime endDateTime = LocalDateTime.of(currentDate, endTime);
                            if (booking.getTable().getTableNumber() == table.getTableNumber() &&
                                    !(startDateTime.isAfter(booking.getEndTime()) || startDateTime.isEqual(booking.getEndTime())) &&
                                    !(endDateTime.isBefore(booking.getStartTime())||endDateTime.isEqual(booking.getStartTime()))) {
                                isAvailable = false; // Table is not available
                                break;
                            }
                        }
                        if (!isAvailable) {
                            break; // Break the loop if the table is not available for the current day
                        }
                    }
                    currentDate = currentDate.plusDays(1); // Move to the next day
                }
                if (isAvailable) {
                    availableTables.add(table); // Add available table to the list
                }
            }
        }
        return availableTables;
    }        
    // Make a reservation
    public static boolean makeReservation(Table table, LocalDateTime startTime, LocalDateTime endTime, int cusID) {
        LocalDate currentDate = startTime.toLocalDate();
        boolean ex = false;
        while(!currentDate.isAfter(endTime.toLocalDate())){
            LocalDateTime startDateTime = LocalDateTime.of(currentDate,startTime.toLocalTime());
            LocalDateTime endDateTime = LocalDateTime.of(currentDate,endTime.toLocalTime());
            if (isTableAvailable(table,startDateTime,endDateTime)) {
                try {
                    String sql = "INSERT INTO booking (table_number, bookingDate, start_time, end_time, cus_Id,paymentStatus) VALUES (?, ?, ?, ?, ?, ?)";

                    Connection conn = Connect.getConnection(); // Assuming Connect.getConnection() returns a valid JDBC connection
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1,table.getTableNumber());
                    pstmt.setString(2, currentDate.toString());
                    pstmt.setString(3, startTime.toLocalTime().toString());
                    pstmt.setString(4, endTime.toLocalTime().toString());
                    pstmt.setString(5, String.valueOf(cusID));
                    pstmt.setString(6, "Pending");
                    int res = pstmt.executeUpdate();
                    if (res > 0)
                            ex =  true;
                } catch (SQLException e) {
                    System.out.println("Error inserting row: " + e.getMessage());
                }
            }else{
                ex = false;
            }
            currentDate = currentDate.plusDays(1);
        }

        return ex;
    }
    
    public static boolean updatePaymentStatus(int bookingID, String paymentStatus) {
        try {
            String sql = "UPDATE booking SET paymentStatus = ? WHERE bid = ?";

            Connection conn = Connect.getConnection(); // Assuming Connect.getConnection() returns a valid JDBC connection
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, paymentStatus);
            pstmt.setInt(2, bookingID);

            int res = pstmt.executeUpdate();
            if (res > 0) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Error updating payment status: " + ex.getMessage());
        }
        return false;
    }
 
    // Make a reservation for specific days
    public static boolean makeReservation(Table table,LocalDate startDate, LocalDate endDate,LocalTime startTime, LocalTime endTime,DayOfWeek[] daysOfWeek, int cusID) {
        LocalDate currentDate = startDate;
        boolean res = false;
        while (!currentDate.isAfter(endDate)) {
            if (isDayOfWeekInArray(currentDate.getDayOfWeek(), daysOfWeek)) {
                LocalDateTime startDateTime = LocalDateTime.of(currentDate, startTime);
                LocalDateTime endDateTime = LocalDateTime.of(currentDate, endTime);
                
                res = makeReservation(table, startDateTime, endDateTime,cusID);
                
                if(!res)
                    break;
            }
            currentDate = currentDate.plusDays(1); // Move to the next day
        }
        return res;
    }

    // Helper method to check if a day of week exists in the array
    private static boolean isDayOfWeekInArray(DayOfWeek dayOfWeek, DayOfWeek[] daysOfWeek) {
        for (DayOfWeek day : daysOfWeek) {
            if (day == dayOfWeek) {
                return true;
            }
        }
        return false;
    }
 
    // Retrieve all bookings from the booking table and return a list of Booking objects
    public static ArrayList<Booking> getAllBookings() {
        ArrayList<Booking> bookings = new ArrayList<>();
        
        String sql = "SELECT * FROM booking";
        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                // Retrieve data from the result set
                int bid = rs.getInt("bid");
                int tableNumber = rs.getInt("table_number");
                Table table = TableController.getTable(tableNumber);
                LocalDate bDate = LocalDate.parse(rs.getString("bookingDate"));
                LocalTime startTime = LocalTime.parse(rs.getString("start_time"));
                LocalTime endTime = LocalTime.parse(rs.getString("end_time"));
                String cusId = rs.getString("cus_Id");
                String paymentStatus = rs.getString("paymentStatus");

                    // Create LocalDateTime objects
                LocalDateTime startDateTime = LocalDateTime.of(bDate, startTime);
                LocalDateTime endDateTime = LocalDateTime.of(bDate, endTime);
                                

                // Create Booking object and add to the list
                Booking booking = new Booking(bid, table, startDateTime, endDateTime, cusId, paymentStatus) ;
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return bookings;
    }
    
    public static ArrayList<Booking> getAllCustomerBookings(int customerId) {
        ArrayList<Booking> bookings = new ArrayList<>();
        
        String sql = "SELECT * FROM booking WHERE cus_Id = ?";
        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set the parameter
            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Retrieve data from the result set
                    int bid = rs.getInt("bid");
                    int tableNumber = rs.getInt("table_number");
                    Table table = TableController.getTable(tableNumber);
                    LocalDate bDate = LocalDate.parse(rs.getString("bookingDate"));
                    LocalTime startTime = LocalTime.parse(rs.getString("start_time"));
                    LocalTime endTime = LocalTime.parse(rs.getString("end_time"));
                    String cusId = rs.getString("cus_Id");
                    String paymentStatus = rs.getString("paymentStatus");

                    // Create LocalDateTime objects
                    LocalDateTime startDateTime = LocalDateTime.of(bDate, startTime);
                    LocalDateTime endDateTime = LocalDateTime.of(bDate, endTime);

                    // Create Booking object and add to the list
                    Booking booking = new Booking(bid, table, startDateTime, endDateTime, cusId, paymentStatus) ;
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return bookings;
    }
    
    public static Booking getBookingById(int bid) {
        String sql = "SELECT * FROM booking WHERE bid = ?";
        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set the parameter
            pstmt.setInt(1, bid);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Retrieve data from the result set
                    int tableNumber = rs.getInt("table_number");
                    Table table = TableController.getTable(tableNumber);
                    LocalDate bDate = LocalDate.parse(rs.getString("bookingDate"));
                    LocalTime startTime = LocalTime.parse(rs.getString("start_time"));
                    LocalTime endTime = LocalTime.parse(rs.getString("end_time"));
                    String cusId = rs.getString("cus_Id");
                    String paymentStatus = rs.getString("paymentStatus");

                    // Create LocalDateTime objects
                    LocalDateTime startDateTime = LocalDateTime.of(bDate, startTime);
                    LocalDateTime endDateTime = LocalDateTime.of(bDate, endTime);

                    // Create Booking object and return it
                    return new Booking(bid, table, startDateTime, endDateTime, cusId, paymentStatus);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null; // Return null if no booking is found
    }

    public static boolean cancelReservation(int bookingID) {
        try {
            String sql = "DELETE FROM booking WHERE bid = ?";

            Connection conn = Connect.getConnection(); // Assuming Connect.getConnection() returns a valid JDBC connection
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookingID);

            int res = pstmt.executeUpdate();
            if (res > 0)
                return true;
        } catch (SQLException ex) {
            System.out.println("Error deleting row: " + ex.getMessage());
        }
        return false;
    }
   
}
