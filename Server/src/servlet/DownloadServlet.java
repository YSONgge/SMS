package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.fabric.Response;

public class DownloadServlet extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		ServletContext cntx = getServletContext();
		OutputStream out = response.getOutputStream();
		
		String userIdType = request.getParameter("userIdType");
		System.out.println(userIdType);
		String[] userId_Type =userIdType.split("_");
		int userId = Integer.parseInt(userId_Type[0]);
		String type = userId_Type[1];
		
		String fileName ;
		
		if(type.equalsIgnoreCase("sms")){
			fileName = cntx.getRealPath("SmsFile/"+userIdType+".xml");
		}else{
			fileName = cntx.getRealPath("ContactsFile/"+userIdType+".xml");
		}
		File f = new File(fileName);

		String mime = cntx.getMimeType(fileName);
		response.setContentType(mime);
		response.setContentLength((int) f.length());
		
		FileInputStream fin = new FileInputStream(f);	
		
		byte[] buf = new byte[1024];
		int count = 0 ;
		while((count = fin .read(buf))>=0){
			out.write(buf, 0, count);
		}
		System.out.println("test"+userId);
		out.flush();
		fin.close();
	}

}
