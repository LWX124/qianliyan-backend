package com.stylefeng.guns.modular.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.HuanxinConst;
import com.stylefeng.guns.modular.system.model.User;
import com.stylefeng.guns.modular.system.service.IUserService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/api/test")
public class TestController extends BaseController {

    @Resource
    private IUserService iUserService;


    @RequestMapping("")
    public void test() {
        ResponseEntity<String> friendAllUserUrl = null;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer YWMtRds7koI6EemVC93XwWBnQgAAAAAAAAAAAAAAAAAAAAGcts9wRLAR6Y47V4Zg4BR6AgMAAAFrBLcmwgBPGgDRYlnCNJ34x1IaA9RPL8xweP8D0TTjW_-Zop8oa1zMTQ");
        requestHeaders.add("Accept", "application/json");
        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);

        try {
            ///{org_name}/{app_name}/users/{owner_username}/contacts/users/{friend_username}
            friendAllUserUrl = restTemplate.exchange(HuanxinConst.friendAllUserUrl + "?limit=1000", HttpMethod.GET, requestEntity, String.class);
//            friendAllUserUrl = restTemplate.exchange(HuanxinConst.friendAllUserUrl + "/{owner_username}/contacts/users/{friend_username}", HttpMethod.GET, requestEntity, String.class);
        } catch (HttpClientErrorException errorException) {
            //重新获取token;
            errorException.printStackTrace();
        }
        System.out.println();

        JSONArray entities = JSONObject.parseObject(friendAllUserUrl.getBody()).getJSONArray("entities");

        for (int i = 0; i < entities.size(); i++) {
            JSONObject obj = entities.getJSONObject(i);
            String account = obj.getString("username");
            List<User> byPhone = iUserService.getByPhone(account);
            if (byPhone.size() > 0) {
                User byAccount = byPhone.get(0);
                if (byAccount.getRoleid().equals("7") || byAccount.getRoleid().equals("8")) {
                    //理赔员
                    for (int j = 0; j < entities.size(); j++) {
                        JSONObject obj2 = entities.getJSONObject(j);
                        String account2 = obj2.getString("username");
                        List<User> byPhone2 = iUserService.getByPhone(account2);
                        if (byPhone2.size() > 0 && (byPhone2.get(0).getRoleid().equals("7") || byPhone2.get(0).getRoleid().equals("8"))) {
                            deleteFriend(account, byPhone2.get(0).getAccount());
                        }
                    }
                } else {
                    //4s店员工
                    for (int j = 0; j < entities.size(); j++) {
                        JSONObject obj2 = entities.getJSONObject(j);
                        String account2 = obj2.getString("username");
                        List<User> byPhone2 = iUserService.getByPhone(account2);
                        if (byPhone2.size() > 0 && byPhone2.get(0).getRoleid().equals("6")) {
                            deleteFriend(account, byPhone2.get(0).getAccount());
                        }
                    }
                }
            } else {

//                17380643096
                System.out.println("size<0   " + account);
                if(account.equals("17380643096")){
                    for (int j = 0; j < entities.size(); j++) {
                        JSONObject obj2 = entities.getJSONObject(j);
                        String account2 = obj2.getString("username");
                        List<User> byPhone2 = iUserService.getByPhone(account2);
                        if (byPhone2.size() > 0 && byPhone2.get(0).getRoleid().equals("6")) {
                            deleteFriend(account, byPhone2.get(0).getAccount());
                        }
                    }
                }
            }

//            System.out.println(obj);
        }

    }

    @RequestMapping("/delete")
    public void deleteFriend(String account, String targetAccount) {
        ResponseEntity<String> friendAllUserUrl = null;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer YWMtRds7koI6EemVC93XwWBnQgAAAAAAAAAAAAAAAAAAAAGcts9wRLAR6Y47V4Zg4BR6AgMAAAFrBLcmwgBPGgDRYlnCNJ34x1IaA9RPL8xweP8D0TTjW_-Zop8oa1zMTQ");
        requestHeaders.add("Accept", "application/json");
        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);
        try {
            ///{org_name}/{app_name}/users/{owner_username}/contacts/users/{friend_username}
            friendAllUserUrl = restTemplate.exchange(HuanxinConst.friendAllUserUrl + "/" + account + "/contacts/users/" + targetAccount, HttpMethod.DELETE, requestEntity, String.class);
            System.out.println(friendAllUserUrl.getBody());
        } catch (HttpClientErrorException errorException) {
            //重新获取token;
            errorException.printStackTrace();
        }
    }
}
