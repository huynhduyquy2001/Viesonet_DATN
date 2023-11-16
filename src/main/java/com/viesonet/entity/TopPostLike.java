package com.viesonet.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TopPostLike {
    @Id
    private int stt;

    private String images;
    private int postId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date postDate;
    private String userPost;
    private String avatarUser;
    private String content;
    private int likeCount;
    private int commentCount;
}
