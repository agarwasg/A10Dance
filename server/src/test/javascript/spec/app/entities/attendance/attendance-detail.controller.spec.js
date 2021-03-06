'use strict';

describe('Controller Tests', function() {

    describe('Attendance Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAttendance, MockSubject;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAttendance = jasmine.createSpy('MockAttendance');
            MockSubject = jasmine.createSpy('MockSubject');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Attendance': MockAttendance,
                'Subject': MockSubject
            };
            createController = function() {
                $injector.get('$controller')("AttendanceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'a10DanceApp:attendanceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
