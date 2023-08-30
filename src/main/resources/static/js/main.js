app.config(function ($translateProvider, $routeProvider) {
	$translateProvider.useStaticFilesLoader({
		prefix: '/json/', // Thay đổi đường dẫn này cho phù hợp
		suffix: '.json'
	});
	$routeProvider.when('/', {
		templateUrl: "/ngview/home.html",
		controller: 'HomeController'
	}).when('/search', {
		templateUrl: "/ngview/search.html",
		controller: 'SearchController'
	}).when('/recommend', {
		templateUrl: "/ngview/recommend.html",
		controller: 'RecommendController'
	}).when('/message', {
		templateUrl: "/ngview/message.html",
		controller: 'MessController'
	}).when('/message/:otherId', {
		templateUrl: "/ngview/message.html",
		controller: 'MessController'
	}).when('/profile/:userId', {
		templateUrl: "/ngview/profile.html",
		controller: 'ProfileController'
	}).when('/shop/:userId', {
		templateUrl: "/ngview/shop.html",
		controller: 'ProfileController'
	})
		;
	// Set the default language
	var storedLanguage = localStorage.getItem('myAppLangKey') || 'vie';
	$translateProvider.preferredLanguage(storedLanguage);
})