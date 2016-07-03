(function() {
    'use strict';

    angular
        .module('a10DanceApp')
        .controller('StudentAttendanceDialogController', StudentAttendanceDialogController);

    StudentAttendanceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'StudentAttendance', 'Student', 'Attendance'];

    function StudentAttendanceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, StudentAttendance, Student, Attendance) {
        var vm = this;

        vm.studentAttendance = entity;
        vm.clear = clear;
        vm.save = save;
        vm.students = Student.query({filter: 'studentattendance-is-null'});
        $q.all([vm.studentAttendance.$promise, vm.students.$promise]).then(function() {
            if (!vm.studentAttendance.student || !vm.studentAttendance.student.id) {
                return $q.reject();
            }
            return Student.get({id : vm.studentAttendance.student.id}).$promise;
        }).then(function(student) {
            vm.students.push(student);
        });
        vm.attendances = Attendance.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.studentAttendance.id !== null) {
                StudentAttendance.update(vm.studentAttendance, onSaveSuccess, onSaveError);
            } else {
                StudentAttendance.save(vm.studentAttendance, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('a10DanceApp:studentAttendanceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
