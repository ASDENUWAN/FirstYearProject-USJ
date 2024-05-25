/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;

/**
 *
 * @author Sachii
 */
public class Booking {
    private int bid;
    private Table table;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String  cusId;
    private String pstatus;

    // Constructor
    public Booking(Table table, LocalDateTime startTime, LocalDateTime endTime, String cusId) {
        this.table = table;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cusId = cusId;
        this.pstatus = "Pending";
    }

    public Booking(int bid, Table table, LocalDateTime startTime, LocalDateTime endTime, String cusId,String pstatus) {
        this.bid = bid;
        this.table = table;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cusId = cusId;
        this.pstatus = pstatus;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getPstatus() {
        return pstatus;
    }

    public void setPstatus(String pstatus) {
        this.pstatus = pstatus;
    }

    @Override
    public String toString() {
        return this.bid+" "+this.table.getTableNumber()+" "+this.startTime+" "+this.endTime; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    
    
    
}
