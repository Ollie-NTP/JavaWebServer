/*
    Document   : eBooksStoreAdmineBooksServlet.java
    Author     : gheorgheaurelpacurar   
    Copyright  : gheorgheaurelpacurar
 */
package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author gheor
 */
public class eBooksStoreAdminEBooks extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        String user = "olimpiu";
        String password = "olimpiu";
        String url = "jdbc:derby://localhost:1527/EBOOKS;create=true;";
        String driver = "org.apache.derby.jdbc.ClientDriver";

        // INSERT SECTION //
        // check push on Insert button
        if (request.getParameter("admin_ebooks_insert") != null) { // insert values from fields
            // set connection paramters to the DB
            // read values from page fields
            Map<BooksKey, String> boundBook = getInsertParameters(request);

            // declare specific DBMS operationsvariables
            Connection connection = null;
            PreparedStatement pstmnt = null;

            try {

                //check driver and create connection
                Class driverClass = Class.forName(driver);
                connection = DriverManager.getConnection(url, user, password);
                String DML = "INSERT INTO EBOOKS.EBOOKS VALUES (?, ?, ?, ?, ?, ?, ?)";
                pstmnt = connection.prepareStatement(DML);

                // PREPARE STATEMENT
                pstmnt.setString(1, boundBook.get(BooksKey.ISBN));
                pstmnt.setString(2, boundBook.get(BooksKey.DENUMIRE));
                pstmnt.setInt(3, cleanStringID(boundBook.get(BooksKey.ID_TYPE)));
                pstmnt.setInt(4, cleanStringID(boundBook.get(BooksKey.ID_QUALITY)));
                pstmnt.setInt(5, Integer.parseInt(boundBook.get(BooksKey.PAGES)));
                pstmnt.setInt(6, cleanStringID(boundBook.get(BooksKey.ID_GENRE)));
                pstmnt.setDouble(7, Double.parseDouble(boundBook.get(BooksKey.PRET)));
                pstmnt.execute();

            } catch (ClassNotFoundException | SQLException ex) {
                // display a message for NOT OK
                Logger.getLogger(eBooksStoreAdminUsersServlet.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closeActivity(null, pstmnt, connection);
                request.getRequestDispatcher("./eBooksStoreAdminEBooks.jsp").forward(request, response);
            }
        } 
        // UPDATE SECTION
        
        // check push on Update button
        else if (request.getParameter("admin_ebooks_update") != null) {

            // declare specific variables
            PreparedStatement pstmnt = null;
            Connection connection = null;

            try {

                //check driver and create connection
                Class driverClass = Class.forName(driver);
                connection = DriverManager.getConnection(url, user, password);

                // identify selected checkbox
                String[] selectedCheckboxes = request.getParameterValues("admin_ebooks_checkbox");

                String checkedIsbn = selectedCheckboxes[0];

                updateSelectedCheckBox(request, connection, pstmnt, checkedIsbn);

            } catch (ClassNotFoundException | SQLException ex) {
                // display a message for NOT OK
                Logger.getLogger(eBooksStoreAdminUsersServlet.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closeActivity(null, pstmnt, connection);
                request.getRequestDispatcher("./eBooksStoreAdminEBooks.jsp").forward(request, response);
            }
        } 
        //  DELETE SECTION
        
        // check push on Delete button
        else if (request.getParameter("admin_ebooks_delete") != null) { // delete 
            // declare specific variables
            PreparedStatement pstmnt = null;
            Connection connection = null;

            try {
                //check driver and create connection
                Class driverClass = Class.forName(driver);
                connection = DriverManager.getConnection(url, user, password);

                // identify selected checkbox and for each execute the delete operation
                String[] selectedCheckboxes = request.getParameterValues("admin_ebooks_checkbox");

                // more critical DB operations should be made into a transaction
                connection.setAutoCommit(false);

                for (String isbn : selectedCheckboxes) {

                    // realize delete of all selected rows
                    String DML = "DELETE FROM EBOOKS.EBOOKS WHERE isbn=?";
                    pstmnt = connection.prepareStatement(DML);
                    pstmnt.setString(1, isbn);

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
                closeActivity(null, pstmnt, connection);
                request.getRequestDispatcher("./eBooksStoreAdminEBooks.jsp").forward(request, response);
            }
        } // check push on Cancel button
        else if (request.getParameter("admin_ebooks_cancel") != null) { // cancel
            request.getRequestDispatcher("./eBooksStoreMainPage.jsp").forward(request, response);
        }
    }

    //  UPDATE SELECTED CHECKBOX SECTION
    
    private void updateSelectedCheckBox(HttpServletRequest request, Connection connection, PreparedStatement pstmnt, String selectedRowISBN)
            throws SQLException {

        String denumire = null;
        int id_type = 0;
        int id_quality = 0;
        int pages = 0;
        int id_genre = 0;
        int pret = 0;

        String sql = "SELECT * FROM EBOOKS.EBOOKS WHERE isbn='" + selectedRowISBN + "'";

        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery(sql);

        if (results.next()) {

            denumire = results.getString("denumire");
            id_type = results.getInt("id_type");
            id_quality = results.getInt("id_quality");
            pages = results.getInt("pages");
            id_genre = results.getInt("id_genre");
            pret = results.getInt("pret");

        }

        Ebook ebook = new Ebook(selectedRowISBN, denumire, id_type, id_quality, pages, id_genre, pret);

        Map<BooksKey, String> parameterMap = getUpdateParameters(request, ebook);

        String DML = "UPDATE EBOOKS.EBOOKS "
                + "SET isbn=?, "
                + "denumire=?, "
                + "id_type=?, "
                + "id_quality=?, "
                + "pages=?, "
                + "id_genre=?, "
                + "pret=? "
                + "WHERE isbn=?";

        pstmnt = connection.prepareStatement(DML);
        pstmnt.setString(1, parameterMap.get(BooksKey.ISBN));
        pstmnt.setString(2, parameterMap.get(BooksKey.DENUMIRE));
        pstmnt.setInt(3, cleanStringID(parameterMap.get(BooksKey.ID_TYPE)));
        pstmnt.setInt(4, cleanStringID(parameterMap.get(BooksKey.ID_QUALITY)));
        pstmnt.setInt(5, Integer.parseInt(parameterMap.get(BooksKey.PAGES)));
        pstmnt.setInt(6, cleanStringID(parameterMap.get(BooksKey.ID_GENRE)));
        pstmnt.setDouble(7, Double.parseDouble(parameterMap.get(BooksKey.PRET)));
        pstmnt.setString(8, selectedRowISBN); // HERE, made update, ISBN checked
        pstmnt.execute();

        closeActivity(results, null, null);
    }

    //  CLOSE RESOURCES SECTION
    
    private void closeActivity(ResultSet resultSet, Statement stmt, Connection connection) {
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

    private int cleanStringID(String string) {
        return Integer.parseInt(string);
    }

        //  GET INSERT PARAMETERS 
    
    private Map<BooksKey, String> getInsertParameters(HttpServletRequest request) {

        Map<BooksKey, String> parameterMap = new HashMap<>();

        String isbn = request.getParameter("admin_ebooks_isbn");
        String denumire = request.getParameter("admin_ebooks_denumire");
        String pret = request.getParameter("admin_ebooks_price");
        String pages = request.getParameter("admin_ebooks_pages");
        String id_type = request.getParameter("admin_ebooks_id_type");
        String id_genre = request.getParameter("admin_ebooks_id_genres");
        String id_quality = request.getParameter("admin_ebooks_id_paper_qualities");

        parameterMap.put(BooksKey.ISBN,
                ((isbn == null || isbn.trim().equals("")) ? "" : isbn));

        parameterMap.put(BooksKey.DENUMIRE,
                ((denumire == null || denumire.trim().equals("")) ? "" : denumire));

        parameterMap.put(BooksKey.PRET,
                (pret == null || pret.trim().equals("")) ? "0" : pret);

        parameterMap.put(BooksKey.PAGES,
                (pages == null || pages.trim().equals("")) ? "0" : pages);

        parameterMap.put(BooksKey.ID_TYPE,
                id_type);

        parameterMap.put(BooksKey.ID_GENRE,
                id_genre);

        parameterMap.put(BooksKey.ID_QUALITY,
                id_quality);

        return parameterMap;
    }

    //  GET UPDATE PARAMETERS 
    private Map<BooksKey, String> getUpdateParameters(HttpServletRequest request, Ebook ebook) {

        Map<BooksKey, String> parameterMap = new HashMap<>();

        String isbnParam = request.getParameter("admin_ebooks_isbn");
        String denumireParam = request.getParameter("admin_ebooks_denumire");
        String pretParam = request.getParameter("admin_ebooks_price");
        String pagesParam = request.getParameter("admin_ebooks_pages");
        String id_typeParam = request.getParameter("admin_ebooks_id_type");
        String id_genreParam = request.getParameter("admin_ebooks_id_genres");
        String id_qualityParam = request.getParameter("admin_ebooks_id_paper_qualities");

        parameterMap.put(BooksKey.ISBN,
                ((isbnParam == null || isbnParam.trim().equals("")) ? ebook.getIsbn() : isbnParam));

        parameterMap.put(BooksKey.DENUMIRE,
                ((denumireParam == null || denumireParam.trim().equals("")) ? ebook.getDenumire() : denumireParam));

        parameterMap.put(BooksKey.PRET,
                (pretParam == null || pretParam.trim().equals("")) ? Integer.toString(ebook.getPret()) : pretParam);

        parameterMap.put(BooksKey.PAGES,
                (pagesParam == null || pagesParam.trim().equals("")) ? Integer.toString(ebook.getPages()) : pagesParam);

        parameterMap.put(BooksKey.ID_TYPE,
                id_typeParam);

        parameterMap.put(BooksKey.ID_GENRE,
                id_genreParam);

        parameterMap.put(BooksKey.ID_QUALITY,
                id_qualityParam);

        return parameterMap;
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet serves eBooksSoreAdminEBooks.JSP page";
    }// </editor-fold>
}
