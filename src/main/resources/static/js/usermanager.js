const input = document.getElementById("search");
//Sự kiện nhấn phím
input.addEventListener("keyup", function(event) {
	var searchText = input.value;
	if (!searchText) {
		searchText = "all";
	}
	$.ajax({
		url: "/admin/usermanager/search/" + searchText,
		method: 'GET',
		success: function(data) {
			//Xóa form
			var nav = document.getElementById("Nav")
			nav.innerHTML = "";

			//Xét điều kiệu để hiển thị
			if (data.length == 0) {
				//Thêm nội dung vào form
				nav.innerHTML += `<li class="sidebar-item sidebar-scrollable">
                          <div class="text-center">
                             <h5><i class="fa-solid fa-triangle-exclamation" style="color: #e14023;"></i> 
                             Không có người dùng này</h5>
                          </div>
                      </li>`;
			} else {
				//Thêm nội dung vào form
				data.forEach(function(item) {
					var icon = ``;
					if (item[4] >= 5) {
						icon = ` <i class="fa-solid fa-circle-exclamation fa-sm mt-1" style="color: #e12814;"></i>`;
					}
					nav.innerHTML += `<li class="sidebar-item">
										<a href="#" onclick="detailUser('${item[0]}')" class="image-link sidebar-link mb-3"> 
											<img src="/images/${item[1]}" width="55" height="55"
												class="rounded-circle" style="object-fit: cover;">
												<div class="content mt-1">
													<span>${item[2]} &nbsp; ${icon}</span>
													<small>${item[3]}</small>
												</div>
										</a> 
										</li>`;
				})
			}
		},
		error: function(xhr, status, error) {
			console.error('Lỗi khi gửi dữ liệu:', error);
		}
	});
});

function detailUser(userId) {
	$.ajax({
		url: "/admin/usermanager/detailUser/" + userId,
		method: 'GET',
		success: function(data) {
			var fn = document.getElementById("function")
			var detail = document.getElementById("card_user")
			//Xóa form
			fn.innerHTML = "";
			detail.innerHTML = "";

			//Thêm nội dung vào form
			if (data[7] != "Admin") {
				fn.innerHTML += `<ul class="navbar-nav flex-row ms-auto align-items-center justify-content-end me-3">
                      <li class="nav-item dropdown">
                        <a class="nav-link nav-icon-hover" href="#" id="drop2" data-bs-toggle="dropdown"
                          aria-expanded="false">
                          <i class="fa-solid fa-user-pen"></i>
                        </a>
                        <div style="background: whitesmoke;" class="dropdown-menu dropdown-menu-end dropdown-menu-animate-up" aria-labelledby="drop2">
                          <div class="message-body">
                            <a href="#" onclick="showToastRole('${data[0]}', '${data[5]}')" class="d-flex align-items-center gap-2 dropdown-item">
                              <i class="fa-solid fa-repeat-1 me-3"></i>
                              <p class="mb-0 fs-3">Đổi vai trò</p>
                            </a>
                            <a href="#" onclick="showToastViolations('${data[0]}')" class="d-flex align-items-center gap-2 dropdown-item">
                              <i class="fa-sharp fa-regular fa-bug-slash me-3"></i>
                              <p class="mb-0 fs-3">Gỡ vi phạm</p>
                            </a>
                          </div>
                        </div>
                      </li>
                    </ul>`;
			}
			//Chuyển định dạng ngày tháng năm
			let date = new Date(data[3]);
			let day = date.getUTCDate().toString().padStart(2, '0');
			let month = (date.getUTCMonth() + 1).toString().padStart(2, '0');
			let year = date.getUTCFullYear().toString();
			let birthday = `${day}/${month}/${year}`;
			detail.innerHTML += `<div class="sidebar-scrollable">
									<div class="image-link">
										<img src="/images/${data[1]}" alt="" width="100" height="100"
											class="rounded-circle"
											style="object-fit: cover; border: 2px solid #ccc;">
										<div class="content mb-2">
											<span><h3>
													<b>${data[2]}</b>
												</h3></span> <small class="mb-2">${birthday}</small> 
												<small>${data[4] ? 'Nam' : 'Nữ'}</small>
										</div>
									</div>
									<div></div>
									<div class="card-footer mt-3" style="line-height: 25px;">
										<div class="row">
										<div class="col-4">
											<small>Số điện thoại</small> <br> <b>${data[5]}</b>
										</div>
										<div class="col-8">
											<small>Mối quan hệ</small> <br> <b>${data[6]}</b>
										</div>
										<div class="col-3 mt-4">
											<small>Vai trò</small> <br> <b>${data[7]}</b>
										</div>
										<div class="col-9 mt-4">
											<small>Địa chỉ</small> <br> <b>${data[8]}</b>
										</div>
										<div class="col-3 mt-4">
											<small>Lượt vi phạm</small> <br> <b>${data[9]}</b>
										</div>
										<div class="col-3 mt-4">
											<small>Trạng thái</small> <br> <b>${data[10]}</b>
										</div>
										<div class="col-6 mt-4">
											<small>Email</small> <br> <b>${data[11]}</b>
										</div>
										<div class="col-12 mt-4">
											<small>Mô tả</small> <br>
											<p class="sidebar-scrollable">${data[12]}</p>
										</div>
									</div>
								</div>`;

		},
		error: function(xhr, status, error) {
			console.error('Lỗi khi gửi dữ liệu:', error);
		}
	});
}


