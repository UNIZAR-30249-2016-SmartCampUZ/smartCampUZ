angular.module('smartCampUZApp')

    .controller('starterCtrl', ['$scope', '$state', 'auth', 'reserve', function ($scope, $state, auth, reserve) {
        /* FEEDBACK MESSAGES */
        // feedback handling variables
        $scope.error = false;
        $scope.success = false;
        $scope.successMsg = "";
        $scope.errorMsg = "";

        // hide the error mensage
        $scope.hideError = function () {
            $scope.errorMsg = "";
            $scope.error = false;
        };
        // show the error mensage
        var showError = function (error) {
            $scope.errorMsg = error;
            $scope.error = true;
        };

        // show the success mensage
        var showSuccess = function (message) {
            $scope.successMsg = message;
            $scope.success = true;
        };

        // hide the success mensage
        $scope.hideSuccess = function () {
            $scope.success = false;
            $scope.successMsg = "";
        };

        /* CALENDAR SECTION*/
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
            $scope.getAvailableHours($scope.currentMonth, $scope.currentDay);
        };
        // [next] plus/minus the current month % 12
        $scope.setMonth = function(next) {
            $scope.currentMonth += next;
            if ($scope.currentMonth > $scope.calendarMonths.length -1) {$scope.currentMonth = 0}
            if ($scope.currentMonth < 0) {$scope.currentMonth = $scope.calendarMonths.length -1}
        };
        // Get current date
        $scope.getDate = function () {
            reserve.getCurrentDate(function (date) {
                $scope.currentMonth = date.month;
                $scope.currentDay = date.day;
                $scope.getAvailableHours($scope.currentMonth, $scope.currentDay);
            }, showError);
        };
        /* HOUR SELECTOR SECTION */
        // Reservable hours
        $scope.reservableHours = ["8:00-9:00", "9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00",
            "14:00-15:00", "15:00-16:00", "16:00-17:00", "17:00-18:00", "18:00-19:00", "19:00-20:00", "20:00-21:00"];
        /* State of the reservable hours
         * 0: Disabled
         * 1: Selected
         * 2: Unselected
         */
        $scope.hoursSelected = [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2];
        $scope.disableHours = function () {
            $("#hourUL ul li.disabled").off('click');
        };
        // Change the [num] reservable hour state
        $scope.setReservableHour = function (num) {
            if ($scope.hoursSelected[num] == 1) {
                $scope.hoursSelected[num] = 2;
            } else if ($scope.hoursSelected[num] = 2) {
                $scope.hoursSelected[num] = 1;
            }
        };
        // Get available hours of a [date]
        $scope.getAvailableHours = function (month, day) {
            reserve.getAvailableHours(month, day, function (hours) {
                $scope.hoursSelected = hours;
                $scope.disableHours();
            }, showError);
        };

        /* RESERVE FORM SECTION */
        $scope.logged = auth.isAuthenticated();
        $scope.userNameReserve = $scope.logged ? auth.getUserName() : "";
        $scope.emailReserve = $scope.logged ? auth.getEmail() : "";
        $scope.descriptionReserve = "";
        // Make a reserve
        $scope.reserve = function () {
            var reserveInfo = {
                name: $scope.userNameReserve,
                email: $scope.emailReserve,
                description: $scope.descriptionReserve
            };
            reserve.reserveHours(reserveInfo, $scope.hoursSelected, function (data) {
                for (i++; i<$scope.hoursSelected.length;i++) {
                    $scope.hoursSelected[i] = $scope.hoursSelected[i] == 1 ? 0 : $scope.hoursSelected[i];
                }
                showSuccess(data);
            }, showError);
        };
        // Watches to control input variables length
        $scope.$watch(function() {
            return auth.isAuthenticated();
        }, function () {
            $scope.logged = auth.isAuthenticated();
            $scope.userNameReserve = $scope.logged ? auth.getUserName() : "";
            $scope.emailReserve = $scope.logged ? auth.getEmail() : "";
        });

    }]);
