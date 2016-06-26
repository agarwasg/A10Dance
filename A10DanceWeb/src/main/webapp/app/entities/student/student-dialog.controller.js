(function() {
    'use strict';

    angular
        .module('a10DanceWebApp')
        .controller('StudentDialogController', StudentDialogController);

    StudentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Student', 'Attendance', 'Subject'];

    function StudentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Student, Attendance, Subject) {
        var vm = this;

        vm.student = entity;
        vm.clear = clear;
        vm.save = save;
        vm.attendances = Attendance.query();
        vm.subjects = Subject.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.student.id !== null) {
                Student.update(vm.student, onSaveSuccess, onSaveError);
            } else {
                Student.save(vm.student, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('a10DanceWebApp:studentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
