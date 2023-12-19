// Handle the query server-side

import java.sql.*;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.util.Date;
import java.text.*;
import java.lang.reflect.Type;

class Hoodie{
    String color;
    int id;
    String name;
    String price; // Assuming price is a decimal value
    int quantity;
    String size;
    String style;

}




public class OrderServlet extends HttpServlet {

    static Connection conn;
    static Statement statement;

    public OrderServlet ()
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
	    System.out.println ("MyServlet: successful connection to H2 dbase");
	}
	catch (Exception e) {
	    // Bad news if we reach here.
	    e.printStackTrace ();
	}
    }
	 
    public void doPost (HttpServletRequest req, HttpServletResponse resp) 
    {
	// We'll print to terminal to know whether the browser used post or get.
	System.out.println ("MyServlet: doPost()");
	handleRequest (req, resp);
    }
    

    public void doGet (HttpServletRequest req, HttpServletResponse resp)
    {
	System.out.println ("MyServlet: doGet()");
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

            // Define the type of the target object
            Type listType = new TypeToken<List<Hoodie>>(){}.getType();

            // Parse the JSON array into a list of objects
            List<Hoodie> yourObjectList = new Gson().fromJson(jStr, listType);

            int orderId= Integer.parseInt(getOrderId());

            String outputJson = "";
            
            for (Hoodie obj : yourObjectList) {
                // System.out.println("ID: " + obj.id + ", Name: " + obj.name);

                String sql = "SELECT MAX(odId) FROM ORDER_DETAIL";
                ResultSet rs = statement.executeQuery(sql);
                String maxid = "";
                
                while (rs.next()) {
                    maxid = rs.getString(1);
                    System.out.println("maxid: " + maxid);
                }
                
                int maxInt = Integer.parseInt(maxid);
                int nextInt = maxInt + 1;
                sql = "INSERT INTO ORDER_DETAIL VALUES(" + nextInt + ", '" + orderId + "', '" + obj.id + "', '" + obj.name + "', '"+ obj.quantity+ "')";
                int success = statement.executeUpdate(sql);
                System.out.println("success: " + success);

            }

            // Set the content type:
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            Writer writer = resp.getWriter ();
            
            String userId;
            

           
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

    String getOrderId () {
        try {
            String sql = "SELECT MAX(orderId) FROM ORDERS";
            ResultSet rs = statement.executeQuery(sql);
            String maxid = "";
            
            while (rs.next()) {
                maxid = rs.getString(1);
                System.out.println("maxid: " + maxid);
            }
            
            return maxid;
            
        }
        catch (Exception e) {
            e.printStackTrace ();
            return null;
        }
    }


}
