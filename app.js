// app.js
// URL to servlet:
let url = "http://localhost:40120/unistyle";

var cart = [] ;

// What we will send over.
let serverData = {
    data1: null,
    data2: null,
    action: null
};

let products = [];

var orderId

var app = angular.module('productApp', ['ngRoute']);

app.config(function($routeProvider) {
  $routeProvider
    .when('/', {
      templateUrl: 'products.html',
      controller: 'ProductController'
    })
    .when('/cart', {
      templateUrl: 'shopping-cart.html',
      controller: 'CartController'
    })
    .otherwise({
      redirectTo: '/'
    });
});

app.factory('CartService', function() {
  cart = JSON.parse(localStorage.getItem("cart")) ? JSON.parse(localStorage.getItem("cart")) : [] ;
  return {
    getCart: function() {
      return cart;
    },
    addToCart: function(product,op) {
      if( product.quantity >=1){

          cart = cart.map((item) => {
            if(item.id == product.id ){
              if(op=='+'){
               item.quantity+=1;
          
              }
              else{
                item.quantity-=1;
                
              }
            }
            return item;
          })

      }
      else{
        product.quantity = 1;
        cart.push(product);
      }
     console.log(cart)
     console.log(product, "product"  )
     localStorage.setItem("cart", JSON.stringify(cart));
    }
 };
});

app.controller('ProductController', function($scope, $http, CartService) {
  // Your existing product controller code
  $scope.addToCart = function(product,op) {

     CartService.addToCart(product,op);
  };

  // Function to load products 
  $scope.loadProducts = function() {
    // This function could be used to fetch data from the server
    serverData.action= "getProducts";
    sendServerData($scope);
  };
    
  $scope.next=function() {
      window.location.href = "http://localhost:40120/shopping-cart.html";
  };

});

app.controller('CartController', function($scope, CartService) {
    $scope.cartAdd=function() {
      console.log("cart", JSON.parse(localStorage.getItem("cart")) )
      $scope.cart = JSON.parse(localStorage.getItem("cart")) ;

    };

    $scope.addToCart = function(product,op) {
       CartService.addToCart(product,op);

    };

   $scope.next=function() {
      serverData.data1=getUserId();
      sum=0
        
      cart.forEach((product) => {
        sum+=parseInt(product.price)*parseInt(product.quantity);

      });

      serverData.data2=sum;
      serverData.action= "placeOrder";
      
      sendOrderData($scope);

  };
  
 
});

function summaryPage() {
     
      window.location.href = "http://localhost:40120/order-summary.html";
  };

function sendOrderData ($scope) {
    let req = new XMLHttpRequest();
    req.addEventListener("load", requestListener);
    req.open("POST", url);
    req.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    req.send(JSON.stringify(serverData));
    console.log("Sent to server: json=" + JSON.stringify(serverData));
    function requestListener() {
        if (req.status >= 200 && req.status < 300) {
            console.log("Successful response:", req.responseText);
            let jsonString = req.responseText;
            
            try {
                console.log();
                let jsonObject = JSON.parse(this.responseText);
                println("Order placed");
                println("Order Id:"+jsonObject.orderId);
                orderId= jsonObject.orderId;
                summaryPage();
                          
            } catch (error) {
                console.error("Error parsing JSON:", error);
            }
        } else {
            console.error("Request failed with status:", req.status, req.statusText);
        }
    }
}

function sendServerData ($scope) {
    let req = new XMLHttpRequest();
    req.addEventListener("load", requestListener);
    req.open("POST", url);
    req.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    req.send(JSON.stringify(serverData));
    console.log("Sent to server: json=" + JSON.stringify(serverData));
    function requestListener() {
        if (req.status >= 200 && req.status < 300) {
            console.log("Successful response:", req.responseText);
            let jsonString = req.responseText;
            
            try {
                console.log(this.responseText);
                jsonString = this.responseText.replace(/,(\s*}]|\s*]$)/, '$1');
                console.log(jsonString);
                let jsonObject = JSON.parse(jsonString);
                $scope.products = jsonObject;
                console.log($scope.products[0]);
            } catch (error) {
                console.error("Error parsing JSON:", error);
            }
        } else {
            console.error("Request failed with status:", req.status, req.statusText);
        }
    }
}


function getUserId () {
    userId = sessionStorage.getItem("userId");
    return userId;
}

function println (outputStr) {
    document.getElementById("output").innerHTML += outputStr + "<br>";
}