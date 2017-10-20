<%-- 
    Document   : eBooksStoreAdminUsersPage
    Created on : Oct 6, 2017, 8:35:27 AM
    Author     : gheor
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>

<%@ taglib prefix="stiu" uri="WEB-INF/tlds/usersutil.tld"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Electronic Books Store Manage Users</title>
        <link rel="stylesheet" type="text/css" href=".\\css\\ebookstore.css">
    </head>

    <%
        if (request.getParameter("admin_users_insert") != null) {
    %>
    <stiu:InsertUser 
    CNP="<%= request.getParameter("admin_users_ssn")%>" 
    username="<%= request.getParameter("admin_users_username")%>" 
    password="<%= request.getParameter("admin_users_password")%>" 
    role="<%= request.getParameter("admin_user_role")%>">
    </stiu:InsertUser> 

    <%
    } else if (request.getParameter("admin_users_update") != null) {
    %>
    <stiu:UpdateUser 
    CNP="<%= request.getParameter("admin_users_ssn")%>" 
    username="<%= request.getParameter("admin_users_username")%>" 
    password="<%= request.getParameter("admin_users_password")%>" 
    role="<%= request.getParameter("admin_user_role")%>"
    selectedCheckboxes="<%= request.getParameterValues("admin_users_checkbox")%>">
    </stiu:UpdateUser> 
    <%
    } else if (request.getParameter("admin_users_delete") != null) {
    %>
    <stiu:DeleteUser
    selectedCheckboxes="<%= request.getParameterValues("admin_users_checkbox")%>">
    </stiu:DeleteUser> 
    <%
    } else if (request.getParameter("admin_users_cancel") != null) {
    %>
    
        <jsp:forward page="./eBooksStoreMainPage.jsp"/> 
    
    <%
        }
    %>



    <body>
        <%-- test if actual user is authenticated and authorized --%>
        <c:choose>
            <c:when test="${(actualUserRole == 'admin')}">   
                <!-- include menu -->
                <%@ include file="./utils/eBooksStoreMenu.jsp" %>
                <%-- Master view --%>

                <form action="${pageContext.request.contextPath}/eBooksStoreAdminUsersPage.jsp" method="POST">
                    <sql:setDataSource 
                        var="snapshot" 
                        driver="org.apache.derby.jdbc.ClientDriver40"
                        url="jdbc:derby://localhost:1527/EBOOKS;create=true;"
                        user="olimpiu"  
                        password="olimpiu"/>
                    <sql:query dataSource="${snapshot}" var="result">
                        SELECT EBOOKS.USERS.SSN, EBOOKS.USERS."NAME", EBOOKS.USERS.PASSWORD, EBOOKS.USERS."ROLE" 
                        FROM EBOOKS.USERS, EBOOKS.ROLES 
                        WHERE EBOOKS.USERS."ROLE" = EBOOKS.ROLES."ROLE" ORDER BY NAME, ROLE ASC 
                    </sql:query>
                    <table border="1" width="100%">
                        <tr>
                            <td width="3.95%" class="thc"> Select </td>   
                            <td width="13.8%" class="thc"> SSN </td>  
                            <td width="13.8%" class="thc">NAME</td>
                            <td width="13.8%" class="thc">PASSWORD</td>
                            <td width="14.5%" class="thc">ROLE</td>
                    </table>   
                    <div style="overflow-y: scroll; height:180px;">
                        <table border="1" width="100%">    
                            </tr>
                            <c:forEach var="row" varStatus="loop" items="${result.rows}">
                                <tr>
                                    <td width="4%" class="tdc"><input type="checkbox" name="admin_users_checkbox" value="${row.ssn}"></td>
                                    <td width="14%" class="tdc"><c:out value="${row.ssn}"/></td>
                                    <td width="14%" class="tdc"><c:out value="${row.name}"/></td>
                                    <td width="14%" class="tdc"><c:out value="${row.password}"/></td>
                                    <td width="14%" class="tdc"><c:out value="${row.role}"/></td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                    <hr>
                    <%-- Details --%>

                    <sql:query dataSource="${snapshot}" var="resultroles">
                        SELECT ROLE from EBOOKS.ROLES ORDER BY ROLE ASC 
                    </sql:query>
                    <table class="tablecenterdwithborder">
                        <tr>
                            <td>    
                                <table>
                                    <tr>
                                        <td> SSN: </td>
                                        <td> <input style = "min-width: 140px;" type="text" name="admin_users_ssn"></input></td>
                                    </tr>                                        
                                    <tr>
                                        <td> NAME: </td>
                                        <td> <input style = "min-width: 140px;" type="text" name="admin_users_username"></input></td>
                                    </tr>
                                    <tr>
                                        <td> PASSWORD: </td>
                                        <td> <input style = "min-width: 140px;" type="text" name="admin_users_password"></input></td>
                                    </tr>
                                    <tr>
                                        <td> ROLE: </td>
                                        <td>
                                            <select style = "min-width: 140px;" name="admin_user_role" required="true">
                                                <c:forEach var="rowrole" items="${resultroles.rows}">    
                                                    <option name="admin_users_roles" value="${rowrole.role}">${rowrole.role}</option>
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

                                    <tr><td class="tdc"><input type="submit" class="ebooksstorebutton" name="admin_users_insert" value="Insert"></td> 
                                        <td class="tdc"><input type="submit" class="ebooksstorebutton" name="admin_users_update" value="Update"></td>
                                        <td class="tdc"><input type="submit" class="ebooksstorebutton" name="admin_users_delete" value="Delete"></td> 
                                        <td class="tdc"><input type="submit" class="ebooksstorebutton" name="admin_users_cancel" value="Cancel"></td>
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
    </body>    
</html>
