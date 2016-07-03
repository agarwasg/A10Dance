(function() {
    'use strict';

    angular
        .module('a10DanceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('student-attendance', {
            parent: 'entity',
            url: '/student-attendance',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'a10DanceApp.studentAttendance.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/student-attendance/student-attendances.html',
                    controller: 'StudentAttendanceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('studentAttendance');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('student-attendance-detail', {
            parent: 'entity',
            url: '/student-attendance/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'a10DanceApp.studentAttendance.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/student-attendance/student-attendance-detail.html',
                    controller: 'StudentAttendanceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('studentAttendance');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StudentAttendance', function($stateParams, StudentAttendance) {
                    return StudentAttendance.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('student-attendance.new', {
            parent: 'student-attendance',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/student-attendance/student-attendance-dialog.html',
                    controller: 'StudentAttendanceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                isPresent: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('student-attendance', null, { reload: true });
                }, function() {
                    $state.go('student-attendance');
                });
            }]
        })
        .state('student-attendance.edit', {
            parent: 'student-attendance',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/student-attendance/student-attendance-dialog.html',
                    controller: 'StudentAttendanceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StudentAttendance', function(StudentAttendance) {
                            return StudentAttendance.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('student-attendance', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('student-attendance.delete', {
            parent: 'student-attendance',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/student-attendance/student-attendance-delete-dialog.html',
                    controller: 'StudentAttendanceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StudentAttendance', function(StudentAttendance) {
                            return StudentAttendance.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('student-attendance', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
