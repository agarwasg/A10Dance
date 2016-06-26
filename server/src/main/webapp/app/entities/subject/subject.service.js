(function() {
    'use strict';
    angular
        .module('a10DanceApp')
        .factory('Subject', Subject);

    Subject.$inject = ['$resource', 'DateUtils'];

    function Subject ($resource, DateUtils) {
        var resourceUrl =  'api/subjects/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdAt = DateUtils.convertDateTimeFromServer(data.createdAt);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
