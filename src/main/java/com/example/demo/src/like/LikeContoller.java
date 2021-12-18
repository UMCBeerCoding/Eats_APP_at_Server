package com.example.demo.src.like;

import com.example.demo.utils.JwtService;
import io.jsonwebtoken.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


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



}
