package user.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import user.dao.UserDAO;
import user.model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getServletPath();
		switch(action) {
			case "/new" -> {
				showNewForm(request,response);
			}
			case "/insert" -> {
				try {
					insertuser(request, response);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			case "/delete" -> {
				try {
					deleteuser(request, response);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			case "/edit" -> {
				try {
					showEditForm(request,response);
				} catch (ServletException | IOException | SQLException e) {
					e.printStackTrace();
				}
			}
			case "/update" -> {
				try {
					updateuser(request, response);
				} catch (ServletException | IOException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			default ->{
				try {
					listUser(request, response);
				} catch (ServletException | IOException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private void showNewForm(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException {
		RequestDispatcher dp = req.getRequestDispatcher("user-form.jsp");
		dp.forward(req, res);
	}
	
	private void insertuser(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException, SQLException{
		try {
			String name = req.getParameter("name");
			String email = req.getParameter("email");
			String country = req.getParameter("country");
			User newUser = new User(name,email,country);
			
			UserDAO.insertUser(newUser);
			res.sendRedirect("list");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteuser(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException, SQLException{
		int id = Integer.parseInt(req.getParameter("id"));
		try {
			UserDAO.deleteUser(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		res.sendRedirect("list");
	}
	
	private void showEditForm(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException, SQLException{
		int id = Integer.parseInt(req.getParameter("id"));
		
		User existingUser;
		try {
			existingUser = UserDAO.selectUser(id);
			RequestDispatcher dp = req.getRequestDispatcher("user-form.jsp");
			req.setAttribute("user", existingUser);
			dp.forward(req, res);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	private void updateuser(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException, SQLException{
		int id = Integer.parseInt(req.getParameter("id"));
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String country = req.getParameter("country");
		
		User user = new User(id,name,email,country);
	    UserDAO.updateUser(user);
		res.sendRedirect("list");
	}
	
	private void listUser(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException, SQLException{
		try {
			List<User> listUser = UserDAO.selectAllUsers();
			req.setAttribute("listUser", listUser);
			RequestDispatcher rd = req.getRequestDispatcher("user-list.jsp");
			rd.forward(req, res);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
