package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import split.News;
import split.Test;
import split.Serialize;

/**
 * Servlet implementation class SearchServlet2
 */
@WebServlet(description = "接收文档列表，封装文档内容，分组返回", urlPatterns = { "/SearchServlet2" })
public class SearchServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet2() {
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
		@SuppressWarnings("unchecked")
		List<List<String>> list = (List<List<String>>)request.getAttribute("list");
		List<String> docList = null;
		List<String> keyList = null;
		if(list==null){
			if(request.getParameter("dlist")!=""){
				docList = Serialize.<String>read(request.getParameter("dlist"));
				keyList = Serialize.<String>read(request.getParameter("klist"));
			}

		}
		else{
			docList = list.get(1);
			keyList = list.get(0);
		}
		
		int total = 0;
		int totalPage = 0;
		int pc;
		String spc = request.getParameter("pc");
		if(spc == null)
			pc = 1;
		else
			pc = Integer.parseInt(spc);
		//计算信息数和总页数
		
		int i = 10*pc;
		
		total = docList.size();
		totalPage = total%10==0?total/10:total/10+1;
		int endIndex = i>total?total:i;
		
		String type = request.getParameter("type");

		List<News> newList;
		if(totalPage==1)
			newList = test.search(docList,keyList,type);
		else{
			newList = test.search(docList.subList(10*(pc-1), endIndex),keyList,type);
		}
		test.close();
		request.setAttribute("dlist", docList);
		request.setAttribute("klist", keyList);
		request.setAttribute("news", newList);
		request.setAttribute("result","<h3>共找到"+total+"条结果<hr>");
		request.setAttribute("totalPage", totalPage);
		request.getRequestDispatcher("search.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
