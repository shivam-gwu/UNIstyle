
import java.sql.*;

public class DbCreator {

    static Statement statement;         

    public static void main (String[] argv)
    {
        try {
	    // The first step is to load the driver and use it to open
	    // a connection to the H2 server (that should be running).
            Class.forName ("org.h2.Driver");
            Connection conn = DriverManager.getConnection (
   	        "jdbc:h2:~/Desktop/myservers/databases/unistyle", 
		"sa", 
		""
            );

	    // If the connection worked, we'll reach here (otherwise an 
	    // exception is thrown.

	    // Now make a statement, which is the object used to issue
	    // queries.
	    statement = conn.createStatement ();
	    
	    // The user table:
	    // makeUserTable ();
	    // printTable ("USER", 5);

	    // The products table:
	    makeProductsTable ();
	    printTable ("PRODUCTS", 3);


	    // The orders table:
	    // makeOrdersTable ();
	    // printTable ("ORDERS", 2);

	    // The order detail table:
	    // makeOrderDetailTable ();
	    // printTable ("ORDER_DETAIL", 2);
        
        

	    // Close the connection, and we're done.
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void printTable (String tableName, int numColumns)
	throws SQLException
    {
	// Build the SELECT query string:
	String sql = "SELECT * FROM " + tableName;

	// Execute at the database, which returns rows that are
	// placed into the ResultSet object.
	ResultSet rs = statement.executeQuery (sql);

	// Now extract the results from ResultSet
	System.out.println ("\nRows from " + tableName + ":");
	while (rs.next()) {
	    String row = "Row: ";
	    for (int i=1; i<=numColumns; i++) {
		String s = rs.getString (i);      
		// One could get an int column into an int variable.
		row += " " + s;
	    }
	    System.out.println (row);
	}
    }


    static void makeUserTable ()
	throws SQLException
    {
    
    String sql = "DROP TABLE IF EXISTS USER";
	statement.executeUpdate (sql);
	sql = "CREATE TABLE USER (userId INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(25), collegename VARCHAR(100), password VARCHAR(12), email VARCHAR(50), role VARCHAR(20))";
	statement.executeUpdate (sql);

	// Insert rows one by one.
	sql = "INSERT INTO USER (username, collegename, password, email, role) VALUES ( 'shivam98', null, 'Pass1234',  'shivam@gateway.edu', 'college_rep')";
	statement.executeUpdate (sql);
	sql = "INSERT INTO USER (username, collegename, password, email, role) VALUES ( 'animal', null, 'Animal1234',  'jill@gateway.edu', 'admin')";
	statement.executeUpdate (sql);
    }


    static void makeProductsTable ()
	throws SQLException
    {
    	String sql = "DROP TABLE IF EXISTS PRODUCTS";
		statement.executeUpdate (sql);
        
         sql = "CREATE TABLE IF NOT EXISTS PRODUCTS (productId INT PRIMARY KEY AUTO_INCREMENT,name VARCHAR(255) ,style VARCHAR(50),color VARCHAR(50),size VARCHAR(20),quantity INT,price INT, url VARCHAR(30));";
        statement.executeUpdate (sql);

        sql= "INSERT INTO PRODUCTS (name, style, color, size, quantity, price, url) VALUES\n" +
            "('Hoodie 1', 'Pullover', 'Black', 'Medium', 10, 29.99, 'hoodie1.jpeg'),\n" +
            "('Hoodie 2', 'Zip-Up', 'Blue', 'Large', 5, 39.99,'hoodie2.jpg'),\n" +
            "('Hoodie 3', 'Crop', 'Red', 'Small', 8, 24.99, 'hoodie3.jpg'),\n" +
            "('T-Shirt 1', 'Short Sleeve', 'White', 'Large', 20, 14.99, 'tshirt1.webp'),\n" +
            "('Jacket 1', 'Winter', 'Green', 'XL', 15, 59.99, 'jacket1.jpeg');";
		statement.executeUpdate (sql);
    }



    static void makeOrdersTable ()
	throws SQLException
    {
	String sql = "DROP TABLE IF EXISTS ORDERS";
	statement.executeUpdate (sql);
	 sql = "CREATE TABLE ORDERS (orderId INT PRIMARY KEY AUTO_INCREMENT, userId INT, orderDate VARCHAR(100), totalPrice INT, paymentStatus VARCHAR(10))";
	statement.execute (sql);

	sql = "INSERT INTO ORDERS VALUES (1, 2, '04/20/2019', 25, 'paid')";
	statement.executeUpdate (sql);
    }

    static void makeOrderDetailTable ()
	throws SQLException
    {
	String sql = "DROP TABLE IF EXISTS ORDER_DETAIL";
	statement.executeUpdate (sql);
	 sql = "CREATE TABLE ORDER_DETAIL (odId INT PRIMARY KEY AUTO_INCREMENT, orderId INT, productId INT, productName VARCHAR(20), productQuantity INT)";
	statement.execute (sql);

	sql = "INSERT INTO ORDER_DETAIL VALUES (1, 1, 1, 'Hoodie 1', 1)";
	statement.executeUpdate (sql);
    }


}