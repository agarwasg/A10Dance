(function() {
    'use strict';

    angular
        .module('a10DanceWebApp')
        .controller('SubjectDetailController', SubjectDetailController);

    SubjectDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Subject', 'Attendance', 'Student', 'User'];

    function SubjectDetailController($scope, $rootScope, $stateParams, entity, Subject, Attendance, Student, User) {
        var vm = this;

        vm.subject = entity;

        var unsubscribe = $rootScope.$on('a10DanceWebApp:subjectUpdate', function(event, result) {
            vm.subject = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
