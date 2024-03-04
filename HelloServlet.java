package com.jetbrains.advancedjavatomcat;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/login")
public class HelloServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        String userIn = request.getParameter("username");
        String passIn = request.getParameter("password");
         PrintWriter pw = response.getWriter();
        if(userIn.equals("admin") && passIn.equals("admin")){
            RequestDispatcher rd = request.getRequestDispatcher("/WelcomeAdmin.html");
            rd.forward(request,response);
        } else{
            try {
                String url = "jdbc:mysql://localhost:3306/logindata";
                String username = "root";
                String password = "root";

                try (Connection conn = DriverManager.getConnection(url, username, password)) {
                    String sql = "SELECT username, password FROM logindata WHERE username = ?";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setString(1, userIn);
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        String storedPassword = resultSet.getString("password");
                        if (passIn.equals(storedPassword)) {
                            pw.write("<h1>Login Successful</h1>");
                        }else{
                            RequestDispatcher rd = request.getRequestDispatcher("/Error.html");
                            rd.forward(request,response);
                        }
                    } else {
                        RequestDispatcher rd = request.getRequestDispatcher("/Error.html");
                        rd.forward(request,response);
                    }
                }
            } catch (Exception e) {
                pw.write("<h1>Error: " + e.getMessage() + "</h1>");
            }
        }

    }
}