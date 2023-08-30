
var app = angular.module('myApp', ['pascalprecht.translate', 'ngRoute'])

app.controller('myCtrl', function ($scope, $http, $translate, $window, $rootScope, $location) {
	$scope.myAccount = {};
	$rootScope.unseenmess = 0;
	$rootScope.check = false;
	$scope.notification = [];
	$scope.allNotification = [];
	$rootScope.postComments = [];
	$rootScope.postDetails = {};
	var sound = new Howl({
		src: ['/images/nhacchuong2.mp3']
	});
	// Hàm để phát âm thanh
	$scope.playNotificationSound = function () {
		sound.play();
	};


	$scope.handleLinkClick = function (linkURL) {
		var ck = 0;
		const menuLinks = document.querySelectorAll("#sidebarnav .sidebar-link");

		// Lặp qua danh sách liên kết
		for (let i = 0; i < menuLinks.length; i++) {
			const link = menuLinks[i];
			if (link.getAttribute("href") === linkURL) {
				// Gỡ bỏ lớp "active" khỏi tất cả các liên kết
				menuLinks.forEach(link => link.classList.remove("active"));

				// Thêm lớp "active" vào liên kết được nhấp vào
				link.classList.add("active");
				ck = ck + 1;
			}

		}
		if (ck == 0) {
			menuLinks[0].classList.add("active");
		}
	};


	//xem chi tiết thông báo
	$scope.getPostDetails = function (postId) {
		$http.get('/findpostcomments/' + postId)

			.then(function (response) {
				var postComments = response.data;
				$rootScope.postComments = postComments;
				console.log(response.data);
			}, function (error) {
				// Xử lý lỗi
				console.log(error);
			});
		$scope.isReplyEmpty = true;
		$http.get('/postdetails/' + postId)
			.then(function (response) {
				var postDetails = response.data;
				$rootScope.postDetails = postDetails;
				// Xử lý phản hồi thành công từ máy chủ
				$('#chiTietBaiViet').modal('show');

			}, function (error) {
				// Xử lý lỗi
				console.log(error);
			});
	};

	$scope.getFormattedTimeAgo = function (date) {
		var currentTime = new Date();
		var activityTime = new Date(date);
		var timeDiff = currentTime.getTime() - activityTime.getTime();
		var seconds = Math.floor(timeDiff / 1000);
		var minutes = Math.floor(timeDiff / (1000 * 60));
		var hours = Math.floor(timeDiff / (1000 * 60 * 60));
		var days = Math.floor(timeDiff / (1000 * 60 * 60 * 24));

		if (days === 0) {
			if (hours === 0 && minutes < 60) {
				if (seconds < 60) {
					return 'vài giây trước';
				} else {
					return minutes + ' phút trước';
				}
			} else if (hours < 24) {
				return hours + ' giờ trước';
			}
		} else if (days === 1) {
			return 'Hôm qua';
		} else if (days <= 7) {
			return days + ' ngày trước';
		} else {
			// Hiển thị ngày, tháng và năm của activityTime
			var formattedDate = activityTime.getDate();
			var formattedMonth = activityTime.getMonth() + 1; // Tháng trong JavaScript đếm từ 0, nên cần cộng thêm 1
			var formattedYear = activityTime.getFullYear();
			return formattedDate + '-' + formattedMonth + '-' + formattedYear;
		}
	};
	$http.get('/findmyaccount')
		.then(function (response) {
			var myAccount = response.data;
			$scope.myAccount = myAccount;
		})
		.catch(function (error) {
			console.log(error);
		});
	//Đa ngôn ngữ	
	$scope.changeLanguage = function (langKey) {
		$translate.use(langKey);
		localStorage.setItem('myAppLangKey', langKey); // Lưu ngôn ngữ đã chọn vào localStorages
	};
	// Kiểm tra xem còn tin nhắn nào chưa đọc không
	$http.get('/getunseenmessage')
		.then(function (response) {
			$rootScope.check = response.data > 0;
			$rootScope.unseenmess = response.data;
		})
		.catch(function (error) {
			console.log(error);
		});

	//Load thông báo
	$scope.hasNewNotification = false;
	$scope.notificationNumber = [];
	//Load thông báo chưa đọc
	$http.get('/loadnotification')
		.then(function (response) {
			var data = response.data;
			console.log(data)
			for (var i = 0; i < data.length; i++) {
				$scope.notification.push(data[i]);
				$scope.notificationNumber = $scope.notification;
				if ($scope.notificationNumber.length != 0) {
					$scope.hasNewNotification = true;
				}
			}
		})
		.catch(function (error) {
			console.log(error);
		});
	//Load tất cả thông báo
	$http.get('/loadallnotification')
		.then(function (response) {
			$scope.allNotification = response.data;
		})
		.catch(function (error) {
			console.log(error);
		});
	//Kết nối websocket
	$scope.ConnectNotification = function () {
		var socket = new SockJS('/private-notification');
		var stompClient = Stomp.over(socket);
		stompClient.connect({}, function (frame) {
			stompClient.subscribe('/private-user', function (response) {

				var data = JSON.parse(response.body)
				// Kiểm tra điều kiện đúng với user hiện tại thì thêm thông báo mới
				if ($scope.myAccount.user.userId === data.receiver.userId) {
					//thêm vào thông báo mới
					$scope.notification.push(data);
					//thêm vào tất cả thông báo
					$scope.allNotification.push(data);
					//thêm vào mảng để đếm độ số thông báo
					$scope.notificationNumber.push(data);
					//cho hiện thông báo mới
					$scope.hasNewNotification = true;
				}
				$scope.$apply();

			});
		});
	};


	//Kết nối khi mở trang web
	$scope.ConnectNotification();
	// Hàm này sẽ được gọi sau khi ng-include hoàn tất nạp tập tin "_menuLeft.html"

	// Thay đổi URL SockJS tại đây nếu cần thiết
	var sockJSUrl = '/chat';

	// Tạo một đối tượng SockJS bằng cách truyền URL SockJS
	var socket = new SockJS(sockJSUrl);

	// Tạo một kết nối thông qua Stomp over SockJS
	var stompClient = Stomp.over(socket);

	// Khi kết nối WebSocket thành công
	stompClient.connect({}, function (frame) {
		console.log('Connected: ' + frame);

		// Đăng ký hàm xử lý khi nhận thông điệp từ server
		// Lắng nghe các tin nhắn được gửi về cho người dùng
		stompClient.subscribe('/user/' + $scope.myAccount.user.userId + '/queue/receiveMessage', function (message) {
			// Kiểm tra xem còn tin nhắn nào chưa đọc không
			$http.get('/getunseenmessage')
				.then(function (response) {
					$rootScope.check = response.data > 0;
					$rootScope.unseenmess = response.data;
					// Hàm để phát âm thanh
					$scope.playNotificationSound = function () {
						sound.play();
					};
				})
				.catch(function (error) {
					console.log(error);
				});
		});
	}, function (error) {
		console.error('Lỗi kết nối WebSocket:', error);
	});


	//Ẩn tất cả thông báo khi click vào xem
	$scope.hideNotification = function () {
		$http.post('/setHideNotification', $scope.notification)
			.then(function (response) {
				// Xử lý phản hồi từ backend nếu cần
			})
			.catch(function (error) {
				// Xử lý lỗi nếu có
			});
		$scope.hasNewNotification = false;
		$scope.notificationNumber = [];
	}

	//Xóa thông báo
	$scope.deleteNotification = function (notificationId) {
		$http.delete('/deleteNotification/' + notificationId)
			.then(function (response) {
				$scope.allNotification = $scope.allNotification.filter(function (allNotification) {
					return allNotification.notificationId !== notificationId;
				});
			})
			.catch(function (error) {
				// Xử lý lỗi nếu có
			});
	}

	//Ẩn thông báo 
	$scope.hideNotificationById = function (notificationId) {
		// Ví dụ xóa phần tử có notificationId là 123
		$scope.removeNotificationById(notificationId);
	}
	//Hàm xóa theo ID của mảng
	$scope.removeNotificationById = function (notificationIdToRemove) {
		// Lọc ra các phần tử có notificationId khác với notificationIdToRemove
		$scope.notification = $scope.notification.filter(function (notification) {
			return notification.notificationId !== notificationIdToRemove;
		});
	};
})


