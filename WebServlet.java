// Handle the query server-side

import java.sql.*;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import com.google.gson.*;
import java.util.Date;
import java.text.*;


class DataInfo {
    String data1;
    String data2;
    String data3;
    String action;
}


public class WebServlet extends HttpServlet {

    static Connection conn;
    static Statement statement;

    public WebServlet ()
    {
	// NOTE: this is where we set up a connection to the dbase.
	// There's a single connection for the life of this servlet,
	// which is opened when the MyBCServlet class instance
	// is first created.
	try {
        Class.forName ("org.h2.Driver");
	    conn = DriverManager.getConnection (
		"jdbc:h2:~/Desktop/myservers/databases/unistyle", 
                "sa", 
	        ""
            );
	    statement = conn.createStatement();
	    System.out.println ("WebServlet: successful connection to H2 dbase");
	}
	catch (Exception e) {
	    // Bad news if we reach here.
	    e.printStackTrace ();
	}
    }
	 
    public void doPost (HttpServletRequest req, HttpServletResponse resp) 
    {
	// We'll print to terminal to know whether the browser used post or get.
	System.out.println ("WebServlet: doPost()");
	handleRequest (req, resp);
    }
    

    public void doGet (HttpServletRequest req, HttpServletResponse resp)
    {
	System.out.println ("WebServlet: doGet()");
	handleRequest (req, resp);
    }
    


