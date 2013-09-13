package it.fileupload;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import oracle.sql.BFILE;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.JsonObject;

public class FileUploadServlet extends HttpServlet implements Servlet {

    private static final long serialVersionUID = 2740693677625051632L;
    private static final String filePath = "c:\\temp";
    private static final int maxSizeMB = 10;
    private static final int maxSizeFileUpload = (maxSizeMB * 1024 * 1024);//1048576;// 1 MB in bytes
	
    int count;
    public void init(ServletConfig config)
    {
    	System.out.println("Init FileUploadServlet ");
    	count = 0;
    }
    
    //private static final int maxMemSize = (maxSizeMB * 1024 * 1024);
    public FileUploadServlet() {
        super();
    }
    /*
     * 
     * 	
        CREATE SEQUENCE  "BM"."SEQ_ACCOUNT_DOCUMENT"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 266 NOCACHE  NOORDER  NOCYCLE ;
		
		--MB tab per la storiciz dei documenti
		CREATE TABLE "CL_ACCOUNT_DOCUMENT" (	
		     "CL_DOCUMENT_ID" NUMBER, 
		     "CL_UPD_DATE" TIMESTAMP (6) DEFAULT SYSDATE, 
		     "DOCUMENT_BINARY" BLOB,
		     "DOCUMENT_TYPE" VARCHAR2(256 BYTE)
		)
		
		--DROP TABLE "CL_ACCOUNT_DOCUMENT"

     * 
     * */

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	ServletOutputStream out		= response.getOutputStream();
        HttpSession session			= request.getSession();
    	try {
			if (request.getContentLength() > maxSizeFileUpload) {
				throw new RuntimeException("Errore file superiore a ["+maxSizeFileUpload+"]");
			}

	    	
	        FileItemFactory factory		= new DiskFileItemFactory();
	        ServletFileUpload upload	= new ServletFileUpload(factory);

	        FileUploadListener listener = new FileUploadListener();
	        session.setAttribute("LISTENER", listener);
	      
	        upload.setSizeMax(maxSizeFileUpload);

	        upload.setProgressListener(listener);
	        List uploadedItems	= null;
	        FileItem fileItem	= null;

        	if (ServletFileUpload.isMultipartContent(request)){
        		System.out.println("E' multipart");
	        	
	            uploadedItems = upload.parseRequest(request);

	            Iterator i = uploadedItems.iterator();

	            while (i.hasNext()) {
	                fileItem = (FileItem) i.next();
	                if (fileItem.isFormField() == false) {
	                    if (fileItem.getSize() > 0) {

//********************************* DB ********************************************************************************|
	                        // Connect to Oracle  
	                        Class.forName("oracle.jdbc.driver.OracleDriver");  
	                    	String ipDb = "127.0.0.1";
	                    	String dbSid= "SID";
	                    	String username = "user";
	                    	String password = "pw";
	                    	
	                        Connection con =   DriverManager.getConnection ("jdbc:oracle:thin:@"+ipDb+":1521:"+dbSid, username, password);
	                        con.setAutoCommit(false);  

System.out.println("size ["+fileItem.getSize()+"]");
///if (fileItem.getSize() > maxSizeFileUpload ){
///	throw new RuntimeException("Errore file superiore ad ["+maxSizeFileUpload+"]");
///}
// ****************** Inserimento Con SP 	  
//	                        String sp = "  { call BM.INSERT_ACCOUNT_DOCUMENT( ?, ?, ?, ?) }";
//	                        CallableStatement cs          = null;
//	                        cs = con.prepareCall(sp);
//	                        cs.registerOutParameter(1, Types.INTEGER);
//	                        cs.setBinaryStream(2, fileItem.getInputStream(), (int) fileItem.getSize());
//	                        cs.setString(3, fileItem.getContentType());
//	                        cs.setString(4, fileItem.getName());
//
//	                        cs.execute();
//	                        System.out.println("result SP INSERT_ACCOUNT_DOCUMENT : ["+cs.getInt(1)+"]");
//	                        cs.close();
	                        
// ****************** Inserimento Con SQL Insert 	                        
	                        PreparedStatement ps = con.prepareStatement("insert into CL_ACCOUNT_DOCUMENT(CL_DOCUMENT_ID, CL_UPD_DATE, DOCUMENT_BINARY, DOCUMENT_TYPE,DOCUMENT_NAME) values(SEQ_ACCOUNT_DOCUMENT.nextval ,sysdate,?,?,?)");  
//	                        // size must be converted to int otherwise it results in error  
	                        ps.setBinaryStream(1, fileItem.getInputStream(), (int) fileItem.getSize()); 
	                        ps.setString(2, fileItem.getContentType());
	                        ps.setString(3, fileItem.getName());  
	                        ps.executeUpdate();                    


//	                        ps.close();
	                        
	                        con.commit();  
	                        con.close();  
//********************************* DB ********************************************************************************|
	                    	
	                        File uploadedFile		= null;
	                        String myFullFileName	= fileItem.getName(), myFileName = "", slashType = (myFullFileName.lastIndexOf("\\") > 0) ? "\\" : "/";
	                        int startIndex	= myFullFileName.lastIndexOf(slashType);
	                        myFileName		= myFullFileName.substring(startIndex + 1, myFullFileName.length());
	                        uploadedFile	= new File(filePath, myFileName);
	                        fileItem.write(uploadedFile);

	                        out.println("File Uploaded Successfully.");  
	                    }else{
	                    	out.println("File Non valido o vuoto.");  
	                    }
	                }
	            }
        	}else{
                out.println("File Uploaded Error.");  
        	}
        } catch (RuntimeException e) {
        	e.printStackTrace();
	        FileUploadListener listener = new FileUploadListener();
	        listener.setEndForMax(true);
	        session.setAttribute("LISTENER", listener);
        	out.println("Exception :"+e.getMessage());  
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.flush();
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out				= response.getWriter();
        HttpSession session			= request.getSession();
        FileUploadListener listener = null;
        long bytesRead = 0, contentLength = 0;
		response.setContentType("application/json");
        //JSONArray ar = new JSONArray();
		 JsonObject jsonObj = new  JsonObject();
		
System.out.println(":: session ::"+session);
        if (session == null) {
            return;
        } else if (session != null) {
            listener = (FileUploadListener) session.getAttribute("LISTENER");

            if (listener == null) {
System.out.println(":: listener :getBytesRead: 0");

				jsonObj.addProperty("bytesRead", "0");
				jsonObj.addProperty("percentComplete", "0");
				jsonObj.addProperty("contentLength", "0");
                //jsonObj.put("bytesRead", "0");
                //jsonObj.put("percentComplete", "0");
                //jsonObj.put("contentLength", "0");
                
            } else {
System.out.println(":: listener :: "+listener.isEndForMax());
            	
                bytesRead		= listener.getBytesRead();
System.out.println(":: listener :getBytesRead: "+bytesRead);
                contentLength	= listener.getContentLength();
                
                long percentComplete = 0L;
                if (bytesRead == contentLength) {
                	percentComplete = 100L;
                	session.setAttribute("LISTENER", null);
                } else {
                	
                    percentComplete = ((100 * bytesRead) / contentLength);
                }

                jsonObj.addProperty("bytesRead", bytesRead);
                jsonObj.addProperty("percentComplete", percentComplete);
                jsonObj.addProperty("contentLength", contentLength);

            }
        }

        out.print(jsonObj);
        out.flush();
        out.close();
    }
}