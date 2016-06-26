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

	private String smsFilePath; // sms�ļ����Ŀ¼
	private String contactsFilePath; // contacts�ļ����Ŀ¼
	private String tempPath; // ��ʱ�ļ�Ŀ¼

	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		// ��ȡ�����ļ�����ȡ��ʼ������
		smsFilePath = "SmsFile";
		contactsFilePath = "ContactsFile";
		tempPath = "Temp";

		ServletContext context = getServletContext();

		smsFilePath = context.getRealPath(smsFilePath);
		contactsFilePath = context.getRealPath(contactsFilePath);
		tempPath = context.getRealPath(tempPath);
		System.out.println("�ļ����Ŀ¼����ʱ�ļ�Ŀ¼׼����� ...");
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
					System.out.println("��������� ...");
					processFormField(item, pw);
				} else {
					System.out.println("�����ϴ����ļ� ...");
					up = processUploadFile(item, pw);
				}
			}
			status = "OK";

		} catch (Exception e) {
			System.out.println("ʹ�� fileupload ��ʱ�����쳣 ...");
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

	// �����ϴ����ļ�
	private File processUploadFile(FileItem item, PrintWriter pw)
			throws Exception {
		// ��ʱ���ļ���������������·������ע��ӹ�һ��
		String filename = item.getName();
		System.out.println("�������ļ�����" + filename);
		//int index = filename.lastIndexOf("\\");
		//filename = filename.substring(index + 1, filename.length());

		long fileSize = item.getSize();

		if ("".equals(filename) && fileSize == 0) {
			System.out.println("�ļ���Ϊ�� ...");
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
