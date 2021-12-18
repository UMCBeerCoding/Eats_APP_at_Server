package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.List;

@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor

public class GetCatRes {
    private int storeIdx;
    private String storeName;
    private float rating;
    private int reviewNum;
    private String distance;
    private String deliveryTip;
    private String deliveryTime;
    private List<String> storeImg;
}
