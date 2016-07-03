(function() {
    'use strict';

    angular
        .module('a10DanceApp')
        .controller('StudentAttendanceDetailController', StudentAttendanceDetailController);

    StudentAttendanceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'StudentAttendance', 'Student', 'Attendance'];

    function StudentAttendanceDetailController($scope, $rootScope, $stateParams, entity, StudentAttendance, Student, Attendance) {
        var vm = this;

        vm.studentAttendance = entity;

        var unsubscribe = $rootScope.$on('a10DanceApp:studentAttendanceUpdate', function(event, result) {
            vm.studentAttendance = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
