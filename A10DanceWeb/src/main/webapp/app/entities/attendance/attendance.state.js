(function() {
    'use strict';

    angular
        .module('a10DanceWebApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('attendance', {
            parent: 'entity',
            url: '/attendance',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'a10DanceWebApp.attendance.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/attendance/attendances.html',
                    controller: 'AttendanceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('attendance');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('attendance-detail', {
            parent: 'entity',
            url: '/attendance/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'a10DanceWebApp.attendance.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/attendance/attendance-detail.html',
                    controller: 'AttendanceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('attendance');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Attendance', function($stateParams, Attendance) {
                    return Attendance.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('attendance.new', {
            parent: 'attendance',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/attendance/attendance-dialog.html',
                    controller: 'AttendanceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                attendanceDate: null,
                                description: null,
                                isPresent: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('attendance', null, { reload: true });
                }, function() {
                    $state.go('attendance');
                });
            }]
        })
        .state('attendance.edit', {
            parent: 'attendance',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/attendance/attendance-dialog.html',
                    controller: 'AttendanceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Attendance', function(Attendance) {
                            return Attendance.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('attendance', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('attendance.delete', {
            parent: 'attendance',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/attendance/attendance-delete-dialog.html',
                    controller: 'AttendanceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Attendance', function(Attendance) {
                            return Attendance.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('attendance', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
