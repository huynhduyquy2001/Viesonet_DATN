let host = "https://search-history-453d4-default-rtdb.firebaseio.com";
  angular.module('myApp',['pascalprecht.translate'])
  .config(function($translateProvider) {
		$translateProvider.useStaticFilesLoader({
			prefix: 'json/', // Thay đổi đường dẫn này cho phù hợp
			suffix: '.json'
		});
		// Set the default language
		// Set the default language
		var storedLanguage = localStorage.getItem('myAppLangKey') || 'vie';
		$translateProvider.preferredLanguage(storedLanguage);
	})
    .controller('myCtrl', function ($scope, $http, $translate) {
      $scope.Posts = [];
      $scope.likedPosts = [];
      $scope.myAccount = {};
      $scope.postData = {};
      $scope.item = {};
      $scope.listFollow=[];
      
      //Đa ngôn ngữ	
		$scope.changeLanguage = function(langKey) {
			$translate.use(langKey);
			localStorage.setItem('myAppLangKey', langKey); // Lưu ngôn ngữ đã chọn vào localStorage
		};
	  // đây là code tim kiếm người dùng (tất cả)
      $scope.searchUser = function() {
        var username = $scope.username; // Lấy tên người dùng từ input hoặc form

        // Gọi API để tìm kiếm người dùng
        $http.get('/search/users?username=' + username)
          .then(function(response) {
            // Xử lý kết quả trả về từ API
            $scope.users = response.data;
          })
          .catch(function(error) {
            console.log('Lỗi khi tìm kiếm người dùng:', error);
          });
      };
      //đây là code hiện lên lịch sử người dùng
      var url = `${host}/history.json`;
      $http.get(url).
	 	then(resp=>{ 
 	 		$scope.items = resp.data;
  			console.log("Load OK", response);
		}).catch(function(error) {
  		console.log("Load Error", error);
			});
      //đây là code xóa lịch sử tìm kiếm
      $scope.deleteLS = function(key){
    	  var url = `${host}/history/${key}.json`;
    	  $http.delete(url).then(resp=>{
    		  delete $scope.items[key];
    		  console.log("Xóa OK", response);
    	  }).catch(function(error) {
    	  		console.log("Xóa Error", error);
			});
      }
      //Đơn giản là check input
      $scope.checkInput = function () {
          // Kiểm tra nếu người dùng không nhập dữ liệu
          if (!$scope.username) {
              $scope.showError = true;
          } else {
              $scope.showError = false;
              $scope.LS = function(){
            	  var item = angular.copy($scope.username);
                  var url = `${host}/history.json`;

                  $http.post(url, { username: item })
                    .then(function(response) {
                      $scope.key = response.data.name;
                      $scope.item[$scope.key] = item;
                      console.log("OK", response);
                    })
                    .catch(function(error) {
                      console.log("Error", error);
                    });
              }
          }
      };
      $scope.resetError = function () {
          // Ẩn thông báo lỗi khi người dùng nhập lại
          $scope.showError = false;
      };
      
      $http.get('/ListFollowing')
      .then(function(response) {
        $scope.followings = response.data;
        console.log($scope.followings); // Kiểm tra dữ liệu trong console log
      })
      .catch(function(error) {
        console.log(error);
  	});
      $http.get('/getUserInfo').then(function(response) {
			$scope.UserInfo = response.data;
			$scope.birthday = new Date($scope.UserInfo.birthday)
			// Khởi tạo biến $scope.UpdateUser để lưu thông tin cập nhật

			$scope.UpdateUser = angular.copy($scope.UserInfo);

		});
    $scope.myGallary = function() {
			var currentUserId = $scope.UserInfo.userId;

		}
	$scope.followUser = function(followingId) {
		var currentUserId = $scope.UserInfo.userId;
		var data = {
			followerId: currentUserId,
			followingId: followingId
		};
		$http.post('/follow', data)
			.then(function(response) {
				// Thêm follow mới thành công, cập nhật trạng thái trong danh sách và chuyển nút thành "Unfollow"
				$scope.listFollow.push(response.data);
				console.log("Success Follow!");

				// Cập nhật lại danh sách follow sau khi thêm mới thành công
				$scope.refreshFollowList();
			})
			.catch(function(error) {
				console.log("Lỗi F",error);
			});
	};

	$scope.unfollowUser = function(followingId) {
		var currentUserId = $scope.UserInfo.userId;
		var data = {
			followerId: currentUserId,
			followingId: followingId
		};
		$scope.refreshFollowList();
		$http.delete('/unfollow', { data: data, headers: { 'Content-Type': 'application/json' } })
			.then(function(response) {
				// Cập nhật lại danh sách follow sau khi xóa thành công
				$scope.listFollow = $scope.listFollow.filter(function(follow) {
					console.log("Success Unfollow!");
					$scope.refreshFollowList();
					return !(follow.followerId === currentUserId && follow.followingId === followingId);
					
				});
			}, function(error) {
				console.log("Lỗi UnF",error);
			});
	};
	
	// Hàm làm mới danh sách follow
	$scope.refreshFollowList = function() {
		$http.get('/ListFollowing')
	      .then(function(response) {
	        $scope.followings = response.data;
	        console.log($scope.followings); // Kiểm tra dữ liệu trong console log
	      })
	      .catch(function(error) {
	        console.log(error);
	  	});	
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
    });