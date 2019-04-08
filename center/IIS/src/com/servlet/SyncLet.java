package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.service.Service;

public class SyncLet extends HttpServlet {
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
		// 新建服务对象
		Service serv = new Service();
			
		// 接收注册信息
		String userId = request.getParameter("id");
		String data = request.getParameter("data");
		System.out.println("Sync  "+ userId);
		System.out.println("data  "+ data);
		serv.add(userId, data);
	}

}
