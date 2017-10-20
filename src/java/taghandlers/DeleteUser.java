/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taghandlers;

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
public class DeleteUser extends SimpleTagSupport {


    private String[] selectedCheckboxes;

    /**
     * Called by the container to invoke this tag. The implementation of this
     * method is provided by the tag library developer, and handles all tag
     * processing, body iteration, etc.
     */
    @Override
    public void doTag() throws JspException {
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

            // more critical DB operations should be made into a transaction
            connection.setAutoCommit(false);

            for (String ssn : selectedCheckboxes) {
                // realize delete of all selected rows
                String DML = "DELETE FROM EBOOKS.USERS WHERE SSN=?";
                pstmnt = connection.prepareStatement(DML);
                pstmnt.setString(1, ssn);
                pstmnt.execute();
            }

            connection.commit();
            connection.setAutoCommit(true);

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(eBooksStoreAdminUsersServlet.class.getName()).log(Level.SEVERE, null, ex);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    Logger.getLogger(eBooksStoreAdminUsersServlet.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        } finally {
            close(null, pstmnt, connection);
        }

        try {

            JspFragment f = getJspBody();
            if (f != null) {
                f.invoke(out);
            }

        } catch (java.io.IOException ex) {
            throw new JspException("Error in DeleteUser tag", ex);
        }
    }

    // Delete from selected check boxes.

    public void setSelectedCheckboxes(String[] selectedCheckboxes) {
        this.selectedCheckboxes = selectedCheckboxes;
    }

    //  Sql close statement 
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
}
