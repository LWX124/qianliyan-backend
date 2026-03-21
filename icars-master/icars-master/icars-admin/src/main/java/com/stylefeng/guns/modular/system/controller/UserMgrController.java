package com.stylefeng.guns.modular.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.config.properties.GunsProperties;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.base.tips.Tip;
import com.stylefeng.guns.core.common.HuanxinConst;
import com.stylefeng.guns.core.common.annotion.BussinessLog;
import com.stylefeng.guns.core.common.annotion.Permission;
import com.stylefeng.guns.core.common.constant.Const;
import com.stylefeng.guns.core.common.constant.dictmap.UserDict;
import com.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.common.constant.state.ManagerStatus;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.datascope.DataScope;
import com.stylefeng.guns.core.db.Db;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.core.node.ZTreeNode;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.shiro.ShiroUser;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.huanxin.HuanXinUtil;
import com.stylefeng.guns.modular.system.dao.UserMapper;
import com.stylefeng.guns.modular.system.factory.UserFactory;
import com.stylefeng.guns.modular.system.model.Dept;
import com.stylefeng.guns.modular.system.model.User;
import com.stylefeng.guns.modular.system.service.IDeptService;
import com.stylefeng.guns.modular.system.service.IUserService;
import com.stylefeng.guns.modular.system.service.IWxService;
import com.stylefeng.guns.modular.system.transfer.UserDto;
import com.stylefeng.guns.modular.system.warpper.UserWarpper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 系统管理员控制器
 *
 * @author kosan
 * @Date 2017年1月11日 下午1:08:17
 */
@Controller
@RequestMapping("/mgr")
public class UserMgrController extends BaseController {

    private static String PREFIX = "/system/user/";

    @Resource
    private GunsProperties gunsProperties;

    @Resource
    private IUserService userService;

    @Resource(name="wxServiceImpl")
    private IWxService wxService;

    @Resource
    private IDeptService deptService;

    @Resource
    private HuanXinUtil huanXinUtil;

    @Value("${spring.system.headimgHost}")
    private String headimgHost;

    @Value("${spring.system.file-upload-path}")
    private String fileUploadPath;


    /**
     * 跳转到查看管理员列表的页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "user.html";
    }

    /**
     * 跳转到新增管理员经纬度页面
     */
    @RequestMapping("/user_add_location")
    public String addLocationView() {
        return PREFIX + "user_add_location.html";
    }

    /**
     * 跳转到查看管理员列表的页面
     */
    @RequestMapping("/user_add")
    public String addView() {
        return PREFIX + "user_add.html";
    }

