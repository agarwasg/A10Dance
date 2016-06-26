'use strict';

describe('Controller Tests', function() {

    describe('Subject Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSubject, MockAttendance, MockStudent, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSubject = jasmine.createSpy('MockSubject');
            MockAttendance = jasmine.createSpy('MockAttendance');
            MockStudent = jasmine.createSpy('MockStudent');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Subject': MockSubject,
                'Attendance': MockAttendance,
                'Student': MockStudent,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("SubjectDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'a10DanceWebApp:subjectUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
