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

import bean.Article;
import bean.User;
import dao.ArticleDAO;
import dao.DAOFactory;
import dao.UserDAO;



public class AddArticleAction extends HttpServlet {

	public AddArticleAction() {
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
		String result = "发表失败";

		if(request.getParameter("article_json")!=null){
			String article_json =request.getParameter("article_json");
			ArticleDAO articledao = DAOFactory.getArticleDAOInstance();
			Gson gson = new Gson();

			try {
				Article article = gson.fromJson(article_json, Article.class);
				articledao.insert(article);
				result ="发表成功";

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
