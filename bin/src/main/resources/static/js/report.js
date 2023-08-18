$(document).ready(function() {
	$('#mySelect').on('change', function() {
		var selectedValue = $(this).val();
		// Gửi giá trị đã chọn đến máy chủ bằng Ajax
		$.ajax({
			url: "/admin/report/filterYear/" + selectedValue, // Thay đổi đường dẫn tới máy chủ của bạn
			method: 'GET', // Hoặc 'GET' tùy vào phương thức gửi dữ liệu
			success: function(data) {
				let bieuDo = document.getElementById("chart");
				bieuDo.innerHTML = "";

				var dsBaiViet = data.listPosts;
				var dsToCao = data.listNumberReports;
				var soBaiViet = []
				var soToCao = []
				for (var i = 0; i < dsBaiViet.length; i++) {
					soBaiViet.push(dsBaiViet[i].violationPosts)
				}
				for (var i = 0; i < dsToCao.length; i++) {
					soToCao.push(dsToCao[i].numberReport)
				}
				var chart = {
					series: [
						{ name: "Số lượt tố cáo:", data: soToCao },
						{ name: "Số lượt vi phạm:", data: soBaiViet },
					],
					chart: {
						type: "bar",
						height: 440,
						offsetX: -15,
						toolbar: { show: true },
						foreColor: "#adb0bb",
						fontFamily: 'inherit',
						sparkline: { enabled: false },
					},
					colors: ["#5D87FF", "#49BEFF"],
					plotOptions: {
						bar: {
							horizontal: false,
							columnWidth: "75%",
							borderRadius: [6],
							borderRadiusApplication: 'end',
							borderRadiusWhenStacked: 'all'
						},
					},
					markers: { size: 0 },

					dataLabels: {
						enabled: false,
					},
					legend: {
						show: false,
					},
					grid: {
						borderColor: "rgba(0,0,0,0.1)",
						strokeDashArray: 3,
						xaxis: {
							lines: {
								show: false,
							},
						},
					},

					xaxis: {
						type: "category",
						categories: ["Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", " Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12",],
						labels: {
							style: { cssClass: "grey--text lighten-2--text fill-color" },
						},
					},


					yaxis: {
						show: true,
						min: 0,
						max: 20,
						tickAmount: 4,
						labels: {
							style: {
								cssClass: "grey--text lighten-2--text fill-color",
							},
						},
					},
					stroke: {
						show: true,
						width: 3,
						lineCap: "butt",
						colors: ["transparent"],
					},
					tooltip: { theme: "light" },
					responsive: [
						{
							breakpoint: 600,
							options: {
								plotOptions: {
									bar: {
										borderRadius: 3,
									}
								},
							}
						}
					]
				};
				var chart = new ApexCharts(document.querySelector("#chart"), chart);
				chart.render();
			},
			error: function(xhr, status, error) {
				// Xử lý lỗi (nếu có)
				console.error('Lỗi khi gửi dữ liệu:', error);
			}
		});
	});
});

function detail(postId) {
	$.ajax({
		url: "/admin/report/detail/" + postId, // Thay đổi đường dẫn tới máy chủ của bạn
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
			let datePosts = `${day}-${month}-${year}`;


			//Thêm nội dung lại cho form
			var avatar = `<img src="/images/${item.avatarUser}" alt="" width="55" height="55"
							class="rounded-circle mt-3" style="margin-left: 15px;">
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
   					 </div>`;});
					if(imageUrls.length > 1){
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
			
			var detailContent = `${item.content}
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
			$("#exampleModal").modal("show");
		},
		error: function(xhr, status, error) {
			// Xử lý lỗi (nếu có)
			console.error('Lỗi khi gửi dữ liệu:', error);
		}
	});
}
