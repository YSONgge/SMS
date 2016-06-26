package servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.alibaba.fastjson.JSONObject;

public class UploadServlet extends HttpServlet {

	private String smsFilePath; // sms文件存放目录
	private String contactsFilePath; // contacts文件存放目录
	private String tempPath; // 临时文件目录

	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		// 读取配置文件，获取初始化参数
		smsFilePath = "SmsFile";
		contactsFilePath = "ContactsFile";
		tempPath = "Temp";

		ServletContext context = getServletContext();

		smsFilePath = context.getRealPath(smsFilePath);
		contactsFilePath = context.getRealPath(contactsFilePath);
		tempPath = context.getRealPath(tempPath);
		System.out.println("文件存放目录、临时文件目录准备完毕 ...");
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/plain;charset=UTF-8");

		PrintWriter pw = response.getWriter();
		String status = null;
		try {
			DiskFileItemFactory diskFactory = new DiskFileItemFactory();
			diskFactory.setSizeThreshold(4 * 1024);
			diskFactory.setRepository(new File(tempPath));

			ServletFileUpload upload = new ServletFileUpload(diskFactory);
			upload.setSizeMax(4 * 1024 * 1024);
			List fileItems = upload.parseRequest(request);
			Iterator iter = fileItems.iterator();
			File up = null;
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField()) {
					System.out.println("处理表单内容 ...");
					processFormField(item, pw);
				} else {
					System.out.println("处理上传的文件 ...");
					up = processUploadFile(item, pw);
				}
			}
			status = "OK";

		} catch (Exception e) {
			System.out.println("使用 fileupload 包时发生异常 ...");
			e.printStackTrace();
			status = "ERROR";
		}
		JSONObject json = new JSONObject();

		json.put("result", status);
		System.out.println(status);

		pw.print(json);
		pw.close();
	}

	private String processFormField(FileItem item, PrintWriter out) {
		String name = item.getFieldName();
		String value = item.getString();
		return value;
	}

	// 处理上传的文件
	private File processUploadFile(FileItem item, PrintWriter pw)
			throws Exception {
		// 此时的文件名包含了完整的路径，得注意加工一下
		String filename = item.getName();
		System.out.println("完整的文件名：" + filename);
		//int index = filename.lastIndexOf("\\");
		//filename = filename.substring(index + 1, filename.length());

		long fileSize = item.getSize();

		if ("".equals(filename) && fileSize == 0) {
			System.out.println("文件名为空 ...");
			return null;
		}
		String[] filesTemp = filename.split("_");
		String fileTemp = filesTemp[1];
		//InputStream is = item.getInputStream();
		//File uploadFile;
		if ("SMS.xml".equalsIgnoreCase(fileTemp)) {
			File SMSuploadFile = new File(smsFilePath + "/" + filename);
			item.write(SMSuploadFile);
			return SMSuploadFile;
			//uploadFile = SMSuploadFile;
		} else {
			File CONuploadFile = new File(contactsFilePath + "/" + filename);
			item.write(CONuploadFile);
			return CONuploadFile;
			//uploadFile = CONuploadFile;
		}
		
		//item.write(uploadFile);
	/*	FileOutputStream fos = new FileOutputStream(uploadFile);
		int hasRead = 0;
		byte[] buf = new byte[1024];
		while ((hasRead = is.read(buf)) > 0) {
			fos.write(buf, 0, hasRead);
		}*/
		//return uploadFile;
	}

}
