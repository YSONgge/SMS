package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import dao.impl.IUserDaoImpl;
import entity.User;
import entity.UserInfoMsg;
import factory.Factory;

public class Login extends HttpServlet {

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

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		String account = request.getParameter("account");
		String password = request.getParameter("password");
		request.setCharacterEncoding("utf-8");
		User u = new User(account, password);
		boolean result = Factory.getIUserService().userLogin(u);
		User u1 = Factory.getIUserService().selectUserId(account);
		UserInfoMsg msg = new UserInfoMsg();
		msg.setResult(result);
		if (result == true) {
			msg.setUser(u1);
		}
		String json = JSON.toJSONString(msg);
		out.println(json);
		if (result) {
			log("login success " + result);
		} else {
			log("login fail " + result);
		}
		out.flush();
		out.close();
	}

}
