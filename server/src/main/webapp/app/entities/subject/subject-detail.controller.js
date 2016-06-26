(function() {
    'use strict';

    angular
        .module('a10DanceApp')
        .controller('SubjectDetailController', SubjectDetailController);

    SubjectDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Subject', 'Student', 'User'];

    function SubjectDetailController($scope, $rootScope, $stateParams, entity, Subject, Student, User) {
        var vm = this;

        vm.subject = entity;

        var unsubscribe = $rootScope.$on('a10DanceApp:subjectUpdate', function(event, result) {
            vm.subject = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
