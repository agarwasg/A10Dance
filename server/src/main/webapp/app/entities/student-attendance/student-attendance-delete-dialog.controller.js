(function() {
    'use strict';

    angular
        .module('a10DanceApp')
        .controller('StudentAttendanceDeleteController',StudentAttendanceDeleteController);

    StudentAttendanceDeleteController.$inject = ['$uibModalInstance', 'entity', 'StudentAttendance'];

    function StudentAttendanceDeleteController($uibModalInstance, entity, StudentAttendance) {
        var vm = this;

        vm.studentAttendance = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StudentAttendance.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
