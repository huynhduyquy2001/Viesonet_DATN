const selectAllCheckbox = document.getElementById('selectAll');
const tableCheckboxes = document.querySelectorAll('.table-checkbox');
const selectedCountElement = document.getElementById('selectedCount');
const myHidden = document.querySelector('.my-hidden');
selectAllCheckbox.addEventListener('click', function() {
	const isChecked = this.checked;
	tableCheckboxes.forEach((checkbox) => {
		checkbox.checked = isChecked;
	});
	updateSelectedCount();

	// Hiển thị hoặc ẩn phần tử dựa trên trạng thái của checkbox
	if (isChecked) {
		myHidden.style.display = 'block'; // Hiển thị phần tử
	} else {
		myHidden.style.display = 'none'; // Ẩn phần tử
	}
});

tableCheckboxes.forEach(checkbox => checkbox.addEventListener('click', function() {
	updateSelectedCount();
	// Hiển thị hoặc ẩn phần tử dựa trên trạng thái của checkbox
	if (checkbox.checked) {
		myHidden.style.display = 'block'; // Hiển thị phần tử
	} else {
		myHidden.style.display = 'none'; // Ẩn phần tử
	}
}));
function updateSelectedCount() {
	const selectedCount = document.querySelectorAll('.table-checkbox:checked').length;
	selectedCountElement.textContent = selectedCount > 0 ? selectedCount : '';
}

function detail(postId) {
	//1
	$.ajax({
		url: "/admin/postsviolations/detailPost/" + postId, // Thay đổi đường dẫn tới máy chủ của bạn
		method: 'GET', // Hoặc 'GET' tùy vào phương thức gửi dữ liệu
		success: function(item) {
			//Xóa trống form
			let t2 = document.getElementById("detailContent");
			let t3 = document.getElementById("avatar");
			t2.innerHTML = "";
			t3.innerHTML = "";

			//Chuyển định dạng ngày tháng năm
			let date = new Date(item.postDate);
			let day = date.getUTCDate().toString().padStart(2, '0');
			let month = (date.getUTCMonth() + 1).toString().padStart(2, '0');
			let year = date.getUTCFullYear().toString();
			let datePosts = `${day}/${month}/${year}`;


			//Thêm nội dung lại cho form
			var avatar = `<img src="/images/${item.avatarUser}" alt="" width="55" height="55"
							class="rounded-circle mt-3">
							<div class="content mt-3">
							<span class="name">${item.userPost}</span> 
									<small>${datePosts}</small>
							</div>`;
			//Xét điều kiện nếu ảnh rỗng 
			var img
			if (item.images === null) {
				img = `<br>`;
			} else {
				//Lấy ra từng ảnh
				var imgs = item.images;
				var imageUrls = imgs.split(", ");

				var img = `<div id="carouselExampleFade" class="carousel slide carousel-fade carousel-dark">
 							 <div class="carousel-inner" style="width: 75%;">`;

				imageUrls.forEach(function(imageUrl, index) {
					var activeClass = index === 0 ? "active" : "";
					img += `
   					 <div class="carousel-item ${activeClass}">
      					<img width="75%" src="/images/${imageUrl}" class="d-block w-100" alt="">
   					 </div>`;
				});
				if (imageUrls.length > 1) {
					img += `
					  </div>
					  <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleFade" data-bs-slide="prev">
					    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
					  </button>
					  <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleFade" data-bs-slide="next">
					    <span class="carousel-control-next-icon" aria-hidden="true"></span>
					  </button>
					</div>`;
				}
			}

			var detailContent = `&nbsp;&nbsp; ${item.content}
							<center style="margin-right: 20px; margin-top: 10px;">
								${img}
							</center> <br>
							<div class="post-reaction">
								<div class="activity-icons d-flex justify-content">
									<div>
										<i class="fa-regular fa-thumbs-up"></i> &nbsp; ${item.likeCount}
									</div>
									&nbsp;&nbsp;&nbsp;&nbsp;
									<div>
										<i class="fa-regular fa-comment"></i>&nbsp; ${item.commentCount}
									</div>
								</div>
							</div>
							<br>
							`;
			t2.innerHTML += detailContent;
			t3.innerHTML += avatar;
		},
		error: function(xhr, status, error) {
			// Xử lý lỗi (nếu có)
			console.error('Lỗi khi gửi dữ liệu:', error);
		}
	});

	//2
	$.ajax({
		url: "/admin/postsviolations/detailViolation/" + postId, // Thay đổi đường dẫn tới máy chủ của bạn
		method: 'GET', // Hoặc 'GET' tùy vào phương thức gửi dữ liệu
		success: function(data) {
			var tr = document.getElementById("listViolation");
			tr.innerHTML = "";

			data.forEach(function(item) {
				tr.innerHTML += `<tr>
                          			<td>${item[0]}</td>
                          			<td class="text-center">${item[1]}</td>
                        		 </tr>`;
			});

			$("#exampleModal").modal("show");
		},
		error: function(xhr, status, error) {
			// Xử lý lỗi (nếu có)
			console.error('Lỗi khi gửi dữ liệu:', error);
		}
	});
}