async function showToastRole(id, sdt) {
	//Các option để chọn
	const inputOptions = {
		'2': 'Staff',
		'3': 'User',
	};
	//Mở toast
	const { value: role } = await Swal.fire({
		title: 'Chọn vai trò',
		input: 'radio',
		showCancelButton: true,
		cancelButtonColor: '#d33',
		confirmButtonColor: '#159b59',
		cancelButtonText: 'Hủy',
		confirmButtonText: 'Chọn',
		inputOptions: inputOptions,
		inputValidator: (value) => {
			if (!value) {
				return 'Hãy chọn vai trò!';
			}
		}
	});
	if (role) {
		Swal.fire({
			text: 'Bạn có chắc muốn đổi vai trò không?',
			icon: 'warning',
			confirmButtonText: 'Có, chắc chắn',
			showCancelButton: true,
			confirmButtonColor: '#159b59',
			cancelButtonColor: '#d33'
		}).then((result) => {
			if (result.isConfirmed) {
				//Thực hiện ajax
				$.ajax({
					url: "/admin/usermanager/userRole/" + role + "/" + id +"/" + sdt,
					method: 'GET',
					success: function(data) {
						//Kiểm tra có giống vai trò đã có không
						if(data == "warning"){
							Swal.fire({
							position: 'top',
							icon: 'warning',
							text: 'Người dùng đã có vai trò này!',
							showConfirmButton: false,
							timer: 1800
						})
						  return;
						}
						
						
						var fn = document.getElementById("function")
						var detail = document.getElementById("card_user")
						//Xóa form
						fn.innerHTML = "";
						detail.innerHTML = "";

						//Thêm nội dung vào form
						if (data[7] != "Admin") {
							fn.innerHTML += `<ul class="navbar-nav flex-row ms-auto align-items-center justify-content-end me-3">
                      	<li class="nav-item dropdown">
                        <a class="nav-link nav-icon-hover" href="#" id="drop2" data-bs-toggle="dropdown"
                          aria-expanded="false">
                          <i class="fa-solid fa-user-pen"></i>
                        </a>
                        <div style="background: whitesmoke;" class="dropdown-menu dropdown-menu-end dropdown-menu-animate-up" aria-labelledby="drop2">
                          <div class="message-body">
                            <a href="#" onclick="showToastRole('${data[0]}', '${data[5]}')" class="d-flex align-items-center gap-2 dropdown-item">
                              <i class="fa-solid fa-repeat-1 me-3"></i>
                              <p class="mb-0 fs-3">Đổi vai trò</p>
                            </a>
                            <a href="#" onclick="showToastViolations('${data[0]}')" class="d-flex align-items-center gap-2 dropdown-item">
                              <i class="fa-sharp fa-regular fa-bug-slash me-3"></i>
                              <p class="mb-0 fs-3">Gỡ vi phạm</p>
                            </a>
                          </div>
                        </div>
                      </li>
                    </ul>`;
						}
						//Chuyển định dạng ngày tháng năm
						let date = new Date(data[3]);
						let day = date.getUTCDate().toString().padStart(2, '0');
						let month = (date.getUTCMonth() + 1).toString().padStart(2, '0');
						let year = date.getUTCFullYear().toString();
						let birthday = `${day}/${month}/${year}`;
						detail.innerHTML += `<div class="sidebar-scrollable">
									<div class="image-link">
										<img src="/images/${data[1]}" alt="" width="100" height="100"
											class="rounded-circle"
											style="object-fit: cover; border: 2px solid #ccc;">
										<div class="content mb-2">
											<span><h3>
													<b>${data[2]}</b>
												</h3></span> <small class="mb-2">${birthday}</small> 
												<small>${data[4] ? 'Nam' : 'Nữ'}</small>
										</div>
									</div>
									<div></div>
									<div class="card-footer mt-3" style="line-height: 25px;">
										<div class="row">
										<div class="col-4">
											<small>Số điện thoại</small> <br> <b>${data[5]}</b>
										</div>
										<div class="col-8">
											<small>Mối quan hệ</small> <br> <b>${data[6]}</b>
										</div>
										<div class="col-3 mt-4">
											<small>Vai trò</small> <br> <b>${data[7]}</b>
										</div>
										<div class="col-9 mt-4">
											<small>Địa chỉ</small> <br> <b>${data[8]}</b>
										</div>
										<div class="col-3 mt-4">
											<small>Lượt vi phạm</small> <br> <b>${data[9]}</b>
										</div>
										<div class="col-3 mt-4">
											<small>Trạng thái</small> <br> <b>${data[10]}</b>
										</div>
										<div class="col-6 mt-4">
											<small>Email</small> <br> <b>${data[11]}</b>
										</div>
										<div class="col-12 mt-4">
											<small>Mô tả</small> <br>
											<p class="sidebar-scrollable">${data[12]}</p>
										</div>
									</div>
								</div>`;

						Swal.fire({
							position: 'top',
							icon: 'success',
							text: 'Đổi vai trò thành công',
							showConfirmButton: false,
							timer: 1800
						})
					},
					error: function(xhr, status, error) {
						Swal.fire({
							position: 'top',
							icon: 'error',
							text: 'Đổi vai trò thất bại!',
							showConfirmButton: false,
							timer: 1800
						})
						console.error('Lỗi khi gửi dữ liệu:', error);
					}
				});
			}
		})
	}
}


