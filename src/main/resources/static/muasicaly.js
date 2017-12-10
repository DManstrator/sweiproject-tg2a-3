var app = angular.module('ActivityMeterApp', ['ui.bootstrap']);
    
function loadActivities ($scope, $http){
		$http({
       		 method : 'GET',
       		 /*
       		 url: (window.location.hostname === 'localhost' ?
     				'http://localhost:8080/activity' :
                    'https://activityexample.herokuapp.com/activity')
             */
             url: 'activity'
                    
    		}).then(function (response) {
     			 $scope.activities = response.data;
  		});
  	}
	
function filterActivities ($scope, $http){
		var tag = $scope.tag.trim();
		tag = tag.replace('#', '');
		if (tag.length > 0)  {
			$http({
				 method : 'GET',
				 url: 'activity/filter/' + tag
	
				}).then(function (response) {
					 $scope.activities = response.data;
			});
		}  else  {
			loadActivities($scope, $http);
		}
	}	
  	
app.controller('ActivityCtrl', function ($scope, $http, $dialog) {
  	
  	loadActivities($scope, $http);
  
		  
  	var addDialogOptions = {
    	controller: 'AddActivityCtrl',
    	templateUrl: './activityAdd.html'
  	};
  	
  	$scope.add = function(activity){
    	$dialog.dialog(angular.extend(addDialogOptions, {})).open().then(function (){
    	    loadActivities($scope, $http);
        }) ;
  	};
	
	$scope.filter = function()  {
  		filterActivities($scope, $http);
  	};
  	
  	var editDialogOptions = {
	    controller: 'EditActivityCtrl',
	    templateUrl: './activityEdit.html',
	};
  	$scope.edit = function(activity){
   	 	var activityToEdit = activity;
    	$dialog.dialog(angular.extend(editDialogOptions, {resolve: {activity: angular.copy(activityToEdit)}})).open().then(function (){
    	    loadActivities($scope, $http);
        }) ;
  	};
		
	$scope.delete = function(activity) {
		var deleteRequest = {
			method : 'DELETE',
			url: 'activity/' + activity.id
		};
		
  		$http(deleteRequest).then(function() {
			loadActivities($scope, $http);
		});
  		//todo handle error
	};
});
app.controller('AddActivityCtrl', function($scope, $http, dialog){
  
  	$scope.save = function(Activity) {
  		var postRequest = {
    	method : 'POST',
       	url: 'activity' ,
       	data: {
  				text:  $scope.activity.text,
  				title: $scope.activity.title,
  				tags:  $scope.activity.tags.split(/(\s+)/).filter( function(e) { return e.trim().length > 0; } )
			  }
		}  
		
  		$http(postRequest).then(function (response) {
  		    $scope.activities = response.data;
  		}).then(function () {
  			$scope.close();
  		});
  	};
  
  	$scope.close = function(){;
    	dialog.close(undefined);
  	};
});
app.controller('EditActivityCtrl', function ($scope, $http, activity, dialog) {
  
	$scope.activity = activity;
	$scope.activity.tags = $scope.activity.tagsAsString;
  	$scope.save = function($activity) {
  	    var putRequest = {
    	method : 'PUT',
       	url: 'activity/' + $scope.activity.id,
       	data: {
  				text:  $scope.activity.text,
  				title: $scope.activity.title,
  				tags:  $scope.activity.tags.split(/(\s+)/).filter( function(e) { return e.trim().length > 0; } )
			  }
		}  
		
  		$http(putRequest).then(function (response) {
  		    $scope.activities = response.data;
  		}).then(function () {
			//todo handle error
			$scope.close();
		});
  	};
  
  	$scope.close = function(){
  		loadActivities($scope, $http);
    	dialog.close();
  	};
});