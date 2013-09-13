package it.fileupload;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListDocumentServlet extends HttpServlet {

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");  
        	String ipDb = "127.0.0.1";
        	String dbSid= "SID";
        	String username = "user";
        	String password = "pw";
        	
            Connection con =   DriverManager.getConnection ("jdbc:oracle:thin:@"+ipDb+":1521:"+dbSid, username, password);
            con.setAutoCommit(false);  
            PreparedStatement ps = con.prepareStatement("select CL_DOCUMENT_ID as id, DOCUMENT_NAME as name, DOCUMENT_TYPE as type from CL_ACCOUNT_DOCUMENT order by CL_DOCUMENT_ID");
            
            ResultSet rs = ps.executeQuery();
            out.println("<h1>Document</h1>");
            while ( rs.next()) {
            	String type = rs.getString("type");
            	if (type.contains("image")){
            		out.println("<h4>" + rs.getString("id") + "-" + rs.getString("name") + "</h4>");
	                out.println("<a href='displaydocument?id=" +  rs.getString("id") + "'>");
	                out.println("<img width='200' height='200' src=displaydocument?id=" +  rs.getString("id") + "></img> </a> <p/>");
            	}else{
	                out.println("<a href='displaydocument?id=" +  rs.getString("id") + "'");
            		out.println("<h4>" + rs.getString("id") + "-" + rs.getString("name") + "</h4>");
	                out.println(" </a> <p/>");
            	}
            }
            con.close();
        }
        catch(Exception ex) {
        	ex.printStackTrace();
        }
        finally { 
            out.close();
        }
        out.flush();
        out.close();
    } 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
}
