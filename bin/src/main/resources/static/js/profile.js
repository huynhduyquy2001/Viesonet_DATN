angular.module('myApp', ['ngRoute','pascalprecht.translate'])
.config(function($translateProvider) {
		$translateProvider.useStaticFilesLoader({
			prefix: 'json/', // Thay đổi đường dẫn này cho phù hợp
			suffix: '.json'
		});
	// Set the default language
		var storedLanguage = localStorage.getItem('myAppLangKey') || 'vie';
		$translateProvider.preferredLanguage(storedLanguage);
	})
	.controller('myCtrl', function($scope, $http ,$translate) {

		$scope.Posts = [];
		$scope.likedPosts = [];
		$scope.myAccount = {};
		$scope.postData = {};
		$scope.postDetails = {};
		$scope.postComments = [];
		$scope.followers = [];
		$scope.followings = [];
		$scope.myPostImage = [];
		$scope.myListFollow = [];
		$scope.UserInfo = {};
		$scope.myUserId = '';
		$scope.notification = [];
		$scope.allNotification = [];
		//Đa ngôn ngữ	
      $scope.changeLanguage = function (langKey) {
          $translate.use(langKey);
          localStorage.setItem('myAppLangKey', langKey); // Lưu ngôn ngữ đã chọn vào localStorages
      };
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
			stompClient.connect({}, function(frame) {
				stompClient.subscribe('/private-user', function(response) {

					var data = JSON.parse(response.body)
					// Kiểm tra điều kiện đúng với user hiện tại thì thêm thông báo mới
					if ($scope.UserInfo.userId === data.receiver.userId) {
						
						//thêm vào thông báo mới
						$scope.notification.push(data);
						console.log($scope.notification)
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
		
		
		$http.get('/findusers')
			.then(function(response) {
				var UserInfo = response.data;
				$scope.UserInfo = UserInfo;
				$scope.myUserId = $scope.UserInfo.userId;
			})
			.catch(function(error) {
				console.log(error);
			});
		// Truyền dữ liệu vào Service


		// Hàm gọi API để lấy thông tin người dùng và cập nhật vào biến $scope.UpdateUser
		$http.get('/getUserInfo').then(function(response) {
			$scope.UserInfo = response.data;
			$scope.birthday = new Date($scope.UserInfo.birthday)
			// Khởi tạo biến $scope.UpdateUser để lưu thông tin cập nhật

			$scope.UpdateUser = angular.copy($scope.UserInfo);

		});

		// Hàm cập nhật thông tin người dùng
		$scope.updateUserInfo = function() {
			// Gửi dữ liệu từ biến $scope.UpdateUser đến server thông qua một HTTP request (POST request)
			$scope.UpdateUser.birthday = $scope.birthday;
			$http.post('/updateUserInfo', $scope.UpdateUser).then(function(response) {
				$http.get('/findusers')
					.then(function(response) {
						var UserInfo = response.data;
						$scope.UserInfo = UserInfo;
					})
					.catch(function(error) {
						console.log(error);
					});
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
					title: 'Cập nhật thông tin thành công!'
				})
			}).catch(function(error) {
				console.error('Error while updating user info:', error);
			});
		};


		// Hàm gọi API để lấy thông tin người dùng và cập nhật vào biến $scope.UpdateUser
		$http.get('/getAccInfo').then(function(response) {
			$scope.AccInfo = response.data;
			// Khởi tạo biến $scope.UpdateUser để lưu thông tin cập nhật

			$scope.UpdateAcc = angular.copy($scope.AccInfo);


		});

		// Hàm cập nhật thông tin người dùng
		$scope.updateAccInfo = function() {
			// Gửi dữ liệu từ biến $scope.UpdateUser đến server thông qua một HTTP request (POST request)
			$http.post('/updateAccInfo/' + $scope.AccInfo.email + "/" + $scope.AccInfo.accountStatus.statusName).then(function(response) {
				console.log('Account info updated successfully.');
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
					title: 'Cập nhật tài khoản thành công!'
				})
			}).catch(function(error) {
				console.error('Error while updating account info:', error);
			});
		};

		$http.get('/findaccounts')
			.then(function(response) {
				var AccInfo = response.data;
				$scope.AccInfo = AccInfo;
			})
			.catch(function(error) {
				console.log(error);
			});


		$http.get('/findmyfollowers')
			.then(function(response) {
				$scope.followers = response.data;
				//console.log($scope.followers); // Kiểm tra dữ liệu trong console log
			})
			.catch(function(error) {
				console.log(error);
			});

		$http.get('/findmyfollowing')
			.then(function(response) {
				$scope.followings = response.data;
				//console.log($scope.followings); // Kiểm tra dữ liệu trong console log
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

		$http.get('/findmyfollow')
			.then(function(response) {
				var myAccount = response.data;
				$scope.myAccount = myAccount;
				console.log($scope.myAccount);
			})
			.catch(function(error) {
				console.log(error);
			});
		$http.get('/getmypost')
			.then(function(response) {
				var myPosts = response.data;
				$scope.myPosts = myPosts;

				$scope.totalImagesCount = myPosts.reduce(function(total, post) {
					return total + post.images.length;
				}, 0);

			});

		$http.get('/countmypost')
			.then(function(response) {
				var sumPost = response.data;
				$scope.sumPost = sumPost;
			})

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
						var post = $scope.myPosts.find(function(item) {
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
						var post = $scope.myPosts.find(function(item) {
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
					return minutes + ' phút trước';
				} else if (hours < 24) {
					return hours + ' giờ trước';
				}
			} else if (days === 1) {
				return 'hôm qua';
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

			$http.get('/postdetails/' + postId)
				.then(function(response) {
					var postDetails = response.data;
					$scope.postDetails = postDetails;
					// Xử lý phản hồi thành công từ máy chủ
					$('#chiTietBaiViet').modal('show');
					console.log(response.data);
				}, function(error) {
					// Xử lý lỗi
					console.log(error);
				});
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
		$http.get('/getallfollow')
			.then(function(response) {
				$scope.myListFollow = response.data;
			}, function(error) {
				// Xử lý lỗi
				console.log(error);
			});
		$scope.isFollowing = function(followingId) {
			// Lấy id của người dùng hiện tại
			var currentUserId = $scope.UserInfo.userId;
			// Kiểm tra xem người dùng hiện tại đã follow người dùng với id tương ứng (followingId) chưa
			// Dựa vào danh sách các người dùng mà người dùng hiện tại đã follow
			// Trong ví dụ này, danh sách này có thể được lưu trữ trong cơ sở dữ liệu hoặc được lấy từ API
			var myListFollow = $scope.myListFollow;
			for (var i = 0; i < myListFollow.length; i++) {
				if (myListFollow[i].followingId === followingId && myListFollow[i].followerId === currentUserId) {
					// Người dùng hiện tại đã follow người dùng với id tương ứng
					return true;
				}
			}
			// Người dùng hiện tại chưa follow người dùng với id tương ứng
			return false;
		},
			$scope.followUser = function(followingId) {
				var currentUserId = $scope.UserInfo.userId;
				var data = {
					followerId: currentUserId,
					followingId: followingId
				};

				$http.post('/followOther', data)
					.then(function(response) {

						// Thêm follow mới đã chuyển đổi vào myListFollow
						$scope.myListFollow = response.data;

						// Cập nhật trạng thái follow và cập nhật giao diện
						$scope.updateFollowStatus();
						$scope.refreshFollowList();
					})
					.catch(function(error) {
						console.log(error);
					});
			};

		$scope.unfollowUser = function(followingId) {
			var currentUserId = $scope.UserInfo.userId;
			var data = {
				followerId: currentUserId,
				followingId: followingId
			};
			$http.delete('/unfollowOther', { data: data, headers: { 'Content-Type': 'application/json' } })
				.then(function(response) {
					// Cập nhật lại danh sách follow sau khi xóa thành công
					$scope.myListFollow = $scope.myListFollow.filter(function(follow) {
						return !(follow.followerId === currentUserId && follow.followingId === followingId);
					});
					console.log("Unfollow dc nhen")

					$scope.refreshFollowList(); // Cập nhật trạng thái isFollowing cho các người dùng còn lại
				})
				.catch(function(error) {
					console.log(error);
				});
		};


		$scope.updateFollowStatus = function() {
			var currentUserId = $scope.UserInfo.userId;
			for (var i = 0; i < $scope.myListFollow.length; i++) {
				$scope.myListFollow[i].isFollowing = $scope.isFollowing($scope.myListFollow[i].followingId);
			}
		};
		// Hàm làm mới danh sách follow
		$scope.refreshFollowList = function() {
			$http.get('/getallfollow')
				.then(function(response) {
					$scope.myListFollow = response.data;
				}, function(error) {
					// Xử lý lỗi
					console.log(error);
				});
		};


		$scope.changeBackground = function() {
			var formData = new FormData();
			var fileInput = document.getElementById('inputGroupFile02');

			for (var i = 0; i < fileInput.files.length; i++) {
				formData.append('photoFiles2', fileInput.files[i]);
			}
			formData.append('content', $scope.content);

			$http.post('/updateBackground', formData, {
				transformRequest: angular.identity,
				headers: {
					'Content-Type': undefined
				}
			}).then(function(response) {
				// Xử lý phản hồi thành công từ máy chủ (cập nhật ảnh bìa)
				$scope.content = '';
				alert("Đăng bài và cập nhật ảnh bìa thành công!");
			}, function(error) {
				// Xử lý lỗi
				console.log(error);
				alert("Đăng bài và cập nhật ảnh bìa thành công!");
			});
		};
		$scope.changeAvatar = function() {
			var formData = new FormData();
			var fileInput = document.getElementById('inputGroupFile03');

			for (var i = 0; i < fileInput.files.length; i++) {
				formData.append('photoFiles3', fileInput.files[i]);
			}
			formData.append('content', $scope.content);

			$http.post('/updateAvatar', formData, {
				transformRequest: angular.identity,
				headers: {
					'Content-Type': undefined
				}
			}).then(function(response) {
				// Xử lý phản hồi thành công từ máy chủ (cập nhật ảnh bìa)
				$scope.content = '';
				alert("Đăng bài và cập nhật ảnh đại diện thành công!");
			}, function(error) {
				// Xử lý lỗi
				console.log(error);
				alert("Đăng bài và cập nhật ảnh đại diện thành công1!");
			});
		};

		$http.get('/getListImage')
			.then(function(response) {
				// Dữ liệu trả về từ API sẽ nằm trong response.data
				$scope.imageList = response.data;
				$scope.displayedImages = $scope.imageList.slice(0, 9);
			})
			.catch(function(error) {
				console.log(error);
			});

		// Khởi tạo biến selectedPost
		$scope.selectedPost = {};

		// Hàm mở modal chỉnh sửa bài viết
		$scope.openEditModal = function(myPost) {
			// Gán giá trị cho selectedPost từ myPost được chọn
			$scope.selectedPost = myPost;
			console.log($scope.selectedPost);
			// Hiển thị modal
			$('#editModal').modal('show');
		};
		$scope.showImageModal = function(imageUrl) {
			// Gán đường dẫn ảnh vào thuộc tính src của thẻ img trong modal
			document.getElementById('modalImage').src = '/images/' + imageUrl;

			// Hiển thị modal
			$('#imageModal').modal('show');
		};
		$scope.updatePost = function(selectedPost) {
			$http.put('/updatePost/' + selectedPost.postId, selectedPost)
				.then(function(response) {
					// Xử lý phản hồi thành công từ server

					// Cập nhật lại bài viết trong mảng myPosts
					var index = $scope.myPosts.findIndex(function(MyPosts) {
						return MyPosts.postId === selectedPost.postId;
					});

					if (index !== -1) {
						$scope.myPosts[index].content = selectedPost.content;
					}
					Swal.fire({
						icon: 'success',
						title: 'Cập nhật thành công!',
						text: 'Bài viết đã được cập nhật thành công.',
						showConfirmButton: false,
						timer: 3000
					});
					// Đóng modal
					$('#editModal').modal('hide');
				})
				.catch(function(error) {
					// Xử lý lỗi
					console.log(error);
					Swal.fire({
						icon: 'error',
						title: 'Có lỗi xảy ra!',
						text: 'Đã có lỗi xảy ra khi cập nhật bài viết.',
						showConfirmButton: false,
						timer: 2000
					});
				});
		};


		$scope.hidePost = function(postId) {
			$http.put('/hide/' + postId)
				.then(function(response) {
					// Cập nhật trạng thái của bài viết trong danh sách myPosts
					var index = $scope.myPosts.findIndex(function(post) {
						return post.postId === postId;
					});

					if (index !== -1) {
						$scope.myPosts[index].isActive = false;
					}

					// Tính lại tổng số lượng ảnh
					$scope.totalImagesCount = $scope.myPosts.reduce(function(total, post) {
						return total + post.images.length;
					}, 0);

					// Hiển thị thông báo SweetAlert2
					Swal.fire({
						icon: 'success',
						title: 'Bài viết đã được ẩn thành công!',
						showConfirmButton: false,
						timer: 1500 // Thời gian hiển thị thông báo (1.5 giây)
					});
				})
				.catch(function(error) {
					// Xử lý lỗi
					console.log(error);

					// Hiển thị thông báo SweetAlert2 khi xảy ra lỗi
					Swal.fire({
						icon: 'error',
						title: 'Đã xảy ra lỗi khi ẩn bài viết!',
						text: 'Vui lòng thử lại sau.',
						showConfirmButton: true
					});
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

		//---------------------------------------------------------------------------
		$scope.goToProfile = function(userId) {
			$scope.Posts = [];
			$scope.likedPosts = [];
			$scope.myAccount = {};
			$scope.postData = {};
			$scope.postDetails = {};
			$scope.postComments = [];
			$scope.followers = [];
			$scope.followings = [];
			$scope.myPostImage = [];
			$scope.myListFollow = [];
			$scope.UserInfo = [];
			$scope.AccInfo = {};
			$scope.myPosts = [];
			$scope.selectedPostId = '';
			console.log(userId)
			$http.post('/getOtherUserId/' + userId)
				.then(function(response) {
					var UserInfo = response.data;
					$scope.UserInfo = UserInfo;
					console.log($scope.myUserId);
				})
			//
			$http.post('/countmypost/' + userId)
				.then(function(response) {
					var sumPost = response.data;
					$scope.sumPost = sumPost;
				})
			//
			$http.post('/findmyfollow/' + userId)
				.then(function(response) {
					var myAccount = response.data;
					$scope.myAccount = myAccount;
				})
			//
			$http.post('/getmypost/' + userId)
				.then(function(response) {
					var myPosts = response.data;
					$scope.myPosts = myPosts;

					$scope.totalImagesCount = myPosts.reduce(function(total, post) {
						return total + post.images.length;
					}, 0);
					console.log($scope.totalImagesCount)

				});
			//
			$http.post('/findaccounts/' + userId)
				.then(function(response) {
					var AccInfo = response.data;
					$scope.AccInfo = AccInfo;
				})
				.catch(function(error) {
					console.log(error);
				});
			//
			$http.post('/findmyfollowers/' + userId)
				.then(function(response) {
					$scope.followers = response.data;
					console.log($scope.followers); // Kiểm tra dữ liệu trong console log
				})
				.catch(function(error) {
					console.log(error);
				});
			//
			$http.post('/findmyfollowing/' + userId)
				.then(function(response) {
					$scope.followings = response.data;
					//console.log($scope.followings); // Kiểm tra dữ liệu trong console log
				})
				.catch(function(error) {
					console.log(error);
				});
			//
			$http.get('/findlikedposts')
				.then(function(response) {
					var likedPosts = response.data;
					$scope.likedPosts = likedPosts;
				})
				.catch(function(error) {
					console.log(error);
				});
			//
			$scope.isFollowing = function(followingId) {
				// Lấy id của người dùng hiện tại
				var currentUserId = $scope.myUserId;
				// Kiểm tra xem người dùng hiện tại đã follow người dùng với id tương ứng (followingId) chưa
				// Dựa vào danh sách các người dùng mà người dùng hiện tại đã follow
				// Trong ví dụ này, danh sách này có thể được lưu trữ trong cơ sở dữ liệu hoặc được lấy từ API
				var myListFollow = $scope.myListFollow;
				for (var i = 0; i < myListFollow.length; i++) {
					if (myListFollow[i].followingId === followingId && myListFollow[i].followerId === currentUserId) {
						// Người dùng hiện tại đã follow người dùng với id tương ứng
						return true;
					}
				}
				// Người dùng hiện tại chưa follow người dùng với id tương ứng
				return false;
			},

				//
			$scope.followUser = function(followingId) {
				var currentUserId = $scope.myUserId;
				var data = {
					followerId: currentUserId,
					followingId: followingId
				};

				$http.post('/followOther', data)
					.then(function(response) {

						// Thêm follow mới đã chuyển đổi vào myListFollow
						$scope.myListFollow = response.data;

						// Cập nhật trạng thái follow và cập nhật giao diện
						$scope.updateFollowStatus();
						$scope.refreshFollowList();
					})
					.catch(function(error) {
						console.log(error);
					});
			};

		$scope.unfollowUser = function(followingId) {
			var currentUserId = $scope.myUserId;
			var data = {
				followerId: currentUserId,
				followingId: followingId
			};
			$http.delete('/unfollowOther', { data: data, headers: { 'Content-Type': 'application/json' } })
				.then(function(response) {
					// Cập nhật lại danh sách follow sau khi xóa thành công
					$scope.myListFollow = $scope.myListFollow.filter(function(follow) {
						return !(follow.followerId === currentUserId && follow.followingId === followingId);
					});
					console.log("Unfollow dc nhen")

					$scope.refreshFollowList(); // Cập nhật trạng thái isFollowing cho các người dùng còn lại
				})
				.catch(function(error) {
					console.log(error);
				});
		};
			//
			$http.get('/getallfollow')
				.then(function(response) {
					var myListFollow = response.data;
					$scope.myListFollow = myListFollow;
				}, function(error) {
					// Xử lý lỗi
					console.log(error);
				});
			// Hàm làm mới danh sách follow
			$scope.refreshFollowList = function() {
				$http.get('/getallfollow')
					.then(function(response) {
						$scope.myListFollow = response.data;
					}, function(error) {
						// Xử lý lỗi
						console.log(error);
					});
			};

			//
			$http.post('/getListImage/' + userId)
				.then(function(response) {
					// Dữ liệu trả về từ API sẽ nằm trong response.data
					$scope.imageList = response.data;
					$scope.displayedImages = $scope.imageList.slice(0, 9);
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
						timer: 3000,
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
				$http.post('/user/report/' + userId + '/' + postId + '/' + $scope.selectedViolationType)
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


		};
	});