const input = document.getElementById("search");
//Sự kiện nhấn phím
input.addEventListener("keyup", function(event) {
	var searchText = input.value;
	if (!searchText) {
		location.reload();
	}
	$.ajax({
		url: "/admin/postsviolations/search/" + searchText,
		method: 'GET',
		success: function(data) {
			var table = document.getElementById("listTable");
			table.innerHTML = "";

			data.forEach(function(item) {
				//Chuyển định dạng ngày tháng năm
				let date = new Date(item[3]);
				let day = date.getUTCDate().toString().padStart(2, '0');
				let month = (date.getUTCMonth() + 1).toString().padStart(2, '0');
				let year = date.getUTCFullYear().toString();
				let dateFormat = `${day}/${month}/${year}`;
				table.innerHTML += `<tr>
									<td class="border-bottom-0"><input type="checkbox"
										value="${item[0]}"
										class="table-checkbox form-check-input hover-effect">
									</td>
									<td class="border-bottom-0" style="max-width: 250px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;">
									<span class="fw-normal mb-0">
									${item[1]}
									</span>
									</td>
									<td class="border-bottom-0"
										style="max-width: 200px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;">
										<span class="fw-normal">${item[2]}</span>
									</td>
									<td class="border-bottom-0">
										<p class="mb-0 fw-normal">${dateFormat}</p>
									</td>
									<td class="border-bottom-0 text-center"><a href="#"
										data-bs-placement="top" title="Xem chi tiết bài viết"
										th:onclick="detail('${item[0]}')"> <i
											class="fa-light fa-eye" style="color: #336cce;"></i>
									</a></td>
								</tr>`;
			});

			const selectAllCheckbox = document.getElementById('selectAll');
			const tableCheckboxes = document.querySelectorAll('.table-checkbox');
			const selectedCountElement = document.getElementById('selectedCount');
			const myHidden = document.querySelector('.my-hidden');
			selectAllCheckbox.addEventListener('click', function() {
				const isChecked = this.checked;
				tableCheckboxes.forEach((checkbox) => {
					checkbox.checked = isChecked;
				});
				updateSelectedCount();

				// Hiển thị hoặc ẩn phần tử dựa trên trạng thái của checkbox
				if (isChecked) {
					myHidden.style.display = 'block'; // Hiển thị phần tử
				} else {
					myHidden.style.display = 'none'; // Ẩn phần tử
				}
			});

			tableCheckboxes.forEach(checkbox => checkbox.addEventListener('click', function() {
				updateSelectedCount();
				// Hiển thị hoặc ẩn phần tử dựa trên trạng thái của checkbox
				if (checkbox.checked) {
					myHidden.style.display = 'block'; // Hiển thị phần tử
				} else {
					myHidden.style.display = 'none'; // Ẩn phần tử
				}
			}));

			function updateSelectedCount() {
				const selectedCount = document.querySelectorAll('.table-checkbox:checked').length;
				selectedCountElement.textContent = selectedCount > 0 ? selectedCount : '';
			}
		},
		error: function(xhr, status, error) {
			console.error('Lỗi khi gửi dữ liệu:', error);
		}
	});
});

function deleteViolation() {

	const tableCheckboxes = document.querySelectorAll('.table-checkbox');
	var listPostId = [];
	tableCheckboxes.forEach(checkbox => {
		if (checkbox.checked) {
			listPostId.push(`${checkbox.value}`);
		}
	});
	if (listPostId == 0) {
		Swal.fire({
			position: 'top',
			icon: 'warning',
			text: 'Chưa chọn bài viết vi phạm để xóa!',
			showConfirmButton: false,
			timer: 1800
		})
	} else {

		Swal.fire({
			text: 'Bạn có chắc muốn xóa bài viết vi phạm không?',
			icon: 'warning',
			confirmButtonText: 'Có, chắc chắn',
			showCancelButton: true,
			confirmButtonColor: '#159b59',
			cancelButtonColor: '#d33'
		}).then((result) => {
			if (result.isConfirmed) {
				$.ajax({
					url: "/admin/postsviolations/delete", // Thay đổi đường dẫn tới máy chủ của bạn
					method: 'POST', // Hoặc 'GET' tùy vào phương thức gửi dữ liệu
					data: JSON.stringify(listPostId),
					contentType: "application/json",
					success: function(data) {
						/*	var table = document.getElementById("listTable");
							table.innerHTML = "";
							data.content.forEach(function(item) {
								//Chuyển định dạng ngày tháng năm
								let date = new Date(item[3]);
								let day = date.getUTCDate().toString().padStart(2, '0');
								let month = (date.getUTCMonth() + 1).toString().padStart(2, '0');
								let year = date.getUTCFullYear().toString();
								let dateFormat = `${day}/${month}/${year}`;
								table.innerHTML += `<tr>
										<td class="border-bottom-0"><input type="checkbox"
											value="${item[0]}"
											class="table-checkbox form-check-input hover-effect">
										</td>
										<td class="border-bottom-0">
										<span class="fw-normal mb-0"
											style="max-width: 250px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;">
										${item[1]}
										</span>
										</td>
										<td class="border-bottom-0"
											style="max-width: 200px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;">
											<span class="fw-normal">${item[2]}</span>
										</td>
										<td class="border-bottom-0">
											<p class="mb-0 fw-normal">${dateFormat}</p>
										</td>
										<td class="border-bottom-0 text-center"><a href="#"
											data-bs-placement="top" title="Xem chi tiết bài viết"
											th:onclick="detail('${item[0]}')"> <i
												class="fa-light fa-eye" style="color: #336cce;"></i>
										</a></td>
									</tr>`; }); */
						Swal.fire({
							position: 'top',
							icon: 'success',
							text: 'Xóa bài viết vi phạm thành công!',
							showConfirmButton: false,
							timer: 1800
						})

						setTimeout(reloadPage, 1800);

					},
					error: function(xhr, status, error) {
						// Xử lý lỗi (nếu có)
						console.error('Lỗi khi gửi dữ liệu:', error);
						Swal.fire({
							position: 'top',
							icon: 'error',
							text: 'Không xóa được bài viết vi phạm!',
							showConfirmButton: false,
							timer: 1800
						})
					}
				});
			}
		});
		//
	}
	//
}

function reloadPage() {
	location.reload();
}


