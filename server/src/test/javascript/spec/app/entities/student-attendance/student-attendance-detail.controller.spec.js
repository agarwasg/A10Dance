'use strict';

describe('Controller Tests', function() {

    describe('StudentAttendance Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockStudentAttendance, MockStudent, MockAttendance;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockStudentAttendance = jasmine.createSpy('MockStudentAttendance');
            MockStudent = jasmine.createSpy('MockStudent');
            MockAttendance = jasmine.createSpy('MockAttendance');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'StudentAttendance': MockStudentAttendance,
                'Student': MockStudent,
                'Attendance': MockAttendance
            };
            createController = function() {
                $injector.get('$controller')("StudentAttendanceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'a10DanceApp:studentAttendanceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
