angular.module('firebase.config', [])
  .constant('FBURL', 'https://a10dance.firebaseio.com')
  .constant('SIMPLE_LOGIN_PROVIDERS', ['password','google'])

  .constant('loginRedirectPath', '/login');
