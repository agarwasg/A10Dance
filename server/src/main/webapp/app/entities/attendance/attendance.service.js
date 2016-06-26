(function() {
    'use strict';
    angular
        .module('a10DanceApp')
        .factory('Attendance', Attendance);

    Attendance.$inject = ['$resource', 'DateUtils'];

    function Attendance ($resource, DateUtils) {
        var resourceUrl =  'api/attendances/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.attendanceDate = DateUtils.convertDateTimeFromServer(data.attendanceDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
