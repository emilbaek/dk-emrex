angular.module('api', [])
    .service('apiService', function ($http, $q, $sce, helperService) {
        
        var getElmoEverthing = function(){
        	var deferred = $q.defer();
            $http.get('/ncp/api/fullelmo').success(function (response) {
                var elmo = { 
                	reports : helperService.fixReports(response.elmo.report), 
                	learner : response.elmo.learner
                }; 
                deferred.resolve(elmo);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }; 
        
        var getElmoLearner = function(){
        	var deferred = $q.defer();
            $http.get('/ncp/api/fullelmo').success(function (response) {
            	deferred.resolve(response.elmo.learner);
            }).error(function(error){
            	deferred.reject(error);
            });
            return deferred.promise;
        };

        var getElmoSelected = function(courses){
            var deferred = $q.defer();
            $http({
                url: '/ncp/api/elmo/',
                method: 'GET',
                params: {courses: courses}
            }).success(function (response) {
                deferred.resolve(helperService.fixReports(response.elmo.report));
            }).error(function (error){
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getSubmitHtml = function(courses) {
            var deferred = $q.defer();
            $http({
                url: 'review',
                method: 'GET',
                params: {courses: courses}
            }).success(function (data) {
                deferred.resolve($sce.trustAsHtml(data));
            }).error(function (error){
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getAbortHtml = function(courses) {
            var deferred = $q.defer();
            $http({
                url: 'abort',
                method: 'GET'
            }).success(function (data) {
                deferred.resolve($sce.trustAsHtml(data));
            }).error(function (error){
                deferred.reject(error);
            });
            return deferred.promise;
        };



        return {
        	getElmoLearner: getElmoLearner,
            getElmoEverthing : getElmoEverthing,
    		getElmoSelected: getElmoSelected,
            getSubmitHtml : getSubmitHtml,
            getAbortHtml : getAbortHtml
        };

    }
);
