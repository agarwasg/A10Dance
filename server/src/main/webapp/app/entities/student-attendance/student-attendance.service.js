(function() {
    'use strict';
    angular
        .module('a10DanceApp')
        .factory('StudentAttendance', StudentAttendance);

    StudentAttendance.$inject = ['$resource'];

    function StudentAttendance ($resource) {
        var resourceUrl =  'api/student-attendances/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
