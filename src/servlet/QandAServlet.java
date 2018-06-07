package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import database.DbOperation;
import split.News;

/**
 * Servlet implementation class QandAServlet
 */
@WebServlet(description = "处理问答页面", urlPatterns = { "/QandAServlet" })
public class QandAServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QandAServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		request.setCharacterEncoding("utf-8");
		String id = request.getParameter("id");

		
		DbOperation db = new DbOperation();
		ResultSet rs = null;
		List<News> list = new ArrayList<News>();
		News news = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String sql = "select * from question where id = ?";
		db.createPStatement(sql);
		
		try {
			rs = db.executeQuery(new String[]{id});
			rs.next();
			news = new News();
			news.setId(rs.getString("id"));
			news.setTitle(rs.getString("title"));
			request.setAttribute("title", rs.getString("title"));
			news.setDescription(rs.getString("detail"));
			news.setUser(rs.getString("user"));
			news.setTime(sdf.format(rs.getTimestamp("date")));
			list.add(news);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sql = "select * from answer where qid = ? order by date desc";
		db.createPStatement(sql);
		try {
			rs = db.executeQuery(new String[]{id});
			while(rs.next()){
				news = new News();
				news.setId(rs.getString("id"));
				news.setDescription(rs.getString("detail"));
				news.setUser(rs.getString("user"));
				news.setTime(sdf.format(rs.getTimestamp("date")));
				list.add(news);
			}
			rs.close();
			db.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("list", list);
		request.getRequestDispatcher("question.jsp").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
