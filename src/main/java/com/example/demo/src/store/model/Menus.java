package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor

public class Menus {
    private int menuIdx;
    private String menuName;
    private String menuPrice;
    private String menuDescription;
    private String menuImage;
    private String isMuchOrder;
    private String isBestReview;
}
