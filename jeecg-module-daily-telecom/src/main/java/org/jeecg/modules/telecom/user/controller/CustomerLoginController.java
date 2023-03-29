package org.jeecg.modules.telecom.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.telecom.user.model.CustomerLoginModel;
import org.jeecg.modules.telecom.user.entity.UserPhone;
import org.jeecg.modules.telecom.user.service.IUserPhoneService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/telecom.user")
@Api(tags = "用户登录")
@Slf4j
public class CustomerLoginController {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IUserPhoneService userPhoneService;
    @Resource
    private BaseCommonService baseCommonService;


    @ApiOperation("登录接口")
    @PostMapping(value = "/login")
    public Result<JSONObject> login(@RequestBody CustomerLoginModel loginModel) {
        Result<JSONObject> result = new Result<JSONObject>();
        result.success("登录成功");
        String account = loginModel.getAccount();
        String password = loginModel.getPassword();
        if (isLoginFailOvertimes(account)) {
            return result.error500("该用户登录失败次数过多，请于10分钟后再次登录！");
        }

        //1. 校验用户是否有效
        //update-begin-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false
        LambdaQueryWrapper<UserPhone> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPhone::getAccount, account);
        UserPhone userPhone = userPhoneService.getOne(queryWrapper);
        //update-end-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false
        result = userPhoneService.checkUserIsEffective(userPhone);
        if (!result.isSuccess()) {
            return result;
        }

        //2. 校验用户名或密码是否正确
//        String userpassword = PasswordUtil.encrypt(username, password, userPhone.getSalt());
//        String syspassword = userPhone.getPassword();
//        if (!syspassword.equals(userpassword)) {
//            //update-begin-author:taoyan date:2022-11-7 for: issues/4109 平台用户登录失败锁定用户
//            addLoginFailOvertimes(username);
//            //update-end-author:taoyan date:2022-11-7 for: issues/4109 平台用户登录失败锁定用户
//            result.error500("用户名或密码错误");
//            return result;
//        }

        //用户登录信息
        userInfo(userPhone, result);
        //update-begin--Author:liusq  Date:20210126  for：登录成功，删除redis中的验证码
//        redisUtil.del(realKey);
        //update-begin--Author:liusq  Date:20210126  for：登录成功，删除redis中的验证码
        redisUtil.del(CommonConstant.LOGIN_FAIL + account);
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(userPhone, loginUser);
        baseCommonService.addLog("用户名: " + account + ",登录成功！", CommonConstant.LOG_TYPE_1, null, loginUser);
        //update-end--Author:wangshuai  Date:20200714  for：登录日志没有记录人员
        return result;
    }

    private void addLoginFailOvertimes(String username) {
        String key = CommonConstant.LOGIN_FAIL + username;
        Object failTime = redisUtil.get(key);
        Integer val = 0;
        if (failTime != null) {
            val = Integer.parseInt(failTime.toString());
        }
        // 1小时
        redisUtil.set(key, ++val, 3600);
    }

    private boolean isLoginFailOvertimes(String username) {
        String key = CommonConstant.LOGIN_FAIL + username;
        Object failTime = redisUtil.get(key);
        if (failTime != null) {
            Integer val = Integer.parseInt(failTime.toString());
            if (val > 5) {
                return true;
            }
        }
        return false;
    }

    private Result<JSONObject> userInfo(UserPhone userPhone, Result<JSONObject> result) {
        String account = userPhone.getAccount();
        String syspassword = userPhone.getPassword();
        // 获取用户部门信息
        JSONObject obj = new JSONObject(new LinkedHashMap<>());

        //1.生成token
        String token = JwtUtil.sign(account, syspassword);
        // 设置token缓存有效时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);
        obj.put("token", token);

        //2.设置登录租户
//        Result<JSONObject> loginTenantError = sysUserService.setLoginTenant(userPhone, obj, account,result);
//        if (loginTenantError != null) {
//            return loginTenantError;
//        }

        //3.设置登录用户信息
        obj.put("userInfo", userPhone);

        //4.设置登录部门
//        List<SysDepart> departs = sysDepartService.queryUserDeparts(userPhone.getId());
//        obj.put("departs", departs);
//        if (departs == null || departs.size() == 0) {
//            obj.put("multi_depart", 0);
//        } else if (departs.size() == 1) {
//            sysUserService.updateUserDepart(account, departs.get(0).getOrgCode(),null);
//            obj.put("multi_depart", 1);
//        } else {
//            //查询当前是否有登录部门
//            // update-begin--Author:wangshuai Date:20200805 for：如果用戶为选择部门，数据库为存在上一次登录部门，则取一条存进去
//            SysUser sysUserById = sysUserService.getById(userPhone.getId());
//            if(oConvertUtils.isEmpty(sysUserById.getOrgCode())){
//                sysUserService.updateUserDepart(account, departs.get(0).getOrgCode(),null);
//            }
//            // update-end--Author:wangshuai Date:20200805 for：如果用戶为选择部门，数据库为存在上一次登录部门，则取一条存进去
//            obj.put("multi_depart", 2);
//        }
//        obj.put("sysAllDictItems", sysDictService.queryAllDictItems());
        result.setResult(obj);
        result.success("登录成功");
        return result;
    }


}
