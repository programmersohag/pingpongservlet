package com.sohaq;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/udp")
public class DemoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String url = req.getParameter("url");
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject obj = new JSONObject();
        try {
            List<PingResponse> data = PingClient.doPing(url);
            JSONArray arr = new JSONArray(data);
            obj.put("data", arr);
            out.println(obj);
            out.flush();
            resp.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
            obj.put("message", "Request processing fail");
            out.println(obj);
            resp.flushBuffer();
            out.flush();
        }
    }
}
