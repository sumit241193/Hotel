package in.codebootcamp;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeConstants;

/**
 * Servlet implementation class LogIn
 */
@WebServlet("/LogIn")
public class LogIn extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String hashedpassword="";
	private Statement stmt;
	private Connection con;
	private String hashed;
	

	public static String getHash(byte[] passwordbyte,String algo)
	{
		String hashedpassword=""; 
		try 
			{
				MessageDigest msgDigest = MessageDigest.getInstance(algo);
				msgDigest.update(passwordbyte);
				byte[] passworddigest = msgDigest.digest();
				hashedpassword = DatatypeConverter.printHexBinary(passworddigest).toLowerCase();			} 
		catch (NoSuchAlgorithmException e) 
			{
			
			e.printStackTrace();
			}
		
	
		return hashedpassword;
	}
	
	
	
	public void init(ServletConfig config) throws ServletException 
	{
		try 
			{
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://sql178.main-hosting.eu:3306/u248334632_Hotel","u248334632_Hotel","CBCsumit@24");
				stmt = con.createStatement();
			} 
		catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			} 
		catch (SQLException e) 
			{
				e.printStackTrace();
			}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		String user = request.getParameter("user");
		String pass = request.getParameter("pass");
		byte[] passwordbytes = pass.getBytes();
		hashed = getHash(passwordbytes, "SHA-256");
		String sql="Select username,email,password from Register where username ='"+user+"' or email = '"+user+"'  and password= '"+hashed+"' ";
		
		try {
			ResultSet rs = stmt.executeQuery(sql);
			
		while(rs.next())
		{
			String u = rs.getString(1);
			
			String e = rs.getString(2);
			
			String p = rs.getString(3);
			
			if((user.equals(u)) && (hashed.equals(p))  ) 
			{
				out.println("log in succesful");
			}
			else if( (user.equals(e)) && (hashed.equals(p))   )   
			{
				out.println("succesful");
			}
			else 
			{
				out.println("wrong input");
			}
		}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



}
