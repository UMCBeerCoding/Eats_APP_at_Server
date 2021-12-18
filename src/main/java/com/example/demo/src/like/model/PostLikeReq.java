package com.example.demo.src.like.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeReq {
    private Integer userIdx;
    private Integer storeIdx;
}
