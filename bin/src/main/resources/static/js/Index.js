
angular.module('myApp', ['pascalprecht.translate'])
.config(function($translateProvider) {
		$translateProvider.useStaticFilesLoader({
			prefix: 'json/', // Thay đổi đường dẫn này cho phù hợp
			suffix: '.json'
		});
		// Set the default language
		var storedLanguage = localStorage.getItem('myAppLangKey') || 'vie';
		$translateProvider.preferredLanguage(storedLanguage);
	})
	.controller('myCtrl', function($scope, $http, $translate) {
		$scope.Posts = [];
		$scope.likedPosts = [];
		$scope.myAccount = {};
		$scope.postData = {};
		$scope.postDetails = {};
		$scope.postComments = [];
		$scope.replyContent = {}; // Khởi tạo replyContent      
		$scope.check = false;
		$scope.unseenmess = 0;
		$scope.notification = [];
		$scope.allNotification = [];
		$scope.violations = [];
		$scope.selectedPostId = '';

		$scope.numOfCommentsToShow = 20; // Số lượng bình luận hiển thị ban đầu
		$scope.commentsToShowMore = 10; // Số lượng bình luận hiển thị khi nhấp vào "hiển thị thêm"

		$scope.changeLanguage = function(langKey) {
			$translate.use(langKey);
			localStorage.setItem('myAppLangKey', langKey); // Lưu ngôn ngữ đã chọn vào localStorage
		};

		// Hàm để tăng số lượng bình luận hiển thị khi nhấp vào "hiển thị thêm"
		$scope.showMoreComments = function() {
			$scope.numOfCommentsToShow += $scope.commentsToShowMore;
		};
	

		//kiểm tra xem còn tin nhắn nào chưa đọc không
		$http.get('/getunseenmessage')
			.then(function(response) {
				var count = response.data;
				if (count > 0) {
					$scope.check = true;
					$scope.unseenmess = count;
				}
			})
			.catch(function(error) {
				console.log(error);
			});

		//Lấy danh sách vi phạm
		$http.get('/getviolations')
			.then(function(response) {
				$scope.violations = response.data;

			})
			.catch(function(error) {
				console.log(error);
			});


		$http.get('/findfollowing')
			.then(function(response) {
				var Posts = response.data;
				$scope.Posts = Posts;
			})
			.catch(function(error) {
				console.log(error);
			});

		$http.get('/findlikedposts')
			.then(function(response) {
				var likedPosts = response.data;
				$scope.likedPosts = likedPosts;
			})
			.catch(function(error) {
				console.log(error);
			});

		$http.get('/findmyaccount')
			.then(function(response) {
				var myAccount = response.data;
				$scope.myAccount = myAccount;
			})
			.catch(function(error) {
				console.log(error);
			});

		$scope.openModalBaoCao = function(postId) {
			$scope.selectedPostId = postId;
			$('#modalBaoCao').modal('show');
		};

		$scope.report = function(postId) {
			if ($scope.selectedViolationType === null || $scope.selectedViolationType === undefined) {
				const Toast = Swal.mixin({
					toast: true,
					position: 'top-end',
					showConfirmButton: false,
					timer: 1000,
					timerProgressBar: true,
					didOpen: (toast) => {
						toast.addEventListener('mouseenter', Swal.stopTimer)
						toast.addEventListener('mouseleave', Swal.resumeTimer)
					}
				})
				Toast.fire({
					icon: 'warning',
					title: 'Bạn phải chọn nội dung báo cáo'
				})
				return;
			}
			$http.post('/report/' + postId + '/' + $scope.selectedViolationType)
				.then(function(response) {
					const Toast = Swal.mixin({
						toast: true,
						position: 'top-end',
						showConfirmButton: false,
						timer: 1000,
						timerProgressBar: true,
						didOpen: (toast) => {
							toast.addEventListener('mouseenter', Swal.stopTimer)
							toast.addEventListener('mouseleave', Swal.resumeTimer)
						}
					})
					Toast.fire({
						icon: 'success',
						title: 'Báo cáo bài viết thành công'
					})
				})
				.catch(function(error) {
					// Xử lý lỗi
					console.log(error);
				});
			$('#modalBaoCao').modal('hide');
		};


		$scope.likePost = function(postId) {
			var likedIndex = $scope.likedPosts.indexOf(postId.toString());
			var likeEndpoint = '/likepost/' + postId;
			var dislikeEndpoint = '/didlikepost/' + postId;

			// Nếu postId chưa tồn tại trong mảng likedPosts
			if (likedIndex === -1) {
				// Thêm postId vào mảng likedPosts
				$scope.likedPosts.push(postId.toString());

				// Gửi yêu cầu POST để like bài viết và cập nhật likeCount+1
				$http.post(likeEndpoint)
					.then(function(response) {

						// Cập nhật thuộc tính likeCount+1 trong đối tượng post
						var post = $scope.Posts.find(function(item) {
							return item.postId === postId;
						});
						if (post) {
							post.likeCount++;

						}
					})
					.catch(function(error) {
						// Xử lý lỗi
						console.log(error);
					});
			} else {

				// Xóa postId khỏi mảng likedPosts
				$scope.likedPosts.splice(likedIndex, 1);

				// Gửi yêu cầu POST để dislike bài viết và cập nhật likeCount-1
				$http.post(dislikeEndpoint)
					.then(function(response) {
						// Xử lý thành công
						//console.log(response.data);

						// Cập nhật thuộc tính likeCount-1 trong đối tượng post
						var post = $scope.Posts.find(function(item) {
							return item.postId === postId;
						});
						if (post) {
							post.likeCount--;

						}
					})
					.catch(function(error) {
						// Xử lý lỗi
						console.log(error);
					});
			}
		};
		$scope.getFormattedTimeAgo = function(date) {
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
						return seconds + ' giây trước';
					} else {
						return minutes + ' phút trước';
					}
				} else if (hours < 24) {
					return hours + ' giờ trước';
				}
			} else if (days === 1) {
				return 'Hôm qua';
			} else {
				return days + ' ngày trước';
			}
		};

		$scope.getPostDetails = function(postId) {
			$http.get('/findpostcomments/' + postId)
				.then(function(response) {
					var count = response.data;
					if (count > 0) {
						$scope.check = true;
					}
				})
				.catch(function(error) {
					console.log(error);
				});
		}
		$http.get('/findfollowing')
			.then(function(response) {
				var Posts = response.data;
				$scope.Posts = Posts;
			})
			.catch(function(error) {
				console.log(error);
			});

		$http.get('/findlikedposts')
			.then(function(response) {
				var likedPosts = response.data;
				$scope.likedPosts = likedPosts;
			})
			.catch(function(error) {
				console.log(error);
			});

		$http.get('/findmyaccount')
			.then(function(response) {
				var myAccount = response.data;
				$scope.myAccount = myAccount;
			})
			.catch(function(error) {
				console.log(error);
			});

		$scope.likePost = function(postId) {
			var likedIndex = $scope.likedPosts.indexOf(postId.toString());
			var likeEndpoint = '/likepost/' + postId;
			var dislikeEndpoint = '/didlikepost/' + postId;

			// Nếu postId chưa tồn tại trong mảng likedPosts
			if (likedIndex === -1) {
				// Thêm postId vào mảng likedPosts
				$scope.likedPosts.push(postId.toString());

				// Gửi yêu cầu POST để like bài viết và cập nhật likeCount+1
				$http.post(likeEndpoint)
					.then(function(response) {

						// Cập nhật thuộc tính likeCount+1 trong đối tượng post
						var post = $scope.Posts.find(function(item) {
							return item.postId === postId;
						});
						if (post) {
							post.likeCount++;

						}
					})
					.catch(function(error) {
						// Xử lý lỗi
						console.log(error);
					});
			} else {

				// Xóa postId khỏi mảng likedPosts
				$scope.likedPosts.splice(likedIndex, 1);

				// Gửi yêu cầu POST để dislike bài viết và cập nhật likeCount-1
				$http.post(dislikeEndpoint)
					.then(function(response) {
						// Xử lý thành công
						//console.log(response.data);

						// Cập nhật thuộc tính likeCount-1 trong đối tượng post
						var post = $scope.Posts.find(function(item) {
							return item.postId === postId;
						});
						if (post) {
							post.likeCount--;

						}
					})
					.catch(function(error) {
						// Xử lý lỗi
						console.log(error);
					});
			}
		};
		$scope.getFormattedTimeAgo = function(date) {
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
						return seconds + ' giây trước';
					} else {
						return minutes + ' phút trước';
					}
				} else if (hours < 24) {
					return hours + ' giờ trước';
				}
			} else if (days === 1) {
				return 'Hôm qua';
			} else {
				return days + ' ngày trước';
			}
		};

		$scope.post = function() {
			var formData = new FormData();
			var fileInput = document.getElementById('inputGroupFile01');

			// Check if no files are selected
			if (fileInput.files.length === 0) {
				const Toast = Swal.mixin({
					toast: true,
					position: 'top-end',
					showConfirmButton: false,
					timer: 3000,
					timerProgressBar: true,
					didOpen: (toast) => {
						toast.addEventListener('mouseenter', Swal.stopTimer)
						toast.addEventListener('mouseleave', Swal.resumeTimer)
					}
				})

				Toast.fire({
					icon: 'warning',
					title: 'Bạn phải thêm ảnh vào bài viết'
				})
				return; // Return without doing anything
			}
			if ($scope.content === null || $scope.content === undefined) {
				$scope.content = '';
			}
			for (var i = 0; i < fileInput.files.length; i++) {
				formData.append('photoFiles', fileInput.files[i]);
			}
			formData.append('content', $scope.content);

			$http.post('/post', formData, {
				transformRequest: angular.identity,
				headers: {
					'Content-Type': undefined
				}
			}).then(function(response) {
				// Xử lý phản hồi thành công từ máy chủ

			}, function(error) {
				// Xử lý lỗi
				console.log(error);
			})
			$scope.content = '';
			const Toast = Swal.mixin({
				toast: true,
				position: 'top-end',
				showConfirmButton: false,
				timer: 3000,
				timerProgressBar: true,
				didOpen: (toast) => {
					toast.addEventListener('mouseenter', Swal.stopTimer)
					toast.addEventListener('mouseleave', Swal.resumeTimer)
				}
			})

			Toast.fire({
				icon: 'success',
				title: 'Bài viết được đăng thành công'
			})
		};


		$scope.getPostDetails = function(postId) {
			$http.get('/findpostcomments/' + postId)
				.then(function(response) {
					var postComments = response.data;
					$scope.postComments = postComments;


					console.log(response.data);
				}, function(error) {
					// Xử lý lỗi
					console.log(error);
				});

			$scope.isReplyEmpty = true;
			$http.get('/postdetails/' + postId)
				.then(function(response) {
					var postDetails = response.data;
					$scope.postDetails = postDetails;
					// Xử lý phản hồi thành công từ máy chủ
					$('#chiTietBaiViet').modal('show');

				}, function(error) {
					// Xử lý lỗi
					console.log(error);
				});
		};


		$scope.addComment = function(postId) {
			var myComment = $scope.myComment;
			if (myComment === null || myComment === undefined) {
				const Toast = Swal.mixin({
					toast: true,
					position: 'top-end',
					showConfirmButton: false,
					timer: 1000,
					timerProgressBar: true,
					didOpen: (toast) => {
						toast.addEventListener('mouseenter', Swal.stopTimer)
						toast.addEventListener('mouseleave', Swal.resumeTimer)
					}
				})
				Toast.fire({
					icon: 'warning',
					title: 'Bạn phải nhập nội dung bình luận'
				})
				return;
			}
			$http.post('/addcomment/' + postId + '?myComment=' + myComment)
				.then(function(response) {
					$scope.postComments.unshift(response.data);
					var postToUpdate = $scope.Posts.find(function(post) {
						return post.postId = postId;
					});
					if (postToUpdate) {
						postToUpdate.commentCount++;
					}
					$scope.myComment = '';


				}, function(error) {
					console.log(error);
				});
		};


		$scope.logout = function() {
			$http.get('/logout')
				.then(function() {
					window.location.href = '/login';
				}, function(error) {
					console.log(error);
				});
		};




		$scope.sendReply = function(receiverId, replyContent, replyId, commentId) {
			var requestData = {
				receiverId: receiverId,
				replyContent: replyContent,
				commentId: commentId,
				postId: $scope.postDetails.postId
			};
			if (replyContent === null || replyContent === undefined) {
				const Toast = Swal.mixin({
					toast: true,
					position: 'top-end',
					showConfirmButton: false,
					timer: 1000,
					timerProgressBar: true,
					didOpen: (toast) => {
						toast.addEventListener('mouseenter', Swal.stopTimer)
						toast.addEventListener('mouseleave', Swal.resumeTimer)
					}
				})
				Toast.fire({
					icon: 'warning',
					title: 'Bạn phải nhập nội dung phản hồi'
				})
				return;

			}
			var postToUpdate = $scope.Posts.find(function(post) {
				return post.postId = $scope.postDetails.postId;
			});
			if (postToUpdate) {
				postToUpdate.commentCount++;
			}
			$http.post('/addreply', requestData)
				.then(function(response) {
					var comment = $scope.postComments.find(function(comment) {
						return comment.commentId === commentId;
					});
					comment.reply.unshift(response.data);
					$scope.replyContent[replyId] = '';
				})
				.catch(function(error) {
					// Xử lý lỗi
					console.log('Lỗi:', error);
				});

		};



		$scope.sendReplyForComment = function(receiverId, commentId, replyContent) {
			var requestData = {
				receiverId: receiverId,
				replyContent: replyContent,
				commentId: commentId,
				postId: $scope.postDetails.postId
			};
			if (replyContent === null || replyContent === undefined) {
				const Toast = Swal.mixin({
					toast: true,
					position: 'top-end',
					showConfirmButton: false,
					timer: 1000,
					timerProgressBar: true,
					didOpen: (toast) => {
						toast.addEventListener('mouseenter', Swal.stopTimer)
						toast.addEventListener('mouseleave', Swal.resumeTimer)
					}
				})
				Toast.fire({
					icon: 'warning',
					title: 'Bạn phải nhập nội dung phản hồi'
				})
				return;

			}
			$http.post('/addreply', requestData)
				.then(function(response) {
					var comment = $scope.postComments.find(function(comment) {
						return comment.commentId === commentId;
					});
					comment.reply.unshift(response.data);
					$scope.replyContent[commentId] = '';
					var postToUpdate = $scope.Posts.find(function(post) {
						return post.postId = $scope.postDetails.postId;
					});
					if (postToUpdate) {
						postToUpdate.commentCount++;
					}

				})
				.catch(function(error) {
					// Xử lý lỗi
					console.log('Lỗi:', error);
				});

		};
		$scope.handleKeyDown = function(event, userId, replyContent, replyId, commentId) {
			if (event.keyCode === 13) {
				// Người dùng đã nhấn phím Enter
				$scope.sendReply(userId, replyContent, replyId, commentId);
			}
		};

		$scope.handleKeyDownReplyForComment = function(event, receiverId, commentId, replyContent) {
			if (event.keyCode === 13) {
				// Người dùng đã nhấn phím Enter
				$scope.sendReplyForComment(receiverId, commentId, replyContent);
			}
		};

		//Thay đổi URL SockJS tại đây nếu cần thiết
		var sockJSUrl = '/chat';

		// Tạo một đối tượng SockJS bằng cách truyền URL SockJS
		var socket = new SockJS(sockJSUrl);

		// Tạo một kết nối thông qua Stomp over SockJS
		var stompClient = Stomp.over(socket);

		// Khi kết nối WebSocket thành công
		// Khi kết nối WebSocket thành công
		stompClient.connect({}, function(frame) {
			// Đăng ký hàm xử lý khi nhận thông điệp từ server
			// Lắng nghe các tin nhắn được gửi về cho người dùng
			stompClient.subscribe('/user/' + $scope.myAccount.user.userId + '/queue/receiveMessage', function(message) {
				$scope.check = true;
				$http.get('/getunseenmessage')
					.then(function(response) {
						var count = response.data;
						if (count > 0) {
							$scope.check = true;
							$scope.unseenmess = count;
						}
					})
					.catch(function(error) {
						console.log(error);
					});
				$scope.$apply();
			});
		}, function(error) {
			console.error('Lỗi kết nối WebSocket:', error);
		});

		//Load thông báo
		$scope.hasNewNotification = false;
		$scope.notificationNumber = [];
		//Load thông báo chưa đọc
		$http.get('/loadnotification')
			.then(function(response) {
				$scope.notification = response.data;
				$scope.notificationNumber = $scope.notification;
				if($scope.notificationNumber.length != 0){
					$scope.hasNewNotification = true;
				}
			})
			.catch(function(error) {
				console.log(error);
			});
		//Load tất cả thông báo
		$http.get('/loadallnotification')
			.then(function(response) {
				$scope.allNotification = response.data;
			})
			.catch(function(error) {
				console.log(error);
			});
		//Kết nối websocket
		$scope.ConnectNotification = function() {
			var socket = new SockJS('/private-notification');
			var stompClient = Stomp.over(socket);
			stompClient.debug = false;
			stompClient.connect({}, function(frame) {
				stompClient.subscribe('/private-user', function(response) {

					var data = JSON.parse(response.body)
					// Kiểm tra điều kiện đúng với user hiện tại thì thêm thông báo mới
					if ($scope.myAccount.user.userId === data.receiver.userId) {
						//thêm vào thông báo mới
						$scope.notification.push(data);
						
						//thêm vào mảng để đếm độ số thông báo
						$scope.notificationNumber.push(data);
						//cho hiện thông báo mới
						$scope.hasNewNotification = true; 
					}
					$scope.$apply();

				});
			});
		};

		//xem chi tiết thông báo
		$scope.seen = function(notificationId) {
			$scope.getPostDetails(notificationId);
		}

		//Ẩn tất cả thông báo khi click vào xem
		$scope.hideNotification = function() {
			$http.post('/setHideNotification', $scope.notification)
				.then(function(response) {
					// Xử lý phản hồi từ backend nếu cần
				})
				.catch(function(error) {
					// Xử lý lỗi nếu có
				});
			$scope.hasNewNotification = false;
			$scope.notificationNumber = [];
		}
		
		//Xóa thông báo
		$scope.deleteNotification = function(notificationId) {
			$http.delete('/deleteNotification/'+ notificationId)
				.then(function(response) {
					$scope.allNotification = $scope.allNotification.filter(function(allNotification) {
					return allNotification.notificationId !== notificationId;});
				})
				.catch(function(error) {
					// Xử lý lỗi nếu có
				});
		}

		//Ẩn thông báo 
		$scope.hideNotificationById = function(notificationId) {
			// Ví dụ xóa phần tử có notificationId là 123
			$scope.removeNotificationById(notificationId);
		}
		//Hàm xóa theo ID của mảng
		$scope.removeNotificationById = function(notificationIdToRemove) {
			// Lọc ra các phần tử có notificationId khác với notificationIdToRemove
			$scope.notification = $scope.notification.filter(function(notification) {
				return notification.notificationId !== notificationIdToRemove;
			});
		};
		//Kết nối khi mở trang web
		$scope.ConnectNotification();
		// Hàm này sẽ được gọi sau khi ng-include hoàn tất nạp tập tin "_menuLeft.html"

	});


