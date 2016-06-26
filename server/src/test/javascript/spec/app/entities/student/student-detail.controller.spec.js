'use strict';

describe('Controller Tests', function() {

    describe('Student Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockStudent, MockSubject;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockStudent = jasmine.createSpy('MockStudent');
            MockSubject = jasmine.createSpy('MockSubject');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Student': MockStudent,
                'Subject': MockSubject
            };
            createController = function() {
                $injector.get('$controller')("StudentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'a10DanceApp:studentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
