let app = angular.module("myApp", []);


app.controller("myController", function($scope) {
    $scope.submit=function () {
        sessionStorage.setItem("userId", null);
        window.location.href = "http://localhost:40120/userlogin.html";
    };
});
