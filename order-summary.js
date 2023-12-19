// URL to servlet:
let url = "http://localhost:40120/order-summary";


let serverJsonData = {
    userId: null
}

let app = angular.module("myApp", []);

var cart = JSON.parse(localStorage.getItem("cart")) ;
sendServerData();

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


app.controller('myController', function($scope, CartService) {
     
    $scope.cartDisplay=function() {
      console.log("cart", JSON.parse(localStorage.getItem("cart")) )
      $scope.cart = JSON.parse(localStorage.getItem("cart")) ;

       $scope.sum=0
      cart.forEach((product) => {
        $scope.sum+=parseInt(product.price)*parseInt(product.quantity);

      });
      console.log(sum)
    };

});

function sendServerData ($scope) {
    let req = new XMLHttpRequest();
    req.addEventListener("load", requestListener);
    req.open("POST", url);
    req.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    req.send(JSON.stringify(cart));
    console.log("Sent to server: json=" + JSON.stringify(cart));
}

function requestListener () {
    let jsonObject = JSON.parse (this.responseText);
    serverJsonData.userId = jsonObject.userId;
    if (serverJsonData.userId == null) {
        println ("something went wrong");
    } else {
        println ("submitted!");
    }
    console.log (jsonObject);
}

function println (outputStr) {
    document.getElementById("output").innerHTML += outputStr + "<br>";
}

function getUserId () {
    userId = sessionStorage.getItem("userId");
    return userId;
}