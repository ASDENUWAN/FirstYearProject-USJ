/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Sachii
 */
public class Customer {
    private int cId;
    private String name;
    private String email;
    private String contact;
    private String username;
    private String password;

    public Customer() {
        this(0,"","","","","");
    }
    
    
    public Customer(String name, String email, String contact, String username, String password) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.username = username;
        this.password = password;
    }

    public Customer(int cId, String name, String email, String contact, String username) {
        this.cId = cId;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.username = username;
    }

    public Customer(int cId, String name, String email, String contact, String username, String password) {
        this.cId = cId;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.username = username;
        this.password = password;
    }
    
    // Getters and setters
    public String getUsername() {
        return username;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
