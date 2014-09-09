'use strict';

/* Services */

var studentServices = angular.module('studentServices', ['ngResource']);

studentServices.factory('Student', ['$resource',
  function($resource){
    return $resource('http://localhost:8080/students', {}, {
      query: {method:'GET', isArray:true}
    });
  }]);