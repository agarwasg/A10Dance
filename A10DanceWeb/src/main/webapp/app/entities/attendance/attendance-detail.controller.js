(function() {
    'use strict';

    angular
        .module('a10DanceWebApp')
        .controller('AttendanceDetailController', AttendanceDetailController);

    AttendanceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Attendance', 'Subject', 'Student'];

    function AttendanceDetailController($scope, $rootScope, $stateParams, entity, Attendance, Subject, Student) {
        var vm = this;

        vm.attendance = entity;

        var unsubscribe = $rootScope.$on('a10DanceWebApp:attendanceUpdate', function(event, result) {
            vm.attendance = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
