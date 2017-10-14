/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taghandlers;


import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import servlets.eBooksStoreAdminUsersServlet;


/**
 *
 * @author mihai
 */
public class Users extends SimpleTagSupport {

    private String SSN;
    private String NAME;
    private String PASSWORD;
    private String ROLE;

    /**
     * Called by the container to invoke this tag. The implementation of this
     * method is provided by the tag library developer, and handles all tag
     * processing, body iteration, etc.
     */
    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
        
        try {
                ResultSet resultSet = null;
                Statement statement = null;
                PreparedStatement pstmnt = null;
                Connection connection = null;
                String user = "olimpiu" ;
                String password = "olimpiu";
                String url = "jdbc:derby://localhost:1527/EBOOKS;create=true";
                String driver = "org.apache.derby.jdbc.ClientDriver"; 
                try
                { 
                    //check driver and create connection
                    Class driverClass = Class.forName(driver);
                    connection = DriverManager.getConnection(url, user, password);
                  
                    if(!(("".equals(NAME)) && ("".equals(PASSWORD)))){
  
                            if("".equals(NAME) && !("".equals(SSN))){ // only password/s should be updated
                                String DML = "UPDATE EBOOKS.USERS SET password=?,role=?,SSN=? WHERE SSN=?";
                                pstmnt = connection.prepareStatement(DML);
                                pstmnt.setString(1, PASSWORD);
                                pstmnt.setString(2, ROLE);
                                pstmnt.setString(3, SSN);
                                pstmnt.setString(4, SSN);
                            }
                            else if("".equals(PASSWORD)){// only username should be updated
                                String DML = "UPDATE EBOOKS.USERS SET name=?,role=? WHERE SSN=?";
                                pstmnt = connection.prepareStatement(DML);
                                pstmnt.setString(1, NAME);
                                pstmnt.setString(2, ROLE);
                                pstmnt.setString(3, SSN);
                            }else{
                                String DML = "UPDATE EBOOKS.USERS SET SSN=?,name=?,password=?,role=? WHERE SSN=?";
                                pstmnt = connection.prepareStatement(DML);
                                pstmnt.setString(1, SSN);
                                pstmnt.setString(2, NAME);
                                pstmnt.setString(3, PASSWORD);
                                pstmnt.setString(4, ROLE);
                                pstmnt.setString(5, SSN);
                            } 
                            
                            boolean execute = pstmnt.execute();
                        
                    }else{ // update one or more roles for one or more users

                            String DML = "UPDATE EBOOKS.USERS SET role=?,SSN=? WHERE SSN=?";
                            pstmnt = connection.prepareStatement(DML);
                            pstmnt.setString(1, ROLE);
                            pstmnt.setString(2, SSN);
                            boolean execute = pstmnt.execute();
          
                    }
                }
                catch (ClassNotFoundException | SQLException ex)
                {
                    // display a message for NOT OK
                    Logger.getLogger(eBooksStoreAdminUsersServlet.class.getName()).log(Level.SEVERE, null, ex);

                }
                finally
                {
                    if (resultSet != null)
                    {
                        try
                        {
                            resultSet.close();
                        }
                        catch (SQLException ex){Logger.getLogger(eBooksStoreAdminUsersServlet.class.getName()).log(Level.SEVERE, null, ex);}
                    }
                    if (pstmnt != null)
                    {
                        try
                        {
                            pstmnt.close();
                        }
                        catch (SQLException ex){Logger.getLogger(eBooksStoreAdminUsersServlet.class.getName()).log(Level.SEVERE, null, ex);}
                    }	
                    if (connection != null)
                    {
                        try
                        {
                            connection.close();
                        }
                        catch (SQLException ex){Logger.getLogger(eBooksStoreAdminUsersServlet.class.getName()).log(Level.SEVERE, null, ex);}
                    }
                    // redirect page to its JSP as view
                    // request.getRequestDispatcher("./eBooksStoreAdminUsersPage.jsp").forward(request, response);
                }

            JspFragment f = getJspBody();
            if (f != null) {
                f.invoke(out);
            }

            // TODO: insert code to write html after writing the body content.
            // e.g.:
            //
            // out.println("    </blockquote>");
        } catch (java.io.IOException ex) {
            throw new JspException("Error in Users tag", ex);
        }
    }

    public void setSSN(String SSN) {
        this.SSN = SSN;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }
    
     public void setROLE(String ROLE) {
        this.ROLE = ROLE;
    }
    
}
