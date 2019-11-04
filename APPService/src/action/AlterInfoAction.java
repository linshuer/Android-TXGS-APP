package action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import bean.User;
import dao.DAOFactory;
import dao.UserDAO;



public class AlterInfoAction extends HttpServlet {

	public AlterInfoAction() {
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
		String result = "修改失败";

		if(request.getParameter("user_json")!=null){
			String user_json =request.getParameter("user_json");
			UserDAO userdao = DAOFactory.getUserDAOInstance();
			Gson gson = new Gson();

			try {
				User user = gson.fromJson(user_json, User.class);
				userdao.update(user);
				result ="修改成功";

			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("xxxxx");
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
