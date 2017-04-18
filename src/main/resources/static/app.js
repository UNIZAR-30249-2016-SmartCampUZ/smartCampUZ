angular.module('smartCampUZApp', ['ui.router', 'base64', 'angular-jwt', 'ui-notification'])

    .config(function(NotificationProvider) {
        NotificationProvider.setOptions({
            positionX: 'center',
            maxCount: 4
        });
    })

    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider

            //starter screen
            .state('starter', {
                url: "/starter",
                templateUrl: "templates/starter.html",
                controller: "starterCtrl",
                onEnter: function ($state, auth) {
                    if (auth.isAuthenticated() && (auth.getType() == 'admin')) {
                        $state.go('admin');
                    } else if (auth.isAuthenticated() && (auth.getType() == 'maintenance')) {
                        $state.go('maintenance');
                    }
                }
            })

            //admin screen
            .state('admin', {
                url: "/admin",
                templateUrl: "templates/admin.html",
                controller: "adminCtrl",
                onEnter: function ($state, auth) {
                    if (!auth.isAuthenticated() || auth.isAuthenticated() && (auth.getType() == 'professor')) {
                        $state.go('starter');
                    } else if (auth.isAuthenticated() && (auth.getType() == 'maintenance')) {
                        $state.go('maintenance');
                    }
                }
            })

            //maintenance screen
            .state('maintenance', {
                url: "/maintenance",
                templateUrl: "templates/maintenance.html",
                controller: "maintenanceCtrl",
                onEnter: function ($state, auth) {
                    if (!auth.isAuthenticated() || auth.isAuthenticated() && (auth.getType() == 'professor')) {
                        $state.go('starter');
                    } else if (auth.isAuthenticated() && (auth.getType() == 'admin')) {
                        $state.go('admin');
                    }
                }
            });

        $urlRouterProvider.otherwise('starter');
    });
