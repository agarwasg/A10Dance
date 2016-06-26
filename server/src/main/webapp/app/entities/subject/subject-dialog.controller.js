(function() {
    'use strict';

    angular
        .module('a10DanceApp')
        .controller('SubjectDialogController', SubjectDialogController);

    SubjectDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Subject', 'Student', 'User'];

    function SubjectDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Subject, Student, User) {
        var vm = this;

        vm.subject = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.students = Student.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.subject.id !== null) {
                Subject.update(vm.subject, onSaveSuccess, onSaveError);
            } else {
                Subject.save(vm.subject, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('a10DanceApp:subjectUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdAt = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
