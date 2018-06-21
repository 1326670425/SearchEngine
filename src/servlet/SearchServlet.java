package servlet;

import java.io.IOException;

import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import split.Test;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet(description = "接收、处理输入信息，生成文档列表", urlPatterns = { "/SearchServlet" })
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
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

		Test test = new Test();
			String text = request.getParameter("keyword");
			String type = request.getParameter("type");
			//System.out.println(type);
			String sql = "";
			if(type.equals("1"))
				sql = "select IDlist from index_list where keyword like ?";
			else
				sql = "select IDlist from index_list_2 where keyword like ?";
			List<List<String>> list = test.splitWord(text,sql);
			//out.println(list);

			List<String> docList = list.get(1);

			if(docList.isEmpty()){
				request.setAttribute("result", 
						"<h3>未找到相关搜索结果:"+text+"<hr>");
				test.close();
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("search.jsp");
				requestDispatcher.forward(request, response);

			}
			else{

				docList = test.sord(docList);

				list.set(1, docList);
				
				request.setAttribute("list", list);
				test.close();
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("SearchServlet2");
				requestDispatcher.forward(request, response);
			}


	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
