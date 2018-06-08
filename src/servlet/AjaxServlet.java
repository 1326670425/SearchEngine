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
import split.Test;
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
		else if(method.equals("submit"))
			doSubmit(request,response);
		else if(method.equals("quiz"))
			doQuiz(request,response);

	}



	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	/**
	 * 
	 * @Title doSearch
	 * @Description 模糊搜索
	 * @param @param request
	 * @param @param response
	 * @return void
	 * @throws
	 */
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
	/**
	 * 
	 * @Title doCheck
	 * @Description 验证注册用户名是否存在
	 * @param @param request
	 * @param @param response
	 * @return void
	 * @throws
	 */
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
	/**
	 * 
	 * @Title doRegister
	 * @Description 处理注册请求
	 * @param @param request
	 * @param @param response
	 * @return void
	 * @throws
	 */
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
	/**
	 * 
	 * @Title doLogin
	 * @Description 处理登录请求
	 * @param @param request
	 * @param @param response
	 * @return void
	 * @throws
	 */
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
	/**
	 * @Title doSubmit
	 * @Description 处理回复请求
	 * @param @param request
	 * @param @param response
	 * @return void
	 * @throws
	 */
	private void doSubmit(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String username = request.getParameter("username");
		String detail = request.getParameter("detail");
		String qid = request.getParameter("qid");
		String sql = "insert into answer(user,detail,date,qid) values(?,?,now(),?)";
		String str[] = {username,detail,qid};
		DbOperation db = new DbOperation();
		db.createPStatement(sql);
		db.executeUpdate(str);
		db.close();
	}
	/**
	 * @Title doQuiz
	 * @Description 处理提问请求
	 * @param @param request
	 * @param @param response
	 * @return void
	 * @throws
	 */
	private void doQuiz(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String username = request.getParameter("username");
		String title = request.getParameter("title");
		String detail = request.getParameter("detail");
		String sql = "insert into question(title,user,detail,date) values(?,?,?,now())";
		String str[] = {title,username,detail};
		DbOperation db = new DbOperation();
		db.createPStatement(sql);
		ResultSet rs = db.executeUpdate(str);
		int id = 0;
		try {
			rs.next();
			id= rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		str = new Test().extract(title, detail);
		
		sql = "insert into index_list_2 values(?,?) ON DUPLICATE KEY UPDATE IDlist=concat(?,IDlist)";
		for(int i=0;i<str.length;i++){
			db.createPStatement(sql);
			db.executeUpdate(new String[]{str[i],""+id,id+" "});
		}
		db.close();
	}
}
