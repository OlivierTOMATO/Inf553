package edu.polytechnique.inf553;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PersonServlet
 */
@WebServlet(urlPatterns = { "/PersonServlet" }, initParams = {
		@WebInitParam(name = "id", value = "", description = "personId") })
public class PersonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PersonServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8");
		String url = "jdbc:postgresql://localhost:5432/postgres";
		String user = "linzhuoyao";
		String password = "admin";

		Connection con;
		PreparedStatement st;
		ResultSet rs;

		try {
			Class.forName("org.postgresql.Driver");

			con = DriverManager.getConnection(url, user, password);

			// query birth date first
			String sql = "select pi.info\r\n" + "from person_info as pi, info_type as it\r\n"
					+ "where pi.info_type_id = it.id and pi.person_id = ? and it.info = 'birth date';";
			st = con.prepareStatement(sql);
			int id = Integer.parseInt(request.getParameter("id"));
			st.setInt(1, id);
			rs = st.executeQuery();

			String birth = "";
			if (rs.next()) {
				birth = "<birthday> born in " + rs.getString(1) + "</birthday>";
			}

			// query all the movies
			sql = "SELECT distinct(m.id), m.title, p.name, p.gender\r\n"
					+ "FROM cast_info AS c, person AS p, movie AS m, role_type AS r\r\n"
					+ "WHERE c.person_id = p.id\r\n" + "and c.movie_id = m.id\r\n" + "and c.role_id = r.id\r\n"
					+ "and (r.role = 'actor' \r\n" + "or  r.role = 'actress')\r\n" + "and p.id = ?";
			st = con.prepareStatement(sql);
			st.setInt(1, id);
			rs = st.executeQuery();

			response.getWriter().append("<result>");
			if (rs.next()) {
				String gender = rs.getString(4);
				response.getWriter().append("<row>");
				response.getWriter().append("<name>");
				// to deal with gender
				if (gender.equals("m")) {
					System.out.print(1);
					response.getWriter().append("Mr. ");
				} else if (gender.equals("f")) {
					response.getWriter().append("Mrs. ");
				}
				response.getWriter().append(rs.getString(3));
				response.getWriter().append("</name>");
				response.getWriter().append(birth);
				response.getWriter().append("</row>");
				response.getWriter().append("<br>");
				
//				response.getWriter().append(rs.getInt(1) + " " + rs.getString(2) + "<br>");
				response.getWriter().append("<row>");
				response.getWriter().append("<film id>");
				response.getWriter().append(rs.getInt(1) + " ");
				response.getWriter().append("</film id>");
				response.getWriter().append("<film name>");
				response.getWriter().append(rs.getString(2));
				response.getWriter().append("</film name>");
				response.getWriter().append("</row>");
				response.getWriter().append("<br>");
			}
			while (rs.next()) {
				response.getWriter().append("<row>");
				response.getWriter().append("<film id>");
				response.getWriter().append(rs.getInt(1) + " ");
				response.getWriter().append("</film id>");
				response.getWriter().append("<film name>");
				response.getWriter().append(rs.getString(2));
				response.getWriter().append("</film name>");
				response.getWriter().append("</row>");
				response.getWriter().append("<br>");
//				response.getWriter().append(rs.getInt(1) + " " + rs.getString(2) + "<br>");
			}
			response.getWriter().append("</result>");
			rs.close();
			st.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("error");
		} finally {
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}