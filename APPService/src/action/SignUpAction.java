package action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.User;
import dao.DAOFactory;
import dao.UserDAO;



public class SignUpAction extends HttpServlet {

	public SignUpAction() {
		super();
	}


	public void destroy() {
		super.destroy();
	}


	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String result = "注册失败";

		if(request.getParameter("username")!=null&&request.getParameter("password")!=null){
			String username =request.getParameter("username");
			String password = request.getParameter("password");
			UserDAO userdao = DAOFactory.getUserDAOInstance();
			User user = new User();
			try {
				if(!userdao.isUser(username)){
					user.setUsername(username);
					user.setPassword(password);
					userdao.insert(user);
					result ="注册成功";
				}else{
					result ="用户名已存在";  
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		out.write(result);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request,response);
	}


	public void init() throws ServletException {
		// Put your code here
	}

}
