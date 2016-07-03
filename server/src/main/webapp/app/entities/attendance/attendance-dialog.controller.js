(function() {
    'use strict';

    angular
        .module('a10DanceApp')
        .controller('AttendanceDialogController', AttendanceDialogController);

    AttendanceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Attendance', 'Subject'];

    function AttendanceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Attendance, Subject) {
        var vm = this;

        vm.attendance = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.subjects = Subject.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.attendance.id !== null) {
                Attendance.update(vm.attendance, onSaveSuccess, onSaveError);
            } else {
                Attendance.save(vm.attendance, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('a10DanceApp:attendanceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.attendanceDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
