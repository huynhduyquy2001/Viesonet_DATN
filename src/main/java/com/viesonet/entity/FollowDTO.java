package com.viesonet.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FollowDTO {
    private int followId;
    private String followerId;
    private String followingId;
    private Date followDate;
    // Các phương thức getter và setter cho các trường
}

