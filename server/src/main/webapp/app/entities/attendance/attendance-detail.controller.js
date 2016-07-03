(function() {
    'use strict';

    angular
        .module('a10DanceApp')
        .controller('AttendanceDetailController', AttendanceDetailController);

    AttendanceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Attendance', 'Subject'];

    function AttendanceDetailController($scope, $rootScope, $stateParams, entity, Attendance, Subject) {
        var vm = this;

        vm.attendance = entity;

        var unsubscribe = $rootScope.$on('a10DanceApp:attendanceUpdate', function(event, result) {
            vm.attendance = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
