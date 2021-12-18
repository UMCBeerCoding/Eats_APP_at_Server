package com.example.demo.src.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.store.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController // Rest API 또는 WebAPI를 개발하기 위한 어노테이션. @Controller + @ResponseBody 를 합친것.
// @Controller      [Presentation Layer에서 Contoller를 명시하기 위해 사용]
//  [Presentation Layer?] 클라이언트와 최초로 만나는 곳으로 데이터 입출력이 발생하는 곳
//  Web MVC 코드에 사용되는 어노테이션. @RequestMapping 어노테이션을 해당 어노테이션 밑에서만 사용할 수 있다.
// @ResponseBody    모든 method의 return object를 적절한 형태로 변환 후, HTTP Response Body에 담아 반환.
@RequestMapping("/app/stores")
// method가 어떤 HTTP 요청을 처리할 것인가를 작성한다.
// 요청에 대해 어떤 Controller, 어떤 메소드가 처리할지를 맵핑하기 위한 어노테이션
// URL(/app/users)을 컨트롤러의 메서드와 매핑할 때 사용

public class StoreController {
    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************

    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.

    @Autowired  // 객체 생성을 스프링에서 자동으로 생성해주는 역할. 주입하려 하는 객체의 타입이 일치하는 객체를 자동으로 주입한다.
    // IoC(Inversion of Control, 제어의 역전) / DI(Dependency Injection, 의존관계 주입)에 대한 공부하시면, 더 깊이 있게 Spring에 대한 공부를 하실 수 있을 겁니다!(일단은 모르고 넘어가셔도 무방합니다.)
    // IoC 간단설명,  메소드나 객체의 호출작업을 개발자가 결정하는 것이 아니라, 외부에서 결정되는 것을 의미
    // DI 간단설명, 객체를 직접 생성하는 게 아니라 외부에서 생성한 후 주입 시켜주는 방식
    private final StoreProvider storeProvider;
    @Autowired
    private final StoreService storeService;
    @Autowired
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!


    public StoreController(StoreProvider storeProvider, StoreService storeService, JwtService jwtService) {
        this.storeProvider = storeProvider;
        this.storeService = storeService;
        this.jwtService = jwtService;
    }

    // ******************************************************************************

    /**
     * 모든 회원들의  조회 API
     * [GET] /app/stores/categories
     */
    //Query String
    @ResponseBody   // return되는 자바 객체를 JSON으로 바꿔서 HTTP body에 담는 어노테이션.
    //  JSON은 HTTP 통신 시, 데이터를 주고받을 때 많이 쓰이는 데이터 포맷.
    @GetMapping("/categories") // (GET) 127.0.0.1:9000/app/users
    // GET 방식의 요청을 매핑하기 위한 어노테이션
    public BaseResponse<List<GetStoreCatRes>> getStoreCat() {

        try {
            // 식당 카테고리 리스트 불러오기
            System.out.println("enter");
            List<GetStoreCatRes> getStoreCat = storeProvider.getStoreCat();
            return new BaseResponse<>(getStoreCat);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 골라먹는 맛집 조회 API
     * [GET] /app/stores/gola
     */
    //Query String
    @ResponseBody   // return되는 자바 객체를 JSON으로 바꿔서 HTTP body에 담는 어노테이션.
    //  JSON은 HTTP 통신 시, 데이터를 주고받을 때 많이 쓰이는 데이터 포맷.
    @GetMapping("/gola") // (GET) 127.0.0.1:9000/app/users
    // GET 방식의 요청을 매핑하기 위한 어노테이션
    public BaseResponse<List<GetGolaRes>> getStoreGola() {

        try {
            // 식당 카테고리 리스트 불러오기
            List<GetGolaRes> getStoreGola = storeProvider.getStoreGola();
            return new BaseResponse<>(getStoreGola);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 추천 맛집 조회 API
     * [GET] /app/stores/reco?type=
     */
    //Query String
    @ResponseBody   // return되는 자바 객체를 JSON으로 바꿔서 HTTP body에 담는 어노테이션.
    //  JSON은 HTTP 통신 시, 데이터를 주고받을 때 많이 쓰이는 데이터 포맷.
    @GetMapping("/reco") // (GET) 127.0.0.1:9000/app/users
    // GET 방식의 요청을 매핑하기 위한 어노테이션
    public BaseResponse<List<GetRecRes>> getStoreRec(@RequestParam String type) {

        try {
            if (type.equals("popular") || type.equals("only") || type.equals("new")) {
                System.out.println(type);
                List<GetRecRes> getStoreRec = storeProvider.getStoreRec();
                return new BaseResponse<>(getStoreRec);
            }
            else {
                return new BaseResponse<>(GET_STORES_REC_TYPE);
            }

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 카테고리별 가게 조회 API
     * [GET] /app/stores/cate?type=
     */
    //Query String
    @ResponseBody   // return되는 자바 객체를 JSON으로 바꿔서 HTTP body에 담는 어노테이션.
    //  JSON은 HTTP 통신 시, 데이터를 주고받을 때 많이 쓰이는 데이터 포맷.
    @GetMapping("/cate") // (GET) 127.0.0.1:9000/app/users
    // GET 방식의 요청을 매핑하기 위한 어노테이션
    public BaseResponse<List<GetCatRes>> getCatStore(@RequestParam String type) {

        try {
            if (type.equals("한식")) {
                System.out.println(type);
                List<GetCatRes> getCatStoreRec = storeProvider.getCatStoreRec(type);
                return new BaseResponse<>(getCatStoreRec);
            }
            else {
                return new BaseResponse<>(GET_STORES_REC_TYPE);
            }

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 가게 메뉴& 정보 조회 API
     * [GET] /app/stores/:storeIdx/store
     */
    //Query String
    @ResponseBody   // return되는 자바 객체를 JSON으로 바꿔서 HTTP body에 담는 어노테이션.
    //  JSON은 HTTP 통신 시, 데이터를 주고받을 때 많이 쓰이는 데이터 포맷.
    @GetMapping("/{storeIdx}/store") // (GET) 127.0.0.1:9000/app/users
    // GET 방식의 요청을 매핑하기 위한 어노테이션
    public BaseResponse<GetStoreMenus> getStoreMenus(@PathVariable("storeIdx") int storeIdx) {

        try {
            System.out.println(storeIdx);
            if (storeIdx < 1) return new BaseResponse<>(GET_STORES_STORESIDX_EMPTY);

            // 존재하는 가게인지 확인
            int storeExist = storeProvider.checkStoreExist(storeIdx);
            if (storeExist == 0) return new BaseResponse<>(GET_STORES_EMPTY);

            // 존재하는 사용자인지 확인
            int userIdx = jwtService.getUserIdx();
            int userExist = storeProvider.checkUserExist(userIdx);
            if (userExist == 0) return new BaseResponse<>(GET_USER_EMPTY);
            System.out.println(userIdx);

            GetStoreMenus getStoreMenus = storeProvider.getStoreMenus(userIdx, storeIdx);
            return new BaseResponse<>(getStoreMenus);


        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
