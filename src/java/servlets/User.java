/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

/**
 *
 * @author Cip
 */
public class User {
    
    private String ssn;
    private String name;
    private String password;
    private String role;

    public User(String ssn, String name, String password, String role) {
        this.ssn = ssn;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public String getSsn() {
        return ssn;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
    
    
    
}
