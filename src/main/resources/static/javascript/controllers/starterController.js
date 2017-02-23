angular.module('smartCampUZApp')

    .controller('starterCtrl', ['$scope', '$state', 'auth', function ($scope, $state, auth) {
        $scope.calendarWeekDays = ["Lun", "Mar", "Mié", "Jue", "Vie", "Sab", "Dom"];    // calendar week days
        $scope.currentMonth = 0;    //current month selected
        $scope.currentDay = 0;  //current day selected
        $scope.calendarMonths = [   // An array with all calendar info of 2017
            {month: "Enero", days: 31, initDays: 6},
            {month: "Febrero", days: 28, initDays: 2},
            {month: "Marzo", days: 31, initDays: 6},
            {month: "Abril", days: 30, initDays: 5},
            {month: "Mayo", days: 31, initDays: 0},
            {month: "Junio", days: 30, initDays: 3},
            {month: "Julio", days: 31, initDays: 5},
            {month: "Agosto", days: 31, initDays: 1},
            {month: "Septiembre", days: 30, initDays: 4},
            {month: "Octubre", days: 31, initDays: 6},
            {month: "Noviembre", days: 30, initDays: 2},
            {month: "Diciembre", days: 31, initDays: 4}
        ];
        // get the offset days of a month
        $scope.getOffsetDays = function() {
            return new Array($scope.calendarMonths[$scope.currentMonth].initDays);
        };
        //get an array with the length of the current month
        $scope.getDays = function() {
            return new Array($scope.calendarMonths[$scope.currentMonth].days);
        };
        // active [day] day of the month
        $scope.setActive = function(day) {
            $scope.currentDay = day;
        };
        // [next] plus/minus the current month % 12
        $scope.setMonth = function(next) {
            $scope.currentMonth += next;
            if ($scope.currentMonth > $scope.calendarMonths.length -1) {$scope.currentMonth = 0}
            if ($scope.currentMonth < 0) {$scope.currentMonth = $scope.calendarMonths.length -1}
        }
    }]);
