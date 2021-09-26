package com.sohaq;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/udp")
public class DemoServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject obj = new JSONObject();
        obj.put("text", "Hello World Servlet");
        out.println(obj);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getParameter("url");
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject obj = new JSONObject();
        try {
            List<PingResponse> data = PingClient.doPing(url);
            JSONArray arr = new JSONArray(data);
            obj.put("data", arr);
            out.println(obj);
        } catch (Exception e) {
            obj.put("message", "Request processing fail");
            out.println(obj);
        }
    }
}
