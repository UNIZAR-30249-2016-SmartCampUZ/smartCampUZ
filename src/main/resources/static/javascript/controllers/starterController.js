angular.module('smartCampUZApp')

    .controller('starterCtrl', ['$scope', '$state', 'auth', 'reserve', 'report', 'userMap', 'Notification',
        function ($scope, $state, auth, reserve, report, userMap, Notification) {
        $scope.location = {id:0,name:""};
        $scope.located = false;
        // Watches to control if the user have selected a location
        $scope.$watch(function() {
            return userMap.getCurrentLocation();
        }, function () {
            $scope.location = userMap.getCurrentLocation();
            $scope.located = $scope.location.name != 0;
        });

        /* FEEDBACK MESSAGES */
        // show the error message
        var showError = function (message) {
            Notification.error('&#10008' + message);
        };

        // show the success message
        var showSuccess = function (message) {
            Notification.success('&#10004' + message);
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
            var date = reserve.getCurrentDate();
            $scope.currentMonth = date.month;
            $scope.currentDay = date.day;
            $scope.getAvailableHours($scope.currentMonth, $scope.currentDay);
        };
        /* HOUR SELECTOR SECTION */
        // Reservable hours
        $scope.reservableHours = ["00:00-01:00", "01:00-02:00", "02:00-03:00", "03:00-04:00", "04:00-05:00",
            "05:00-06:00", "06:00-07:00", "07:00-08:00", "08:00-09:00", "09:00-10:00", "10:00-11:00",
            "11:00-12:00", "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00",
            "17:00-18:00", "18:00-19:00", "19:00-20:00", "20:00-21:00", "21:00-22:00", "22:00-23:00", "23:00-00:00"];
        /* State of the reservable hours
         * 0: Disabled
         * 1: Selected
         * 2: Unselected
         */
        $scope.hoursSelected = [2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2];
        // Change the [num] reservable hour state
        $scope.setReservableHour = function (num) {
            if ($scope.hoursSelected[num] == 1) {
                $scope.hoursSelected[num] = 2;
            } else if ($scope.hoursSelected[num] == 2) {
                $scope.hoursSelected[num] = 1;
            }
        };
        // Get available hours of a [date]
        $scope.getAvailableHours = function (month, day) {
            reserve.getAvailableHours(month, day, function (hours) {
                $scope.hoursSelected = hours;
            }, showError);
        };

        /* RESERVE FORM SECTION */
        $scope.logged = auth.isAuthenticated();
        $scope.emailReserve = $scope.logged ? auth.getEmail() : "";
        $scope.descriptionReserve = "";
        // Make a reserve
        $scope.reserve = function () {
            var reserveInfo = {
                email: $scope.emailReserve,
                description: $scope.descriptionReserve,
                day: $scope.currentDay,
                month: $scope.currentMonth
            };
            reserve.reserveHours(reserveInfo, $scope.hoursSelected, function (message) {
                for (i=0; i<$scope.hoursSelected.length;i++) {
                    $scope.hoursSelected[i] = $scope.hoursSelected[i] == 1 ? 0 : $scope.hoursSelected[i];
                }
                showSuccess(message);
            }, showError);
        };
        // Watches to control if the user is authenticated
        $scope.$watch(function() {
            return auth.isAuthenticated();
        }, function () {
            $scope.logged = auth.isAuthenticated();
            $scope.emailReserve = $scope.logged ? auth.getEmail() : "";
        });
        /* REPORT FORM SECTION */
        $scope.report = function () {
            report.makeReport($scope.descriptionReport, showSuccess, showError)
        }
    }]);
