app.controller(
	"UserManagerController",
	function ($scope, $http, $translate, $rootScope, $location) {
		$scope.listUsers = [];
		$scope.profile = [];

		$http.get('/admin/usermanager/load')
			.then(function(response) {
				$scope.listUsers = response.data;
			});

		$http.get('/admin/usermanager/profile')
			.then(function(response) {
				$scope.profile = response.data;
			});

		$scope.detailUser = function(userId) {
			var host = '/admin/usermanager/detailUser/' + userId;

			$http.get(host)
				.then(function(response) {
					$scope.profile = response.data;
				});
		}

		$scope.searchUser = function() {
			var search = $scope.searchValue;
			if ($scope.searchValue === '') {
				$scope.listUsers.content = originalList;
				return;
			}else{
				$http.get("/admin/usermanager/search/"+ search)
				.then(function(response) {
					$scope.listUsers = response.data;
				});
			}
			
		}

		$scope.searchClick = function() {
			var search = $scope.searchValue;
			var hostUser = '/admin/usermanager/searchId/' + search;

			$http.get(hostUser)
				.then(function(response) {
					$scope.profile = response.data;
				});
		}

		$scope.updateRole = async function(sdt, roleId) {
			const inputOptions = new Promise((resolve) => {
				setTimeout(() => {
					resolve({
						'2': 'Staff',
						'3': 'User'
					})
				}, 500)
			})

			const { value: role } = await Swal.fire({
				title: 'Chọn vai trò?',
				input: 'radio',
				icon: 'question',
				inputOptions: inputOptions,
				inputValidator: (value) => {
					if (!value) {
						return 'Chưa chọn vai trò!'
					}
				}
			})

			if (role) {
				const swalWithBootstrapButtons = Swal.mixin({
					customClass: {
						confirmButton: 'btn btn-success',
						cancelButton: 'btn btn-danger me-2'
					},
					buttonsStyling: false
				})

				swalWithBootstrapButtons.fire({
					text: "Bạn có chắc chắn muốn đổi vai trò không?",
					icon: 'warning',
					showCancelButton: true,
					confirmButtonText: 'Có, chắc chắn',
					cancelButtonText: 'Hủy',
					reverseButtons: true
				}).then((result) => {
					if (result.isConfirmed) {
						if (role == roleId) {
							Swal.fire(
								'Lỗi!',
								'Đã có vai trò này!',
								'warning'
							)
						} else {
							var url = '/admin/usermanager/userRole/' + sdt + '/' + role;
							$http.put(url)
								.then(function(response) {
									$scope.profile = response.data;
									swalWithBootstrapButtons.fire(
										'Thành công!',
										'Vai trò đã được cập nhật',
										'success'
									)
								})
								.catch(function(error) {
									// Xử lý lỗi
									console.log(error);
									Swal.fire(
										'Lỗi!',
										'Không thể cập nhật vai trò!',
										'error'
									)
								});
						}

					}
				});
			}
		}
		
		$scope.resetViolation = function(userId, violationCount){
			const swalWithBootstrapButtons = Swal.mixin({
					customClass: {
						confirmButton: 'btn btn-success',
						cancelButton: 'btn btn-danger me-2'
					},
					buttonsStyling: false
				})

				swalWithBootstrapButtons.fire({
					text: "Bạn có chắc chắn muốn gỡ lượt vi phạm không?",
					icon: 'question',
					showCancelButton: true,
					confirmButtonText: 'Có, chắc chắn',
					cancelButtonText: 'Hủy',
					reverseButtons: true
				}).then((result) => {
					if (result.isConfirmed && violationCount > 0) {
						var url = '/admin/usermanager/userViolations/' + userId;
							$http.put(url)
								.then(function(response) {
									$scope.profile = response.data;
									swalWithBootstrapButtons.fire(
										'Thành công!',
										'Đã gỡ lượt vi phạm',
										'success'
									)
								})
								.catch(function(error) {
									// Xử lý lỗi
									console.log(error);
									Swal.fire(
										'Lỗi!',
										'Không thể gỡ lượt vi phạm!',
										'error'
									)
								});
						}else{
								Swal.fire(
										'Lỗi!',
										'Người dùng hiện tại không có lượt vi phạm!',
										'warning'
									)
						}
				})
		}
		
	  //
	}
  );
  