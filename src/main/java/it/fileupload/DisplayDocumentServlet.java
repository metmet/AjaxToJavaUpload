package it.fileupload;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DisplayDocumentServlet extends HttpServlet {
    /**
	 *   CREATE TABLE "CL_ACCOUNT_DOCUMENT" 
		   (	"CL_DOCUMENT_ID" NUMBER, 
			"CL_UPD_DATE" TIMESTAMP (6) DEFAULT SYSDATE, 
			"DOCUMENT_BINARY" BLOB, 
			"DOCUMENT_TYPE" VARCHAR2(256 BYTE), 
			"DOCUMENT_NAME" VARCHAR2(256 BYTE)
		   )
	 */
	private static final long serialVersionUID = -1178310190157520952L;
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        try {
        	String ipDb = "127.0.0.1";
        	String dbSid= "SID";
        	String username = "user";
        	String password = "pw";
        	
            Connection con =   DriverManager.getConnection ("jdbc:oracle:thin:@"+ipDb+":1521:"+dbSid, username, password);
            con.setAutoCommit(false);  
            PreparedStatement ps = con.prepareStatement("select DOCUMENT_BINARY as document, DOCUMENT_TYPE as type from CL_ACCOUNT_DOCUMENT  where CL_DOCUMENT_ID = ?");
            
            String id = request.getParameter("id");
            ps.setString(1,id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            Blob  b = rs.getBlob("document");
            response.setContentType("type");
            response.setContentLength( (int) b.length());
            InputStream is = b.getBinaryStream();
            OutputStream os = response.getOutputStream();
            byte buf[] = new byte[(int) b.length()];
            is.read(buf);
            os.write(buf);
            os.close();
        }
        catch(Exception ex) {
             System.out.println(ex.getMessage());
        }
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