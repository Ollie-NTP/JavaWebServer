/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taghandlers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import servlets.eBooksStoreAdminUsersServlet;

/**
 *
 * @author mihai
 */
public class InsertUser extends SimpleTagSupport {

    private String CNP;
    private String username;
    private String password;
    private String role;

    /**
     * Called by the container to invoke this tag. The implementation of this
     * method is provided by the tag library developer, and handles all tag
     * processing, body iteration, etc.
     */
    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();

        String dbUser = "olimpiu";
        String dbPassword = "olimpiu";
        String url = "jdbc:derby://localhost:1527/EBOOKS;create=true;";
        String driver = "org.apache.derby.jdbc.ClientDriver";

        PreparedStatement pstmnt = null;
        Connection connection = null;

        try {

            Class driverClass = Class.forName(driver);
            connection = DriverManager.getConnection(url, dbUser, dbPassword);

            String DML = "INSERT INTO EBOOKS.USERS VALUES (?, ?, ?, ?)";

            pstmnt = connection.prepareStatement(DML);

            pstmnt.setString(1, CNP);
            pstmnt.setString(2, username);
            pstmnt.setString(3, password);
            pstmnt.setString(4, role);

            pstmnt.execute();

        } catch (ClassNotFoundException | SQLException ex) {
            out.println("Exception in INSERT:" + ex.getMessage());
        } finally {
            close(null, pstmnt, connection);
        }

        JspFragment f = getJspBody();

        if (f != null) {
            try {
                f.invoke(out);
            } catch (IOException ex) {
                throw new JspException("Error in InsertUser tag", ex);
            }
        }

    }

    // Close resultset.
    
    private void close(ResultSet resultSet, Statement stmt, Connection connection) {
        if (resultSet != null) {
            try {
                resultSet.close();

            } catch (SQLException ex) {
                Logger.getLogger(eBooksStoreAdminUsersServlet.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (stmt != null) {
            try {
                stmt.close();

            } catch (SQLException ex) {
                Logger.getLogger(eBooksStoreAdminUsersServlet.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (connection != null) {
            try {
                connection.setAutoCommit(true);

            } catch (SQLException ex) {
                Logger.getLogger(eBooksStoreAdminUsersServlet.class
                        .getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    connection.close();

                } catch (SQLException ex) {
                    Logger.getLogger(eBooksStoreAdminUsersServlet.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    //  Setters
    
    public void setCNP(String CNP) {
        this.CNP = CNP;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String id_role) {
        this.role = id_role;
    }

}
