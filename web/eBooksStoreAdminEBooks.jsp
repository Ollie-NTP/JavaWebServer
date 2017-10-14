<%-- 
    Document   : eBooksStoreAdminEBooks
    Created on : Nov 19, 2016, 7:36:42 PM
    Author     : gheor
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>eBooksStore Management </title>
        <link rel="stylesheet" type="text/css" href=".\\css\\ebookstore.css">
    </head>
    <body>
        <h1>Manage the books from Electronic Books Store</h1>
        <form style = "margin-top: -20px" action="${pageContext.request.contextPath}/eBooksStoreAdminEBooks">
            <%-- test if actual user is authenticated and authorized --%>
            <c:choose>
                <c:when test="${(actualUserRole == 'admin')}">   
                    <!-- include menu -->
                    <%@ include file="./utils/eBooksStoreMenu.jsp" %>
                    <%-- Master view --%>
                    
                        <sql:setDataSource 
                            var="snapshot" 
                            driver="org.apache.derby.jdbc.ClientDriver40"
                            url="jdbc:derby://localhost:1527/EBOOKS;create=true;"
                            user="olimpiu"  
                            password="olimpiu"/>
                        <sql:query dataSource="${snapshot}" var="result">
                            SELECT ISBN, DENUMIRE, TYPE, QUALITY, GENRE, PAGES, PRET 
                            FROM EBOOKS.EBOOKS, EBOOKS.BOOK_TYPES, EBOOKS.BOOK_PAPER_QUALITIES, EBOOKS.BOOK_GENRES
                            WHERE ID_TYPE = EBOOKS.BOOK_TYPES.ID 
                              AND ID_QUALITY = EBOOKS.BOOK_PAPER_QUALITIES.ID
                              AND ID_GENRE = EBOOKS.BOOK_GENRES.ID
                            ORDER BY QUALITY ASC
                        </sql:query>
              
                        <table style = "margin-top: -30px" border="2" width="100%">
                            <tr>
                                <td width="3%" class="thc"> Select </td>   
                                <td width="12%" class="thc">ISBN</td>  
                                <td width="12%" class="thc">DENUMIRE</td>
                                <td width="8%" class="thc">TYPE</td>
                                <td width="8%" class="thc">QUALITY</td>
                                <td width="8%" class="thc">GENRE</td>
                                <td width="12%" class="thc">PAGES</td>
                                <td width="13%" class="thc">PRET</td>
                        </table>   
                <div id="table-wrapper">
                    <div id="table-scroll">
                        <table border="1" width="100%">    
                            </tr>
                            <c:forEach var="row" varStatus="loop" items="${result.rows}">
                                <tr>
                                    <td width="3%" class="tdc"><input type="checkbox" name="admin_ebooks_checkbox" value="${row.isbn}"></td>
                                    <td width="12%" class="tdc"><c:out value="${row.isbn}"/></td>
                                    <td width="12%" class="tdc"><c:out value="${row.denumire}"/></td>
                                    <td width="8%" class="tdc"><c:out value="${row.type}"/></td>
                                    <td width="8%" class="tdc"><c:out value="${row.quality}"/></td>
                                    <td width="8%" class="tdc"><c:out value="${row.genre}"/></td>
                                    <td width="12%" class="tdc"><c:out value="${row.pages}"/></td>
                                    <td width="12%" class="tdc"><c:out value="${row.pret } lei"/></td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                </div>
                        <%-- Details --%>
                        <sql:setDataSource 
                            var="snapshotgenres" 
                            driver="org.apache.derby.jdbc.ClientDriver40"
                            url="jdbc:derby://localhost:1527/EBOOKS;create=true;"
                            user="olimpiu"  
                            password="olimpiu"/>
                        <sql:query dataSource="${snapshotgenres}" var="resultgenres">
                            SELECT ID, GENRE FROM EBOOKS.BOOK_GENRES 
                        </sql:query>
                        <sql:setDataSource 
                            var="snapshotpaperqualities" 
                            driver="org.apache.derby.jdbc.ClientDriver40"
                            url="jdbc:derby://localhost:1527/EBOOKS;create=true;"
                            user="olimpiu"  
                            password="olimpiu"/>
                        <sql:query dataSource="${snapshotpaperqualities}" var="resultpaperqualities">
                            SELECT ID, QUALITY FROM EBOOKS.BOOK_PAPER_QUALITIES 
                        </sql:query>    
                        <sql:setDataSource 
                            var="snapshottypes" 
                            driver="org.apache.derby.jdbc.ClientDriver40"
                            url="jdbc:derby://localhost:1527/EBOOKS;create=true;"
                            user="olimpiu"  
                            password="olimpiu"/>
                        <sql:query dataSource="${snapshottypes}" var="resulttypes">
                            SELECT ID, TYPE FROM EBOOKS.BOOK_TYPES 
                        </sql:query>    
                        <table style = "margin-top: 10px" class="tablecenterdwithborder">
                            <tr>
                                <td>    
                                    <table>
                                        <tr>
                                            <td> ISBN: </td>
                                            <td> <input style = "min-width: 150px;" type="text" name="admin_ebooks_isbn"></input></td>
                                        </tr>                                        
                                        <tr>
                                            <td> DENUMIRE: </td>
                                            <td> <input style = "min-width: 150px;" type="text" name="admin_ebooks_denumire"></input></td>
                                        </tr>
                                        <tr>
                                            <td> PAGES NO: </td>
                                            <td> <input style = "min-width: 150px;" type="text" name="admin_ebooks_pages"></input></td>
                                        </tr>
                                        <tr>
                                            <td> PRICE: </td>
                                            <td> <input style = "min-width: 150px;" type="text" name="admin_ebooks_price"></input></td>
                                        </tr>
                                        <tr>
                                            <td> ID_TYPE: </td>
                                            <td>
                                                <select name="admin_ebooks_id_type" required="true">
                                                    <c:forEach var="rowtype" items="${resulttypes.rows}">    
                                                        <option name="admin_ebooks_types" value="${rowtype.ID}">${rowtype.TYPE} (${rowtype.ID})</option>
                                                    </c:forEach>
                                                </select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td> ID_PAPER_QUALITY: </td>
                                            <td>
                                                <select name="admin_ebooks_id_paper_qualities" required="true">
                                                    <c:forEach var="rowquality" items="${resultpaperqualities.rows}">    
                                                        <option name="admin_ebooks_paper_qualities" value="${rowquality.ID}">${rowquality.quality} (${rowquality.ID})</option>
                                                    </c:forEach>
                                                </select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td> ID_GENRE: </td>
                                            <td>
                                                <select name="admin_ebooks_id_genres" required="true">
                                                    <c:forEach var="rowgenre" items="${resultgenres.rows}">    
                                                        <option name="admin_ebooks_genres" value="${rowgenre.ID}">${rowgenre.genre} (${rowgenre.ID})</option>
                                                    </c:forEach>
                                                </select>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <%-- buttons --%>
                                    <table>

                                        <tr>
                                            <td style = "min-width: 80px;" class="tdc"><input type="submit" class="ebooksstorebutton" name="admin_ebooks_insert" value="Insert"></td> 
                                            <td style = "min-width: 80px;" class="tdc"><input type="submit" class="ebooksstorebutton" name="admin_ebooks_update" value="Update"></td>
                                            <td style = "min-width: 80px;" class="tdc"><input type="submit" class="ebooksstorebutton" name="admin_ebooks_delete" value="Delete"></td> 
                                            <td style = "min-width: 80px;" class="tdc"><input type="submit" class="ebooksstorebutton" name="admin_ebooks_cancel" value="Cancel"></td>
                                        </tr>  
                                    </table>
                                </td>
                            </tr>
                        </table>    
                    </form>
                </c:when>
                <c:otherwise>
                    <c:redirect url="./Index.jsp"></c:redirect>
                </c:otherwise>
            </c:choose>
        </form>
    </body>
</html>

