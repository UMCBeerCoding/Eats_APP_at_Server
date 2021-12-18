package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.like.model.PostLikeReq;
import com.example.demo.src.like.model.PostLikeRes;
import com.example.demo.utils.JwtService;
import io.jsonwebtoken.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/app/likes")
public class LikeContoller {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private final LikeProvider likeProvider;
    @Autowired
    private final LikeService likeService;
    @Autowired
    private final JwtService jwtService;

    public LikeContoller(LikeProvider likeProvider, LikeService likeService, JwtService jwtService){
            this.likeProvider = likeProvider;
            this.likeService = likeService;
            this.jwtService = jwtService;
    }


    @ResponseBody
    @PostMapping("/store")
    public BaseResponse<PostLikeRes> createLike(@RequestBody PostLikeReq postLikeReq){
        if (postLikeReq.getStoreIdx() == null){
            return new BaseResponse<>(POST_LIKE_EMPTY_STORE);
        }
        if (postLikeReq.getUserIdx() == null){
            return new BaseResponse<>(POST_LIKE_EMPTY_USER);
        }

        try{
            System.out.println("컨트롤ㄹ러");
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdxByJwt != postLikeReq.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            System.out.println("컨트롤ㄹ러2");
            PostLikeRes postLikeRes = likeService.createLike(postLikeReq);
            return new BaseResponse<>(postLikeRes);
        }
        catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }


}
