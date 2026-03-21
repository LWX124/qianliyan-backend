package com.stylefeng.guns.modular.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.stylefeng.guns.core.datascope.DataScope;
import com.stylefeng.guns.modular.system.dao.UserMapper;
import com.stylefeng.guns.modular.system.model.User;
import com.stylefeng.guns.modular.system.service.IUserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author kosans
 * @since 2018-02-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    @Value("${spring.system.file-upload-path}")
    private String claimImgLocalPath;

    @Value("${spring.system.headimgHost}")
    private String netHost;


    @Override
    public int setStatus(Integer userId, int status) {
        return this.baseMapper.setStatus(userId, status);
    }

    @Override
    public int changePwd(Integer userId, String pwd) {
        return this.baseMapper.changePwd(userId, pwd);
    }

    @Override
    public List<Map<String, Object>> selectUsers(Page<User> page, DataScope dataScope, String name, String beginTime, String endTime, Integer deptid) {
        return this.baseMapper.selectUsers(page, dataScope, name, beginTime, endTime, deptid);
    }

    @Override
    public List<Map<String, Object>> selectUsersForPush(User user) {
        return this.baseMapper.selectUsersForPush(user);
    }

    @Override
    public List<Map<String, Object>> selectUsersForMaintainPush(User user) {
        return this.baseMapper.selectUsersForMaintainPush(user);
    }

    @Override
    public int setRoles(Integer userId, String roleIds) {
        return this.baseMapper.setRoles(userId, roleIds);
    }

    @Override
    public User getByAccount(String account) {
        return this.baseMapper.getByAccount(account);
    }

    @Override
    public  List<User> getByPhone(String phone) {
        List<User> users = this.baseMapper.selectList(
                new EntityWrapper<User>().eq("phone", phone)
        );
        return users;
    }

    @Override
    public List<String> uploadFileForUser(List<MultipartFile> files) throws IOException {
        List<String> urls = new ArrayList<>();
        for(MultipartFile file : files){
            String random = RandomStringUtils.randomAlphabetic(32);
            String fileName;
            fileName = random.concat(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length()));
            String imgUrl = null;
            File newfile = new File(claimImgLocalPath, fileName);
            if(!newfile.getParentFile().exists()){
                newfile.getParentFile().mkdirs();
            }
            FileCopyUtils.copy(file.getBytes(), newfile);
            imgUrl = netHost.concat(fileName);
            urls.add(imgUrl);
        }
        return urls;
    }


}
