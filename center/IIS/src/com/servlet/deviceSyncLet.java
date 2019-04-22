package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.Service;

public class deviceSyncLet extends HttpServlet {

	/**
	 * The doGet method of the Server let.
	 */

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		
	}

	/**
	 * The doPost method of the Server let.
	 */

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Service serv = new Service();
				
		String encodedSql = request.getParameter("sql");
		byte[] base64decodedBytes = Base64.getDecoder().decode(encodedSql);
		String sql = new String(base64decodedBytes,"utf-8");
		Boolean ret = serv.execUpdate(sql);
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
				
		out.print(ret?"success":"fail");
		out.flush();
		out.close();
	}

}
