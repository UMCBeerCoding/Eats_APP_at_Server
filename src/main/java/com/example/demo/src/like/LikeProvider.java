package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.src.like.model.CheckLike;
import com.example.demo.src.like.model.PostLikeReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class LikeProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final LikeDao likeDao;
    private final JwtService jwtService;

    @Autowired
    public LikeProvider(LikeDao likeDao, JwtService jwtService){
        this.likeDao = likeDao;
        this.jwtService = jwtService;
    }

    public CheckLike checkLikeIdx(PostLikeReq postLikeReq) throws BaseException{
        try{
            return likeDao.checkLikeIdx(postLikeReq);
        }
        catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
