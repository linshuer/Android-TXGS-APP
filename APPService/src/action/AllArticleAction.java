package action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bean.Article;
import split.SplitPage;

public class AllArticleAction extends HttpServlet {


	public AllArticleAction() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}


	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String result = "error";
		SplitPage listPage = new SplitPage();
		List<Article> all = new ArrayList<Article>();
		String sql = "select * from article";// order by studentID
		int pageSize = 3;
		int ipage = 1;
		if (request.getParameter("showpage") == null) {
			ipage = 1;
		} else {
			ipage = Integer.parseInt(request.getParameter("showpage").toString());
			System.out.println(ipage);
			if (ipage <= 0) {
				result = "已无更多数据";
			} else {
				all = listPage.getPage(sql, pageSize, ipage);
				if (ipage > listPage.getPageCount()) {
					result = "已无更多数据";
				} else {
					Gson gson = new Gson();
					String list = gson.toJson(all);
					result = list;
					System.out.println(result);
				}
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
