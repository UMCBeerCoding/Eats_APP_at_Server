package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.src.like.model.CheckLike;
import com.example.demo.src.like.model.PostLikeReq;
import com.example.demo.src.like.model.PostLikeRes;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.hibernate.annotations.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class LikeService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final LikeDao likeDao;
    private final LikeProvider likeProvider;
    private final JwtService jwtService;
    private final UserProvider userProvider;

    public LikeService(LikeProvider likeProvider, LikeDao likeDao, JwtService jwtService, UserProvider userProvider){
        this.likeProvider = likeProvider;
        this.likeDao = likeDao;
        this.jwtService = jwtService;
        this.userProvider = userProvider;
    }

    @Transactional
    public PostLikeRes createLike(PostLikeReq postLikeReq) throws BaseException {
        System.out.println("서비스0");
        if(userProvider.checkUserIdx(postLikeReq.getUserIdx()) == 0){
            throw new BaseException(POST_LIKE_INVALID_USER);
        }

        /*추후 store validation 추가
        if(storeProvider.checkUserIdx(postLikeReq.getUserIdx()) == 0){
            throw new BaseException(POST_LIKE_INVALID_USER);
        }
        */

        try {
            System.out.println("서비스1");
            CheckLike checkLike = likeProvider.checkLikeIdx(postLikeReq);
            System.out.println("서비스2");
            //좋아요 테이블에 존재하지 않는 관계
            if(checkLike == null){
                System.out.println("1");
                checkLike = likeDao.createLike(postLikeReq);
                PostLikeRes postLikeRes = new PostLikeRes(checkLike.getFavIdx(), checkLike.getStatus());
                return postLikeRes;
            }
            //좋아요 테이블에 존재하지만 F한 관계
            else if(checkLike.getStatus().equals("F")){
                System.out.println("2");
                checkLike = likeDao.createActiveLike(checkLike.getFavIdx());
                PostLikeRes postLikeRes = new PostLikeRes(checkLike.getFavIdx(), checkLike.getStatus());
                return postLikeRes;
            }
            //좋아요 테이블에 존재하지만 T한 관계
            else if(checkLike.getStatus().equals("T")){
                System.out.println("3");
                checkLike = likeDao.createInactiveLike(checkLike.getFavIdx());
                PostLikeRes postLikeRes = new PostLikeRes(checkLike.getFavIdx(), checkLike.getStatus());
                return postLikeRes;
            }
            else{
                System.out.println("4");
                throw new BaseException(DATABASE_ERROR);
            }
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
            System.out.println(exception.getStackTrace());
            System.out.println("Serveice");
            throw new BaseException(DATABASE_ERROR);
        }


    }
}
