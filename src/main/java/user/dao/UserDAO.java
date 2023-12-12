package user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.jdbc.Driver;

import jakarta.servlet.http.HttpServlet;
import user.model.User;

// This DAO class provides CRUD database operations for the table users in the database.
public class UserDAO extends HttpServlet{
	private static String jdbcURL = "jdbc:mysql://localhost:3306/demo";
	private static String jdbcUsername = "root";
	private static String jdbcPassword = "1234";
	
	private static final String INSERT_USERS_SQL = 
	"INSERT INTO users" + 
	"(name, email, country) VALUES "+ 
    "(?, ?, ?);";
	
	private static final String SELECT_USER_BY_ID = "SELECT * FROM users where id = ?;";
	private static final String SELECT_ALL_USERS = "SELECT * FROM users;";
	private static final String DELETE_USERS_SQL = "DELETE FROM users WHERE id = ?;";
	private static final String UPDATE_USERS_SQL = "UPDATE user SET name = ?, email = ?, country = ? WHERE id = ?;";

	protected static Connection getConnection() throws SQLException, ClassNotFoundException {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL,jdbcUsername,jdbcPassword);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return connection;
	}
	
	// Create or insert user
	public static void insertUser(User user) throws SQLException{
		try (Connection connection = getConnection(); 
				 PreparedStatement ps = connection.prepareStatement(INSERT_USERS_SQL)) {
				 ps.setString(1, user.getName());
				 ps.setString(2, user.getEmail());
				 ps.setString(3, user.getCountry());
				 ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	// select user by id
	public static User selectUser(int id) throws SQLException, ClassNotFoundException{
		User user = null;
		try(Connection connect = getConnection(); PreparedStatement ps = connect.prepareStatement(SELECT_USER_BY_ID);) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				user = new User(id,name,email,country);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return user;
	}
	// select users
	public static List<User> selectAllUsers(){
		List<User> users = new ArrayList<User>();
		try(Connection connect = getConnection(); PreparedStatement ps = connect.prepareStatement(SELECT_ALL_USERS);) {
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				User user = new User(id,name,email,country);
				users.add(user);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return users;
	}
	// update user
	public static boolean updateUser(User user) {
		boolean rowUpdate = false;
		try(Connection connect = getConnection(); PreparedStatement ps = connect.prepareStatement(UPDATE_USERS_SQL);) {
			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getCountry());
			ps.setInt(4,user.getId());
			
			rowUpdate = ps.executeUpdate() > 0;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return rowUpdate;
	}
	// delete user
	// select user by id
	public static boolean deleteUser(int id) throws SQLException, ClassNotFoundException{
		boolean rowDeleted = false;
		try(Connection connect = getConnection(); PreparedStatement ps = connect.prepareStatement(DELETE_USERS_SQL);) {
			ps.setInt(1, id);
			rowDeleted = ps.executeUpdate() > 0 ;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowDeleted;
	}
}
