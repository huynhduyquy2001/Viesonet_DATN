app.config(function ($translateProvider, $routeProvider) {
	$translateProvider.useStaticFilesLoader({
		prefix: 'json/', // Thay đổi đường dẫn này cho phù hợp
		suffix: '.json'
	});
	$routeProvider.when('/', {
		templateUrl: "ngview/home.html",
		controller: 'HomeController'
	}).when('/search', {
		templateUrl: "ngview/search.html",
		controller: 'SearchController'
	}).when('recommend', {
		templateUrl: "ngview/recommend.html",
		controller: 'RecommendController'
	}).when('/message', {
		templateUrl: "ngview/message.html",
		controller: 'MessController'
	}).when('/message/:otherId', {
		templateUrl: "ngview/message.html",
		controller: 'MessController'
	}).when('/profile/:userId', {
		templateUrl: "ngview/profile.html",
		controller: 'ProfileController'
	}).when('/product', {
		templateUrl: "ngview/productDetails.html",
		controller: 'ProductController'
	}).when('/mystore', {
		templateUrl: "/ngview/myStore.html",
		controller: 'MyStoreController'
	}).when('/shoppingcart', {
			templateUrl: "/ngview/shoppingcart.html",
			controller: 'ShoppingCartController'
	}).when('/admin/report', {
			templateUrl: "/ngview/report.html",
			controller: 'ReportController'
	}).when('/admin/usermanager', {
			templateUrl: "/ngview/usermanager.html",
			controller: 'UserManagerController'
	}).when('/admin/postsviolatin', {
			templateUrl: "/ngview/postsviolatin.html",
			controller: 'PostsViolationController'
	}).when('/favouriteProduct', {
		templateUrl: "ngview/favouriteProducts.html",
		controller: 'FavouriteProductsController'
	});

		;
	// Set the default language
	var storedLanguage = localStorage.getItem('myAppLangKey') || 'vie';
	$translateProvider.preferredLanguage(storedLanguage);
})