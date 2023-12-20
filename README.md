Website for selling hoodies to college representatives of different background.
Database design can be seen in the Design Document.
Database used to store data is the H2 database.
There are html pages-
products.html- for products displaying
shopping-cart.html- for cart displaying
order-summary- for order summary
Other pages such as login, adminlogin and profile pages.
All these files have there separate javascript files which utilize Angular JS library.
The product and shopping cart pages refer to the same js file called app.js which have their separate controllers.
Server is deployed in Jetty Web Server.
With all the js files calling to WebServlet file which performs databases queries for different requirements.