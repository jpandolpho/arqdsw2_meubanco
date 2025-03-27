package meubanco.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/consulta_banco")
public class MeuServletJDBC extends HttpServlet {
// Informações da conexão JDBC diretamente no código:
	private static final String URL = "jdbc:mysql://localhost:3307/meu_banco";
	private static final String USER = "root";
	private static final String PASSWORD = "root";
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

	@Override
	public void init() throws ServletException {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			throw new ServletException("Driver JDBC não encontrado", e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=UTF-8");
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT nome FROM usuario")) {
			resp.getWriter().println("<h1>Lista de Usuários (via JDBC):</h1>");
			resp.getWriter().println("<ul>");
			while (rs.next()) {
				resp.getWriter().println("<li>" + rs.getString("nome") + "</li>");
			}
			resp.getWriter().println("</ul>");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}