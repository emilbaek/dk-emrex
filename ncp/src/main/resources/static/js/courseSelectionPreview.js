angular.module('courseSelection')
    .controller('courseSelectionPreviewCtrl', function ($scope, $sce, $http, selectedCoursesService, apiService, helperService) {
        apiService.getSubmitHtml(selectedCoursesService.selectedCourseIds).then(function (html) {
            $scope.review = html;
        });

        apiService.getAbortHtml().then(function (html) {
            $scope.abort = html;
        });

        $scope.numberOfCourses = 0;
        $scope.totalEcts = 0;

        apiService.getElmoSelected(selectedCoursesService.selectedCourseIds).then(function (data) {

            var reports = helperService.calculateAndFilter(data.reports);
            angular.forEach(reports, function(report){
                $scope.numberOfCourses += report.numberOfCourses;
                $scope.totalEcts += report.numberOfEcts;
            });
            $scope.reports = reports;
            $scope.learner = data.learner;
        });

        // there are learning opportunitites in report
        $scope.courseNumberFilter = function(report) {
            return (report.learningOpportunitySpecification);
        };

    })
;
