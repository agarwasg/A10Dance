(function() {
    'use strict';

    angular
        .module('a10DanceApp')
        .controller('StudentDetailController', StudentDetailController);

    StudentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Student', 'Subject'];

    function StudentDetailController($scope, $rootScope, $stateParams, entity, Student, Subject) {
        var vm = this;

        vm.student = entity;

        var unsubscribe = $rootScope.$on('a10DanceApp:studentUpdate', function(event, result) {
            vm.student = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
