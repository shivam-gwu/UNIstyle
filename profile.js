// URL to servlet:
let url = "http://localhost:40120/unistyle";

// What we will send over.
let serverData = {
    data1: null,
    data2: null,
    action: "profile"
};

let serverJsonData = {
    collegeName:"",
    email: "",  
}

var collegeName = "";
var email = "";

let app = angular.module("myApp", []);


app.controller("myController", function($scope) {
    myProfile($scope);
    setTimeout(function(){
        $scope.collegeName = serverJsonData.collegeName;
        $scope.email = serverJsonData.email;
        $scope.$apply();
    }, 1000);
    $scope.editprofile=function() {
        window.location.href = "http://localhost:40120/editprofile.html";
    };
    
});

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
    serverJsonData.collegeName = jsonObject.collegeName;
    serverJsonData.email = jsonObject.email;
    if (serverJsonData == null) {
        println ("Something went wrong");
    }    
    console.log(serverJsonData);
    console.log (jsonObject);
}


function myProfile($scope) {
    userId = sessionStorage.getItem("userId");
    serverData.data1 = userId;
    sendServerData();
}