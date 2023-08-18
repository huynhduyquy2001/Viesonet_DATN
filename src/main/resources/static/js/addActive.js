setTimeout(function() {
	const menuLinks = document.querySelectorAll("#sidebarnav .sidebar-link");

	// Lặp qua danh sách liên kết
	for (let i = 0; i < menuLinks.length; i++) {
		const link = menuLinks[i];
		link.addEventListener("click", function() {
			// Gỡ bỏ lớp "active-link" khỏi tất cả các liên kết
			menuLinks.forEach(link => link.classList.remove("active"));

			// Thêm lớp "active-link" vào liên kết được nhấp vào
			link.classList.add("active");
		});
	}
}, 100);
