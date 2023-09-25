app.controller('OrdersController', function ($scope, $http, $translate, $rootScope, $location, $routeParams) {
    var Url = "http://localhost:8080";
    var order = [];
    if ($routeParams.userId) {
        $http.post(Url + '/get-my-order/' + $routeParams.userId)
            .then(function (response) {
                // Dữ liệu trả về từ API sẽ nằm trong response.data
                var orders = response.data;
                $scope.orders = orders;
                alert(orders)
                console.log(orders);
            })
            .catch(function (error) {
                console.log(error);
            });

    }

});