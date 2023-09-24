package com.viesonet.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAndRatingValue {

    private int productId;
    private String productName;
    private float originalPrice;
    private int promotion;
    private String description;
    private int soldQuantity;

    private String userId;
    private String username;
    private String avatar;

    private List<Media> media;
    List<Integer> ratingValue;

}
