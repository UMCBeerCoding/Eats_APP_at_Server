package com.example.demo.src.like.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckLike {
    private int favIdx;
    private String status;
}
