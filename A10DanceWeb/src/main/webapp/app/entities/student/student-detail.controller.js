(function() {
    'use strict';

    angular
        .module('a10DanceWebApp')
        .controller('StudentDetailController', StudentDetailController);

    StudentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Student', 'Attendance', 'Subject'];

    function StudentDetailController($scope, $rootScope, $stateParams, entity, Student, Attendance, Subject) {
        var vm = this;

        vm.student = entity;

        var unsubscribe = $rootScope.$on('a10DanceWebApp:studentUpdate', function(event, result) {
            vm.student = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
