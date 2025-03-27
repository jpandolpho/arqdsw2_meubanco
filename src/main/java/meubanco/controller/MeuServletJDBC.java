package meubanco.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/consulta_banco")
public class MeuServletJDBC extends HttpServlet {
	private DataSource dataSource;

	@Override
	public void init() throws ServletException {
		try {
			InitialContext context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/MeuBanco");
		} catch (NamingException e) {
			throw new ServletException("Erro ao configurar JNDI", e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=UTF-8");
		try (Connection conn = dataSource.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT nome FROM usuario")) {
			resp.getWriter().println("<h1>Lista de Usuários:</h1>");
			resp.getWriter().println("<ul>");
			while (rs.next()) {
				resp.getWriter().println("<li>" + rs.getString("nome") + "</li>");
			}
			resp.getWriter().println("</ul>");
		} catch (SQLException e) {
			throw new ServletException("Erro SQL ao acessar o banco", e);
		}
	}
}