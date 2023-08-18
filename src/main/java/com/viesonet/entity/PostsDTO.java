package com.viesonet.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostsDTO {
	private Long postId;
    private String postContent;
    // Thêm các thuộc tính khác của bài viết cần thiết

    private List<Images> images;
}
