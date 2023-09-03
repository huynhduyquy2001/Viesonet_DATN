
var app = angular.module('myApp', ['pascalprecht.translate', 'ngRoute'])

app.controller('myCtrl', function ($scope, $http, $translate, $window, $rootScope, $location, $timeout, $interval) {
	$scope.myAccount = {};
	$rootScope.unseenmess = 0;
	$rootScope.check = false;
	$scope.notification = [];
	$scope.allNotification = [];
	$rootScope.postComments = [];
	$rootScope.postDetails = {};
	$scope.ListUsersMess = [];
	$scope.receiver = {};
	$scope.newMessMini = '';
	$rootScope.ListMess = [];
	var sound = new Howl({
		src: ['/images/nhacchuong2.mp3']
	});
	// Hàm để phát âm thanh
	$scope.playNotificationSound = function () {
		sound.play();
	};

	$http.get('/getusersmess')
		.then(function (response) {
			$scope.ListUsersMess = response.data;
		})
		.catch(function (error) {
			console.log(error);
		});

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

	//tìm người mình nhắn tin và danh sách tin nhắn với người đó
	$scope.getMess = function (receiverId) {
		$http.get('/getUser/' + receiverId)
			.then(function (response) {
				$scope.receiver = response.data;
			})
		$http.get('/getmess2/' + receiverId)
			.then(function (response) {
				$rootScope.ListMess = response.data;

			})
			.catch(function (error) {
				console.log(error);
			});
		var boxchatMini = document.getElementById("boxchatMini");
		boxchatMini.style.bottom = '0';


		angular.element(document.querySelector('.menu')).toggleClass('menu-active');
		var menu = angular.element(document.querySelector('.menu'));
		if (menu.hasClass('menu-active')) {
			menu.css("right", "0");
		} else {
			menu.css("right", "-330px");
		}
		$timeout(function () {
			var boxchatMini = document.getElementById("messMini");
			boxchatMini.scrollTop = boxchatMini.scrollHeight;
		}, 100);
	}

	//Hàm thu hồi tin nhắn
	$scope.revokeMessage = function (messId) {
		$http.post('/removemess/' + messId)
			.then(function (reponse) {
				var messToUpdate = $scope.ListMess.find(function (mess) {
					return mess.messId === messId;
				})
				messToUpdate.status = "Đã ẩn";

				var mess = reponse.data;

				var objUpdate = $scope.ListUsersMess.find(function (obj) {
					return (mess.receiver.userId === obj[0] || mess.receiver.userId === obj[2]) && mess.messId === obj[9];
				});
				if (objUpdate) {
					Object.assign(objUpdate, {
						0: mess.sender.userId,
						1: mess.sender.username,
						2: mess.receiver.userId,
						3: mess.receiver.username,
						4: mess.sender.avatar,
						5: mess.receiver.avatar,
						6: mess.content,
						7: new Date(),
						8: "Đã ẩn",
						9: mess.messId
					});
				}
				stompClient.send('/app/sendnewmess', {}, JSON.stringify(mess));



			}, function (error) {
				console.log(error);
			});
	};

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
		// Đăng ký hàm xử lý khi nhận thông điệp từ server
		// Lắng nghe các tin nhắn được gửi về cho người dùng
		stompClient.subscribe('/user/' + $scope.myAccount.user.userId + '/queue/receiveMessage', function (message) {
			try {
				var newMess = JSON.parse(message.body);

				var checkMess = $rootScope.ListMess.find(function (obj) {
					return obj.messId === newMess.messId;
				});
				if (checkMess) {
					checkMess.status = 'Đã ẩn';
				}
				// Xử lý tin nhắn mới nhận được ở đây khi nhắn đúng người
				else if (($scope.receiver.userId === newMess.sender.userId || $scope.myAccount.user.userId === newMess.sender.userId) && !checkMess) {
					$rootScope.ListMess.push(newMess);
				}
				if ($scope.myAccount.user.userId !== newMess.sender.userId) {
					$scope.playNotificationSound();
				}
				//cập nhật lại danh sách người đang nhắn tin với mình
				$http.get('/getusersmess')
					.then(function (response) {
						$scope.ListUsersMess = response.data;
						//$scope.playNotificationSound();
					})
					.catch(function (error) {
						console.log(error);
					});

				$timeout(function () {
					$scope.scrollToBottom();
				}, 10);

				$scope.$apply();
			} catch (error) {
				alert('Error handling received message:', error);
			}
		});
	}, function (error) {
		console.error('Lỗi kết nối WebSocket:', error);
	});

	// Hàm gửi tin nhắn và lưu vào csdl
	$scope.sendMessage = function (content) {
		if (content == '' || content.trim() === undefined) {
			return;
		}
		var sender = $scope.myAccount.user.userId;
		var receiver = $scope.receiver.userId;
		var message = {
			senderId: sender,
			receiverId: receiver,
			content: content
		};
		// Lưu tin nhắn vào cơ sở dữ liệu
		$http.post('/savemess', message)
			.then(function (response) {
				// Hàm gửi tin nhắn qua websocket
				stompClient.send('/app/sendnewmess', {}, JSON.stringify(response.data));
				$http.post('/seen/' + receiver)
					.then(function (response) {
						$http.get('/getunseenmessage')
							.then(function (response) {
								$rootScope.check = response.data > 0;
								$rootScope.unseenmess = response.data;
								// Làm rỗng trường nhập liệu có id "newMessMini"
								var newMessMini = document.getElementById("newMessMini");
								if (newMessMini) {
									newMessMini.value = ''; // Đặt giá trị của trường nhập liệu thành chuỗi rỗng
								}
								$timeout(function () {
									$scope.scrollToBottom();
								}, 100);
							})
							.catch(function (error) {
								console.log(error);
							});
					})
					.catch(function (error) {
						console.log(error);
					});
			})
			.catch(function (error) {
				console.log(error);
			});
	};


	$scope.scrollToBottom = function () {
		var chatContainer = document.getElementById("messMini");
		chatContainer.scrollTop = chatContainer.scrollHeight;
	};


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




	// Trong AngularJS controller
	$scope.toggleMenu = function (event) {
		event.stopPropagation();
		angular.element(document.querySelector('.menu')).toggleClass('menu-active');
		var menu = angular.element(document.querySelector('.menu'));
		if (menu.hasClass('menu-active')) {
			menu.css("right", "0");
		} else {
			menu.css("right", "-330px");
		}
	};

	$scope.closeBoxchat = function (event) {
		event.stopPropagation();
		angular.element(document.getElementById('boxchatMini')).toggleClass('menu-active');
		var boxchatMini = angular.element(document.getElementById('boxchatMini'));
		boxchatMini.css("bottom", "-500px");
	};

	angular.element(document).on('click', function (event) {
		var menu = angular.element(document.querySelector('.menu'));
		var toggleButton = angular.element(document.getElementById('toggle-menu'));

		if (!menu[0].contains(event.target) && event.target !== toggleButton[0]) {
			menu.css("right", "-330px");
			menu.removeClass('menu-active');
		}
	});

	// Ban đầu ẩn menu
	var menu = angular.element(document.querySelector('.menu'));
	menu.css("right", "-330px");
	var boxchatMini = angular.element(document.getElementById('boxchatMini'));
	boxchatMini.css("bottom", "-500px");

})


