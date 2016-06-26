(function () {
    'use strict';

    angular
        .module('a10DanceWebApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