    /**
     * 跳转到获取推广二维码页面
     */
    @RequestMapping("/toQrPage/{account}")
    public String toQrPage(@PathVariable String account, Model model) {
        if (ToolUtil.isEmpty(account)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        model.addAttribute("qrCodeUrl", getHttpServletRequest().getRequestURI().toString().replace("toQrPage", "user_qrCode"));
        return PREFIX + "qrcode_view.html";
    }

    /**
     * 获取推广二维码图片流
     */
    @RequestMapping("/user_qrCode/{account}")
    public void getQrCodeImg(@PathVariable String account, HttpServletResponse response, Model model) {
        if (ToolUtil.isEmpty(account)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        byte[] data = wxService.getMiniqrQr(account);
        response.setContentType("image/png");
        try {
            OutputStream stream = response.getOutputStream();
            stream.write(data);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 跳转到角色分配页面
     */
    //@RequiresPermissions("/mgr/role_assign")  //利用shiro自带的权限检查
    @Permission
    @RequestMapping("/role_assign/{userId}")
    public String roleAssign(@PathVariable Integer userId, Model model) {
        if (ToolUtil.isEmpty(userId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        User user = (User) Db.create(UserMapper.class).selectOneByCon("id", userId);
        model.addAttribute("userId", userId);
        model.addAttribute("userAccount", user.getAccount());
        return PREFIX + "user_roleassign.html";
    }

    /**
     * 跳转到编辑管理员页面
     */
    @Permission
    @RequestMapping("/user_edit/{userId}")
    public String userEdit(@PathVariable Integer userId, Model model) {
        if (ToolUtil.isEmpty(userId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        assertAuth(userId);
        User user = this.userService.selectById(userId);
        model.addAttribute(user);
        model.addAttribute("roleName", ConstantFactory.me().getRoleName(user.getRoleid()));
        model.addAttribute("deptName", ConstantFactory.me().getDeptName(user.getDeptid()));
        LogObjectHolder.me().set(user);
        return PREFIX + "user_edit.html";
}

    /**
     * 跳转到查看用户详情页面
     */
    @RequestMapping("/user_info")
    public String userInfo(Model model) {
        Integer userId = ShiroKit.getUser().getId();
        if (ToolUtil.isEmpty(userId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        User user = this.userService.selectById(userId);
        model.addAttribute(user);
        model.addAttribute("roleName", ConstantFactory.me().getRoleName(user.getRoleid()));
        model.addAttribute("deptName", ConstantFactory.me().getDeptName(user.getDeptid()));
        LogObjectHolder.me().set(user);
        return PREFIX + "user_view.html";
    }

    /**
     * 跳转到修改密码界面
     */
    @RequestMapping("/user_chpwd")
    public String chPwd() {
        return PREFIX + "user_chpwd.html";
    }

    /**
     * 修改当前用户的密码
     */
    @RequestMapping("/changePwd")
    @ResponseBody
    public Object changePwd(@RequestParam String oldPwd, @RequestParam String newPwd, @RequestParam String rePwd) {
        if (!newPwd.equals(rePwd)) {
            throw new GunsException(BizExceptionEnum.TWO_PWD_NOT_MATCH);
        }
        Integer userId = ShiroKit.getUser().getId();
        User user = userService.selectById(userId);
        String oldMd5 = ShiroKit.md5(oldPwd, user.getSalt());
        if (user.getPassword().equals(oldMd5)) {
            String newMd5 = ShiroKit.md5(newPwd, user.getSalt());
            user.setPassword(newMd5);
            user.updateById();
            return SUCCESS_TIP;
        } else {
            throw new GunsException(BizExceptionEnum.OLD_PWD_NOT_RIGHT);
        }
    }

    /**
     * 查询管理员列表
     */
    @RequestMapping("/list")
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Object list(@RequestParam(required = false) String name, @RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime, @RequestParam(required = false) Integer deptid) {
        Page<User> page = new PageFactory<User>().defaultPage();
        if (ShiroKit.isAdmin()) {
            List<Map<String, Object>> users = userService.selectUsers(page, null, name, beginTime, endTime, deptid);
            page.setRecords((List<User>) new UserWarpper(users).warp());
            return super.packForBT(page);
        } else {
            DataScope dataScope = new DataScope(ShiroKit.getDeptDataScope());
            List<Map<String, Object>> users = userService.selectUsers(page, dataScope, name, beginTime, endTime, deptid);
            return new UserWarpper(users).warp();
        }
    }

    /**
     * 添加管理员
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加管理员", key = "account", dict = UserDict.class)
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Tip add(@Valid UserDto user, BindingResult result) {
        if (result.hasErrors()) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        // 判断账号是否重复
        User theUser = userService.getByAccount(user.getAccount());
        if (theUser != null) {
            throw new GunsException(BizExceptionEnum.USER_ALREADY_REG);
        }
        // 判断账号是否重复
        List<User> var1 = userService.getByPhone(user.getPhone());
        if (var1 != null && var1.size() > 0) {
            throw new GunsException(BizExceptionEnum.USER_PHONE_CONFLICT);
        }
        // 完善账号信息
        user.setSalt(ShiroKit.getRandomSalt(5));
        user.setPassword(ShiroKit.md5(user.getPassword(), user.getSalt()));
        user.setStatus(ManagerStatus.FREEZED.getCode());
        user.setCreatetime(new Date());

        this.userService.insert(UserFactory.createUser(user));
        return SUCCESS_TIP;
    }

    /**
     * 修改管理员
     *
     * @throws NoPermissionException
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "修改管理员", key = "account", dict = UserDict.class)
    @ResponseBody
    public Tip edit(@Valid UserDto user, BindingResult result) throws NoPermissionException {
        if (result.hasErrors()) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }

        User oldUser = userService.selectById(user.getId());

        if (ShiroKit.hasRole(Const.ADMIN_NAME) || ShiroKit.hasRole(Const.SUB_ADMIN_NAME)) {
            this.userService.updateById(UserFactory.editUser(user, oldUser));
            return SUCCESS_TIP;
        } else {
            assertAuth(user.getId());
            ShiroUser shiroUser = ShiroKit.getUser();
            if (shiroUser.getId().equals(user.getId())) {
                this.userService.updateById(UserFactory.editUser(user, oldUser));
                return SUCCESS_TIP;
            } else {
                throw new GunsException(BizExceptionEnum.NO_PERMITION);
            }
        }
    }

    /**
     * 删除管理员（逻辑删除）
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除管理员", key = "userId", dict = UserDict.class)
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Tip delete(@RequestParam Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        //不能删除超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new GunsException(BizExceptionEnum.CANT_DELETE_ADMIN);
        }
        assertAuth(userId);
        this.userService.setStatus(userId, ManagerStatus.DELETED.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 查看管理员详情
     */
    @RequestMapping("/view/{userId}")
    @ResponseBody
    public User view(@PathVariable Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        assertAuth(userId);
        return this.userService.selectById(userId);
    }

    /**
     * 重置管理员的密码
     */
    @RequestMapping("/reset")
    @BussinessLog(value = "重置管理员密码", key = "userId", dict = UserDict.class)
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Tip reset(@RequestParam Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        assertAuth(userId);
        User user = this.userService.selectById(userId);
        user.setSalt(ShiroKit.getRandomSalt(5));
        user.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, user.getSalt()));
        this.userService.updateById(user);
        return SUCCESS_TIP;
    }

    /**
     * 冻结用户
     */
    @RequestMapping("/freeze")
    @BussinessLog(value = "冻结用户", key = "userId", dict = UserDict.class)
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Tip freeze(@RequestParam Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        //不能冻结超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new GunsException(BizExceptionEnum.CANT_FREEZE_ADMIN);
        }
        assertAuth(userId);
        this.userService.setStatus(userId, ManagerStatus.FREEZED.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 解除冻结用户
     */
    @RequestMapping("/unfreeze")
    @BussinessLog(value = "解除冻结用户", key = "userId", dict = UserDict.class)
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Tip unfreeze(@RequestParam Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        assertAuth(userId);
        this.userService.setStatus(userId, ManagerStatus.OK.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 审核通过用户注册
     */
    @RequestMapping("/checkOk")
    @BussinessLog(value = "审核通过用户注册", key = "userId", dict = UserDict.class)
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Tip checkOk(@RequestParam Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        assertAuth(userId);
        this.userService.setStatus(userId, ManagerStatus.REGISTED.getCode());

        //环形初始化好友信息 todo

        return SUCCESS_TIP;
    }

    /**
     * 分配角色
     */
    @RequestMapping("/setRole")
    @BussinessLog(value = "分配角色", key = "userId,roleIds", dict = UserDict.class)
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Tip setRole(@RequestParam("userId") Integer userId, @RequestParam("roleIds") String roleIds) {
        if (ToolUtil.isOneEmpty(userId, roleIds)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        //不能修改超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new GunsException(BizExceptionEnum.CANT_CHANGE_ADMIN);
        }
        assertAuth(userId);
        this.userService.setRoles(userId, roleIds);
        return SUCCESS_TIP;
    }

    /**
     * 初始化环信好友
     */
    @RequestMapping("/initHuanxinFriend")
    @Permission({Const.ADMIN_NAME, Const.SUB_ADMIN_NAME})
    @ResponseBody
    public Tip initHuanxinFriend(@RequestParam("userId") Integer userId) {
        if (userId == null || userId == 0) {
            throw new GunsException(BizExceptionEnum.PARAM_ERR);
        }
        assertAuth(userId);
        User user = this.userService.selectById(userId);
        if (user == null || StringUtils.isEmpty(user.getAccount())) {
            throw new GunsException(BizExceptionEnum.NO_THIS_USER);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(user.getName());
        Dept dept = deptService.selectById(user.getDeptid());
        if (dept != null && !StringUtils.isEmpty(dept.getSimplename())) {
            sb.append("-");
            sb.append(dept.getSimplename());
        }

        //注册
        huanXinUtil.singleRegister(user.getAccount(), HuanxinConst.defaultPass, String.valueOf(sb), false);
        FutureTask<Integer> task = new FutureTask<Integer>(
                new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        huanXinUtil.initNewUser(user.getAccount(), String.valueOf(sb));
                        return null;
                    }
                }
        );
        new Thread(task, "开启线程注册环信新用户").start();
        return SUCCESS_TIP;
    }


    /**
     * 跳转到新增管理员经纬度页面
     */
    @RequestMapping("/toHuanxinFriendPage/{userId}")
    public String toHuanxinFriendPage(Model model, @PathVariable Integer userId) {
        model.addAttribute("userId", userId);
        return PREFIX + "huanxin_friend_list.html";
    }


    /**
     * 查找某个用户的好友
     */
    @RequestMapping("/huanxinFriendList/{userId}")
    @ResponseBody
    public List<ZTreeNode> countHuanxinFriendNum(@PathVariable Integer userId) {
        if (userId == null || userId == 0) {
            throw new GunsException(BizExceptionEnum.PARAM_ERR);
        }
        User user = this.userService.selectById(userId);
        if (user == null || StringUtils.isEmpty(user.getAccount())) {
            throw new GunsException(BizExceptionEnum.NO_THIS_USER);
        }
        assertAuth(userId);
        List<ZTreeNode> list = new ArrayList<>();

        JSONObject friendInfo = huanXinUtil.findFriendInfo(user.getAccount(), false);
        if (friendInfo == null) {
            return list;
        }
        JSONArray entities = friendInfo.getJSONArray("data");

        for (int i = 0; i < entities.size(); i++) {
            String value = entities.getString(i);
            ZTreeNode zTreeNode = new ZTreeNode();
            zTreeNode.setId((long) i);
            zTreeNode.setName(value);
            list.add(zTreeNode);
        }
        return list;
    }


    /**
     * 上传图片
     */
    @RequestMapping(method = RequestMethod.POST, path = "/upload")
    @ResponseBody
    public String upload(@RequestPart("file") MultipartFile picture) {

        String pictureName = UUID.randomUUID().toString() + "." + ToolUtil.getFileSuffix(picture.getOriginalFilename());
        try {
//            String fileSavePath = gunsProperties.getFileUploadPath();
            String fileSavePath = fileUploadPath;
            picture.transferTo(new File(fileSavePath + pictureName));
        } catch (Exception e) {
            throw new GunsException(BizExceptionEnum.UPLOAD_ERROR);
        }
        return (headimgHost == null ? "" : headimgHost) + pictureName;
    }

    /**
     * 判断当前登录的用户是否有操作这个用户的权限
     */
    private void assertAuth(Integer userId) {
        if (ShiroKit.isAdmin()) {
            return;
        }
        List<Integer> deptDataScope = ShiroKit.getDeptDataScope();
        User user = this.userService.selectById(userId);
        Integer deptid = user.getDeptid();
        if (deptDataScope.contains(deptid)) {
            return;
        } else {
            throw new GunsException(BizExceptionEnum.NO_PERMITION);
        }

    }
}
