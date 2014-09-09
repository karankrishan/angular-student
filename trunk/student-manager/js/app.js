'use strict';

/* App Module */

var studentManagerApp = angular.module('studentManagerApp', [
  'ngRoute',
  'studentControllers',
  'studentServices',
  'ui.bootstrap'
]);

studentManagerApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/students', {
        templateUrl: 'partials/student-list.html',
        controller: 'StudentListCtrl'
      }).
      otherwise({
        redirectTo: '/students'
      });
  }]);