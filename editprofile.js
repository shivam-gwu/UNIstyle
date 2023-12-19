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

let app = angular.module("myApp", []);


app.controller("myController", function($scope) {
    userId = getuserId();
    serverData.data1 = userId;
    $scope.collegeName=function () {
        serverData.data2 = $scope.user.collegeName;
        serverData.action = "collegeName";
        sendserverData();
    };
    $scope.email=function () {
        serverData.data2 = $scope.user.email;
        serverData.action = "email";
        sendserverData();
    };
});


function sendserverData ($scope) {
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
        println ("something went wrong");
    } else {
        println ("updated");
    }
    console.log (jsonObject);
}

function println (outputStr) {
    document.getElementById("output").innerHTML += outputStr + "<br>";
}

function getuserId () {
    userId = sessionStorage.getItem("userId");
    return userId;
}