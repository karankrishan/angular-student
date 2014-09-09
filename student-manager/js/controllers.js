'use strict';

/* Controllers */
/**
 * This class became angular soup very quickly. This definitely exposed my weakness with using/understanding
 * the angular framework, however, I got 'something' working.  From here, I would want to make this
 * controller, much more lean by moving $http calls to be Services, genericising modal pop-up logic, and 
 * getting a better handle on $scope.  
 * 
 * Revisit:
 * @link http://www.technofattie.com/2014/03/21/five-guidelines-for-avoiding-scope-soup-in-angular.html
 * @link http://joshdmiller.github.io/ng-boilerplate/#/home
 */
var studentControllers = angular.module('studentControllers', []);

studentControllers.controller('StudentListCtrl', [
        '$rootScope',
		'$scope',
		'Student',
		'$modal',
		'$log',
		'$http',
		function($rootScope, $scope, Student, $modal, $log, $http) {
			$scope.students = Student.query();
			$scope.orderProp = 'id';
			$scope.myOrder = true;

			$scope.open = function(size, user) {

				var modalInstance = $modal.open({
					templateUrl : 'partials/modal-delete-student.html',
					controller : ModalInstanceCtrl,
					size : size,
					resolve : {
						selectedStudent : function() {
							return user;
						}
					}
				});

				modalInstance.result.then(function(selectedItem) {
					$scope.purge(selectedItem);
				}, function() {
					$log.info('Modal dismissed at: ' + new Date());
				});
			};

			//Event listener to refresh page after new student is added/updated
			$rootScope.$on('refreshEvent', function(event, args) {$scope.reload();});

			
			
			/////////////////////////////////////////////
			//Open modal to update a student
			/////////////////////////////////////////////			
			$scope.openStudentUpdateForm = function(size, user) {
				
				$scope.student = {
					id : user.id,
					firstName : user.firstName,
					middleName : user.middleName,
					lastName : user.lastName,
					address : user.address,
					address2 : user.address2,
					city : user.city,
					state : user.state,
					zipCode : user.zipCode
				};
	
				$modal.open({
					templateUrl : 'partials/modal-update-student.html',
					backdrop : true,
					windowClass : 'modal',
					controller : function($scope, $modalInstance, $log, student) {
						$scope.student = student;
						$scope.submit = function() {
							$http["put"]('http://localhost:8080/students/' + student.id, student).success(
									function(data, status) {
										$scope.status = status;
										$scope.$emit('refreshEvent', data);
									}).error(function(data, status) {
							});
							$modalInstance.dismiss('cancel');
						}
						$scope.cancel = function() {
							$modalInstance.dismiss('cancel');
						};
					},
					resolve : {
						student : function() {
							return $scope.student;
						}
					}
				});

			};

			/////////////////////////////////////////////
			//Open modal to add a new student
			/////////////////////////////////////////////	
			$scope.openNewStudentForm = function(size) {

				$scope.student = {
					firstName : null,
					middleName : null,
					lastName : null,
					address : null,
					address2 : null,
					city : null,
					state : null,
					zipCode : null
				};

				$modal.open({
					templateUrl : 'partials/modal-new-student.html',
					backdrop : true,
					windowClass : 'modal',
					controller : function($scope, $modalInstance, $log, student) {
						$scope.student = student;
						$scope.submit = function() {
							$http.post('http://localhost:8080/students', student).success(
									function(data, status, headers, config) {
										$log.info('Created new resource at: ' + headers('Location'));
										$scope.$emit('refreshEvent', data);
									}).error(function(data, status) {
								alert("Failed to create student. " + data);
							});
							$modalInstance.dismiss('cancel');
						}
						$scope.cancel = function() {
							$modalInstance.dismiss('cancel');
						};
					},
					resolve : {
						student : function() {
							return $scope.student;
						}
					}
				});

			};
			
			
			/////////////////////////////////////////////
			//Function to remove student from database
			/////////////////////////////////////////////	
			$scope.purge = function(student) {
				$http["delete"]('http://localhost:8080/students/' + student.id).success(function(data, status) {
					$scope.status = status;
					$scope.students.splice($scope.students.indexOf(student), 1);
				}).error(function(data, status) {
					alert("Refresh table. User already deleted.");
				});
			};

			
			/////////////////////////////////////////////
			//Function update the sorting on the ui table
			/////////////////////////////////////////////	
			$scope.sortColumn = function(column) {
				if (column == $scope.orderProp) {
					if ($scope.myOrder == true) {
						$scope.myOrder = false;
					} else {
						$scope.myOrder = true;
					}
				}

				$scope.orderProp = column;
			};
			
			/////////////////////////////////////////////
			//Reload table from database
			/////////////////////////////////////////////	
			$scope.reload = function() {
				$scope.students = Student.query();
			};

		} ]);

///////////////////////////////////////////////////////////
//Controller to show module. Currently only used for delete
//////////////////////////////////////////////////////////
var ModalInstanceCtrl = function($scope, $modalInstance, selectedStudent) {

	$scope.selectedStudent = selectedStudent;

	$scope.ok = function() {
		$modalInstance.close($scope.selectedStudent);
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};
};