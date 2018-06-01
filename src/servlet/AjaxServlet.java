package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DbOperation;
import java.sql.*;
import java.util.*;
/**
 * Servlet implementation class AjaxServlet
 */
@WebServlet(description = "处理各种Ajax请求", urlPatterns = { "/AjaxServlet" })
public class AjaxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjaxServlet() {
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
		response.setCharacterEncoding("utf-8");

		String method = request.getParameter("method");

		if(method.equals("search"))
			doSearch(request,response);
		else if(method.equals("checkname"))
			doCheck(request,response);
		else if(method.equals("register"))
			doRegister(request,response);
		else if(method.equals("login"))
			doLogin(request,response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	protected void doSearch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String keyword = request.getParameter("keyword");
		String type = request.getParameter("type");
		String[] str = {"%"+keyword+"%"};
		String sql = "";
		if(type.equals("1"))
			sql = "select keyword from index_list where keyword like ?";
		else
			sql = "select keyword from index_list_2 where keyword like ?";
		
		DbOperation db = new DbOperation();
		db.createPStatement(sql);
		ResultSet rs = db.executeQuery(str);
		String result = "";
		try{
			while(rs.next()){
				result += rs.getString("keyword")+"-";
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		db.close();
		response.getWriter().write(result);
	}
	
	protected void doCheck(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String[] str = {username};
		String sql = "select * from user where name = ?";
		DbOperation db = new DbOperation();
		db.createPStatement(sql);
		ResultSet rs = db.executeQuery(str);
		try {
			if(rs.next())
				response.getWriter().write("exist");
			else
				response.getWriter().write("");
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.close();
		
	}
	
	protected void doRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String tel = request.getParameter("tel");
		String[] str = {username,password,email,tel};

		String sql = "insert into user values(?,?,?,?)";
		DbOperation db = new DbOperation();
		db.createPStatement(sql);
		db.executeUpdate(str);
		db.close();
		
	}
	
	protected void doLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String[] str = {username,password};

		String sql = "select * from user where name = ? and password = ?";
		DbOperation db = new DbOperation();
		db.createPStatement(sql);
		ResultSet rs = db.executeQuery(str);
		try {
			if(!rs.next())
				response.getWriter().write("error");
			else
				response.getWriter().write("");
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.close();

	}
}
