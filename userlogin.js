// URL to servlet:
let url = "http://localhost:40120/unistyle";

// What we will send over.
let serverData = {
    data1: null,
    data2: null,
    action: null
};

let serverJsonData = {
    userId: null
}

angular.module("myApp",[])
    .controller("myController", function($scope) {
    $scope.submit=function () {
        if (checkInput($scope)) {
            serverData.data1 = $scope.user.username;
            serverData.data2 = $scope.user.password;
            serverData.action = "login";
            sendServerData();
        } else {
             println ("invalid input");
        }  
    };
    $scope.signup=function () {
        window.location.href = "http://localhost:40120/signup.html";
    }
});

function checkInput ($scope) {

    // Define regular expressions for username and password criteria
      const usernameRegex = /^[a-zA-Z0-9_]{3,20}$/; // Allows alphanumeric characters and underscores, 3 to 20 characters
      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/; // Requires at least 8 characters, one lowercase, one uppercase, and one digit

      // Test the input against the regular expressions
      const isUsernameValid = usernameRegex.test($scope.user.username);
      const isPasswordValid = passwordRegex.test($scope.user.password);

      // Return true if both username and password are valid
      return isUsernameValid && isPasswordValid;
}
function sendServerData ($scope) {
    let req = new XMLHttpRequest();
    req.addEventListener("load", requestListener);
    req.open("POST", url);
    req.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    req.send(JSON.stringify(serverData));
    console.log("Sent to server: json=" + JSON.stringify(serverData));
}

function requestListener () {
    let jsonObject = JSON.parse (this.responseText);
    serverJsonData.userId = jsonObject.userId;
    if (serverJsonData.userId == null) {
        println ("Incorrect username and password try again");
    } else {
        setUserId(serverJsonData.userId);
        window.location.href = "http://localhost:40120/products.html";
    }
    console.log (jsonObject);
}

function println (outputStr) {
    document.getElementById("output").innerHTML += outputStr + "<br>";
}

function setUserId (userId) {
    userID = userId.toString();
    sessionStorage.setItem("userId", userID);
}