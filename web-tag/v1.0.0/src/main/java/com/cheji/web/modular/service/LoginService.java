package com.cheji.web.modular.service;

import com.cheji.web.modular.mapper.AppUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class LoginService {

    @Resource
    private AppUserMapper appUserMapper;


    @Transactional
    public void insertUser(){

    }

}
