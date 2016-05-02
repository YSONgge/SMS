package servlet;

import java.io.File;
import java.io.IOException;
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

		super.init();
		// ��ȡ�����ļ�����ȡ��ʼ������
		smsFilePath = "SmsFile";
		contactsFilePath = "ContactsFlie";
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
		
		File sms = new File(smsFilePath+"/");
		
		PrintWriter out = response.getWriter();
		try{
			DiskFileItemFactory diskFactory = new DiskFileItemFactory();
			diskFactory.setSizeThreshold(4*1024);
			diskFactory.setRepository(new File(tempPath));
			
			ServletFileUpload upload = new ServletFileUpload(diskFactory);
			upload.setSizeMax(4*1024*1024);
			List fileItems = upload.parseRequest(request);
			Iterator iter = fileItems.iterator();
			File up = null;
			while (iter.hasNext()){
				FileItem item = (FileItem) iter.next();
				if (item.isFormField()) {
					System.out.println("��������� ...");
					processFormField(item, out);
				} else {
					System.out.println("�����ϴ����ļ� ...");
					up = processUploadFile(item, out);
				}
			}
			
		}catch (Exception e) {
			System.out.println("ʹ�� fileupload ��ʱ�����쳣 ...");
			e.printStackTrace();
		}
		JSONObject json = new JSONObject();
		json.put("result", "OK");
		
		out.print(json);
		out.close();
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
		int index = filename.lastIndexOf("\\");
		filename = filename.substring(index + 1, filename.length());

		long fileSize = item.getSize();

		if ("".equals(filename) && fileSize == 0) {
			System.out.println("�ļ���Ϊ�� ...");
			return null;
		}
		
		File uploadFile = new File(contactsFilePath + "/" + filename);
		item.write(uploadFile);
		return uploadFile;
	}
}
