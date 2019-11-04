package action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import bean.User;
import dao.DAOFactory;
import dao.UserDAO;



public class UserInfoAction extends HttpServlet {

	public UserInfoAction() {
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
		String result = "xxxx";

		if(request.getParameter("username")!=null){
			String username =request.getParameter("username");
			UserDAO userdao = DAOFactory.getUserDAOInstance();
			Gson gson = new Gson();
			try {
				if(userdao.getNowUser(username)!=null){
					User user = userdao.getNowUser(username);
					result = gson.toJson(user);
				}else{
					result ="xxxx";  
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
