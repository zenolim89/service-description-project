package com.kt.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/queryTest")
public class FirstServlet extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.service(arg0, arg1);
	}

	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.service(arg0, arg1);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub

		String domain = req.getParameter("domain");
		String dictionary = req.getParameter("dictionary");
		String svcType = req.getParameter("svcType");
		String svcDescription = req.getParameter("svcDescription");
		String svcIntent = req.getParameter("svcIntent");
		String targetURL = req.getParameter("targetURL");
		String method = req.getParameter("method");
		String dataType = req.getParameter("dataType");
		String dataDescription = req.getParameter("dataDescription");

		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter out = resp.getWriter();
		out.print("<html><head><title>질의문자열테스트</title></head>");
		out.print("<body>");
		out.print("도메인 코드 : " + domain + "<br/>");
		out.print("참고 어휘사전 : " + dictionary + "<br/>");
		out.print("서비스 제공 타입 : " + svcType + "<br/>");
		out.print("서비스 설명 : " + svcDescription + "<br/>");
		out.print("서비스 인텐트 : " + svcIntent + "<br/>");
		out.print("타켓 URL : " + targetURL + "<br/>");
		out.print("메소드 : " + method + "<br/>");
		out.print("데이터 타입 : " + dataType + "<br/>");
		out.print("데이터 상세 : " + dataDescription + "<br/>");
		out.print("post방");
		out.println("</body></html>");
		out.close();

	}

}
