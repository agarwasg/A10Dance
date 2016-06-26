(function() {
    'use strict';

    angular
        .module('a10DanceWebApp')
        .controller('AttendanceDeleteController',AttendanceDeleteController);

    AttendanceDeleteController.$inject = ['$uibModalInstance', 'entity', 'Attendance'];

    function AttendanceDeleteController($uibModalInstance, entity, Attendance) {
        var vm = this;

        vm.attendance = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Attendance.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