    public void handleRequest (HttpServletRequest req, HttpServletResponse resp)
    {
        try {
            // We are going to extract the string line by line
            StringBuffer sbuf = null;
            BufferedReader bufReader = null;
            String inputStr = null;

            bufReader = req.getReader ();
            sbuf = new StringBuffer ();
            while ((inputStr = bufReader.readLine()) != null) {
                sbuf.append (inputStr);
            }

            // What's in the buffer is the entire JSON string.
            String jStr = sbuf.toString();
            System.out.println("Received: " + jStr);


            // Parse out the information from JSON:
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            DataInfo d = gson.fromJson (jStr, DataInfo.class);
            System.out.println ("Received: dataInfo=" + d);
            String action = d.action;

            // Set the content type:
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            Writer writer = resp.getWriter ();
            String outputJson = "";
            String userId;
            switch (action) {
                case "getProducts":
                    outputJson = getProducts();
                    break;

                case "login":
                    userId = confirmUser(d.data1, d.data2);
                    System.out.println("userId: " + userId);
                    outputJson = "{\"userId\":" + userId + "}";
                    break;

                case "signup":
                    userId = signup(d.data1, d.data2);
                    System.out.println("userId: " + userId);
                    outputJson = "{\"userId\":" + userId + "}";
                    break;

                case "adminLogin":
                    userId = confirmAdmin(d.data1, d.data2);
                    System.out.println("userId: " + userId);
                    outputJson = "{\"userId\":" + userId + "}";
                    break;
                case "addAdmin":
                    userId = addAdmin(d.data1, d.data2);
                    System.out.println("userId: " + userId);
                    outputJson = "{\"userId\":" + userId + "}";
                    break;
                case "placeOrder":
                    String orderId = order(d.data1, d.data2);
                    System.out.println("orderId: " + orderId);
                    outputJson = "{\"orderId\":" + orderId + "}";
                    break;
                case "profile":
                    outputJson = getProfile(d.data1);
                    break;
                case "collegeName":
                    userId = collegeName(d.data1, d.data2);
                    outputJson = "{\"userId\":" + userId + "}";
                    break;
                case "email":
                    userId = email(d.data1, d.data2);
                    outputJson = "{\"userId\":" + userId + "}";
                    break;
                case "delete":
                    userId = delete(d.data1);
                    outputJson = "{\"userId\":" + userId + "}";
                    break;
                case "updateProduct":
                    String productId = updateProduct(d.data1, d.data2);
                    outputJson = "{\"productId\":" + productId + "}";
                    break;

                default:
                    // Handle other cases if needed
                    System.out.println("Unknown action: " + action);
            }

            // Write it out and, most importantly, flush():
            writer.write(outputJson);
            writer.flush();

            // Debugging:
            System.out.println (outputJson);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    String getProducts() {
        try {
            String sql = "SELECT * FROM PRODUCTS ";
            ResultSet rs = statement.executeQuery (sql);
            String products = "[";
            
            while (rs.next()) {
                products += "{\"id\": \"" + rs.getInt(1) + "\",\"name\": \"" + rs.getString(2) + "\", \"style\": \"" + rs.getString(3) + "\", \"color\": \"" + rs.getString(4) + "\", \"size\": \"" + rs.getString(5) + "\", \"price\": \"" + rs.getInt(6) + "\", \"url\": \"" + rs.getString(8) +"\" },";
                
            }
            // System.out.println("products: " + products);
            products+="]";
            return products;
        }
        catch (Exception e) {
            e.printStackTrace ();
            return null;
        }
        
    }


    String confirmUser (String username, String password)
    {
        try {
            String sql = "SELECT userId FROM USER WHERE USERNAME = '" + username + "' AND PASSWORD = '" + password + "'";
            ResultSet rs = statement.executeQuery (sql);
            String userId = null;

            while (rs.next()) {
              userId= rs.getString(1);
              System.out.println ("userId: " + userId);
            }
            return userId;
        }
        catch (Exception e) {
            e.printStackTrace ();
            return null;
        }
    }

    String confirmAdmin (String username, String password)
    {
        try {
            String sql = "SELECT userId FROM USER WHERE USERNAME = '" + username + "' AND PASSWORD = '" + password + "' and ROLE='admin'";
            ResultSet rs = statement.executeQuery (sql);
            String userId = null;

            while (rs.next()) {
              userId= rs.getString(1);
              System.out.println ("userId: " + userId);
            }
            return userId;
        }
        catch (Exception e) {
            e.printStackTrace ();
            return null;
        }
    }
    
    
    
    String getProfile(String userId) {
        int uId = Integer.parseInt(userId);
        try {
            String sql = "SELECT COLLEGENAME, EMAIL FROM USER WHERE userId =" + uId;
            ResultSet rs = statement.executeQuery (sql);
            String profile = null;
            
            while (rs.next()) {
                profile = "{\"collegeName\": \"" + rs.getString(1) + "\", \"email\": \"" + rs.getString(2) + "\" }";
                System.out.println("profile: " + profile);
            }
            return profile;
        }
        catch (Exception e) {
            e.printStackTrace ();
            return null;
        }
        
    }
    
    String signup (String username, String password) {
        try {
            String sql = "SELECT MAX(userId) FROM USER";
            ResultSet rs = statement.executeQuery(sql);
            String maxid = "";
            
            while (rs.next()) {
                maxid = rs.getString(1);
                System.out.println("maxid: " + maxid);
            }
            
            int maxInt = Integer.parseInt(maxid);
            int nextInt = maxInt + 1;
            sql = "INSERT INTO USER VALUES(" + nextInt + ", '" + username + "', null, '" + password + "', null, 'college_rep')";
            int success = statement.executeUpdate(sql);
            System.out.println("success: " + success);
            if (success == 1){
                String userId = Integer.toString(nextInt);
                return userId;
            } else {
                return null;
            }
            
        }
        catch (Exception e) {
            e.printStackTrace ();
            return null;
        }
    }
    String addAdmin (String username, String password) {
        try {
            String sql = "SELECT MAX(userId) FROM USER";
            ResultSet rs = statement.executeQuery(sql);
            String maxid = "";
            
            while (rs.next()) {
                maxid = rs.getString(1);
                System.out.println("maxid: " + maxid);
            }
            
            int maxInt = Integer.parseInt(maxid);
            int nextInt = maxInt + 1;
            sql = "INSERT INTO USER VALUES(" + nextInt + ", '" + username + "', '" + password + "', null, 'admin')";
            int success = statement.executeUpdate(sql);
            System.out.println("success: " + success);
            if (success == 1){
                String userId = Integer.toString(nextInt);
                return userId;
            } else {
                return null;
            }
            
        }
        catch (Exception e) {
            e.printStackTrace ();
            return null;
        }
    }

    String order (String userId, String tot_price) {
        try {
            String sql = "SELECT MAX(orderId) FROM ORDERS";
            ResultSet rs = statement.executeQuery(sql);
            String maxid = "";
            
            while (rs.next()) {
                maxid = rs.getString(1);
                System.out.println("maxid: " + maxid);
            }
            
            int maxInt = Integer.parseInt(maxid);
            int nextInt = maxInt + 1;
            int uid= Integer.parseInt(userId);
            int tp=Integer.parseInt(tot_price);
            Date date = new Date();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date today= formatter.parse(formatter.format(date));
            sql = "INSERT INTO ORDERS VALUES(" + nextInt + ", '" + uid + "', '" + today + "', '" + tp + "', + 'pending')";
            int success = statement.executeUpdate(sql);
            System.out.println("success: " + success);
            if (success == 1){
                String orderId = Integer.toString(nextInt);
                return orderId;
            } else {
                return null;
            }
            
        }
        catch (Exception e) {
            e.printStackTrace ();
            return null;
        }
    }
    
    String delete (String userId) {
        try {
            String sql = "DELETE FROM USER WHERE userId = " + userId;
            int success = statement.executeUpdate (sql);
            if (success == 1) {
                return userId;
            } else {
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace ();
            return null;
        }
    }
    String updateProduct (String productId, String quantity) {
        try {
            String sql = "UPDATE PRODUCTS SET quantity = '" + quantity + "' WHERE productId = '" + productId + "'";
            int success = statement.executeUpdate (sql);
            if (success == 1) {
                return productId;
            } else {
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace ();
            return null;
        }
    }
    
   
   
    
    String collegeName (String userId, String collegeName) {
        try {
            String sql = "UPDATE USER SET COLLEGENAME = '" + collegeName + "' WHERE userId = '" + userId + "'";
            int success = statement.executeUpdate (sql);
            if (success == 1) {
                return userId;
            } else {
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace ();
            return null;
        }
    }
    
    
    String email (String userId, String email) {
        try {
            String sql = "UPDATE USER SET EMAIL = '" + email + "' WHERE userId = '" + userId + "'";
            int success = statement.executeUpdate (sql);
            if (success == 1) {
                return userId;
            } else {
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace ();
            return null;
        }
    }
    


}
