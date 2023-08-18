// Hàm xử lý khi trang đã tải xong với độ trễ 100 mili-giây
  setTimeout(function () {
    // Lấy URL hiện tại
    const currentURL = window.location.href;

    // Lấy tất cả các thẻ <a> trong menu
    const menuLinks = document.querySelectorAll("#sidebarnav .sidebar-link");

    // Lặp qua tất cả các thẻ <a> và thêm sự kiện click cho mỗi thẻ
    menuLinks.forEach(link => {
      // Lấy href của thẻ <a>
      const linkURL = link.getAttribute("href");

      // So sánh URL hiện tại với href của thẻ <a>
      if (currentURL.includes(linkURL)) {
        // Thêm lớp "active" vào thẻ <a> nếu URL chứa href của thẻ
        link.classList.add("active");
      }
    });
  }, 100);