function showToastViolations(id) {
	Swal.fire({
			text: 'Bạn có chắc muốn gỡ vi phạm không?',
			icon: 'warning',
			confirmButtonText: 'Có, chắc chắn',
			showCancelButton: true,
			confirmButtonColor: '#159b59',
			cancelButtonColor: '#d33'
		}).then((result) => {
			if (result.isConfirmed) {
				//Thực hiện ajax
				$.ajax({
					url: "/admin/usermanager/userViolations/" + id ,
					method: 'GET',
					success: function(data) {
						//Kiểm tra có giống vai trò đã có không
						if(data == "warning"){
							Swal.fire({
							position: 'top',
							icon: 'warning',
							text: 'Người dùng không có lượt vi phạm để gỡ!',
							showConfirmButton: false,
							timer: 1800
						})
						  return;
						}
						
						
						var fn = document.getElementById("function")
						var detail = document.getElementById("card_user")
						//Xóa form
						fn.innerHTML = "";
						detail.innerHTML = "";

						//Thêm nội dung vào form
						if (data[7] != "Admin") {
							fn.innerHTML += `<ul class="navbar-nav flex-row ms-auto align-items-center justify-content-end me-3">
                      	<li class="nav-item dropdown">
                        <a class="nav-link nav-icon-hover" href="#" id="drop2" data-bs-toggle="dropdown"
                          aria-expanded="false">
                          <i class="fa-solid fa-user-pen"></i>
                        </a>
                        <div style="background: whitesmoke;" class="dropdown-menu dropdown-menu-end dropdown-menu-animate-up" aria-labelledby="drop2">
                          <div class="message-body">
                            <a href="#" onclick="showToastRole('${data[0]}', '${data[5]}')" class="d-flex align-items-center gap-2 dropdown-item">
                              <i class="fa-solid fa-repeat-1 me-3"></i>
                              <p class="mb-0 fs-3">Đổi vai trò</p>
                            </a>
                            <a href="#" onclick="showToastViolations('${data[0]}')" class="d-flex align-items-center gap-2 dropdown-item">
                              <i class="fa-sharp fa-regular fa-bug-slash me-3"></i>
                              <p class="mb-0 fs-3">Gỡ vi phạm</p>
                            </a>
                          </div>
                        </div>
                      </li>
                    </ul>`;
						}
						//Chuyển định dạng ngày tháng năm
						let date = new Date(data[3]);
						let day = date.getUTCDate().toString().padStart(2, '0');
						let month = (date.getUTCMonth() + 1).toString().padStart(2, '0');
						let year = date.getUTCFullYear().toString();
						let birthday = `${day}/${month}/${year}`;
						detail.innerHTML += `<div class="sidebar-scrollable">
									<div class="image-link">
										<img src="/images/${data[1]}" alt="" width="100" height="100"
											class="rounded-circle"
											style="object-fit: cover; border: 2px solid #ccc;">
										<div class="content mb-2">
											<span><h3>
													<b>${data[2]}</b>
												</h3></span> <small class="mb-2">${birthday}</small> 
												<small>${data[4] ? 'Nam' : 'Nữ'}</small>
										</div>
									</div>
									<div></div>
									<div class="card-footer mt-3" style="line-height: 25px;">
										<div class="row">
										<div class="col-4">
											<small>Số điện thoại</small> <br> <b>${data[5]}</b>
										</div>
										<div class="col-8">
											<small>Mối quan hệ</small> <br> <b>${data[6]}</b>
										</div>
										<div class="col-3 mt-4">
											<small>Vai trò</small> <br> <b>${data[7]}</b>
										</div>
										<div class="col-9 mt-4">
											<small>Địa chỉ</small> <br> <b>${data[8]}</b>
										</div>
										<div class="col-3 mt-4">
											<small>Lượt vi phạm</small> <br> <b>${data[9]}</b>
										</div>
										<div class="col-3 mt-4">
											<small>Trạng thái</small> <br> <b>${data[10]}</b>
										</div>
										<div class="col-6 mt-4">
											<small>Email</small> <br> <b>${data[11]}</b>
										</div>
										<div class="col-12 mt-4">
											<small>Mô tả</small> <br>
											<p class="sidebar-scrollable">${data[12]}</p>
										</div>
									</div>
								</div>`;

						Swal.fire({
							position: 'top',
							icon: 'success',
							text: 'Gỡ vi phạm thành công',
							showConfirmButton: false,
							timer: 1800
						})
					},
					error: function(xhr, status, error) {
						Swal.fire({
							position: 'top',
							icon: 'error',
							text: 'Gỡ vi phạm thất bại!',
							showConfirmButton: false,
							timer: 1800
						})
						console.error('Lỗi khi gửi dữ liệu:', error);
					}
				});
			}
		})
}
