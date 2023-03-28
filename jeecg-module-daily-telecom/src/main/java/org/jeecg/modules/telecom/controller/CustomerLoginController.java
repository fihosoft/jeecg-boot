package org.jeecg.modules.telecom.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.telecom.model.CustomerLoginModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/telecom")
@Api(tags="用户登录")
@Slf4j
public class CustomerLoginController {
    @ApiOperation("登录接口")
    @PostMapping(value = "/login")
    public Result<JSONObject> login(@RequestBody CustomerLoginModel sysLoginModel){
        Result<JSONObject> result = new Result<JSONObject>();
        result.success("登录成功");
//        String username = sysLoginModel.getUsername();
//        String password = sysLoginModel.getPassword();
//        //update-begin-author:taoyan date:2022-11-7 for: issues/4109 平台用户登录失败锁定用户
//        if(isLoginFailOvertimes(username)){
//            return result.error500("该用户登录失败次数过多，请于10分钟后再次登录！");
//        }
//        //update-end-author:taoyan date:2022-11-7 for: issues/4109 平台用户登录失败锁定用户
//        //update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题
//        //前端密码加密，后端进行密码解密
//        //password = AesEncryptUtil.desEncrypt(sysLoginModel.getPassword().replaceAll("%2B", "\\+")).trim();//密码解密
//        //update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题
//
//        //update-begin-author:taoyan date:20190828 for:校验验证码
//        String captcha = sysLoginModel.getCaptcha();
//        if(captcha==null){
//            result.error500("验证码无效");
//            return result;
//        }
//        String lowerCaseCaptcha = captcha.toLowerCase();
//        //update-begin-author:taoyan date:2022-9-13 for: VUEN-2245 【漏洞】发现新漏洞待处理20220906
//        // 加入密钥作为混淆，避免简单的拼接，被外部利用，用户自定义该密钥即可
//        String origin = lowerCaseCaptcha+sysLoginModel.getCheckKey()+jeecgBaseConfig.getSignatureSecret();
//        String realKey = Md5Util.md5Encode(origin, "utf-8");
//        //update-end-author:taoyan date:2022-9-13 for: VUEN-2245 【漏洞】发现新漏洞待处理20220906
//        Object checkCode = redisUtil.get(realKey);
//        //当进入登录页时，有一定几率出现验证码错误 #1714
//        if(checkCode==null || !checkCode.toString().equals(lowerCaseCaptcha)) {
//            log.warn("验证码错误，key= {} , Ui checkCode= {}, Redis checkCode = {}", sysLoginModel.getCheckKey(), lowerCaseCaptcha, checkCode);
//            result.error500("验证码错误");
//            // 改成特殊的code 便于前端判断
//            result.setCode(HttpStatus.PRECONDITION_FAILED.value());
//            return result;
//        }
//        //update-end-author:taoyan date:20190828 for:校验验证码
//
//        //1. 校验用户是否有效
//        //update-begin-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false
//        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(SysUser::getUsername,username);
//        SysUser sysUser = sysUserService.getOne(queryWrapper);
//        //update-end-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false
//        result = sysUserService.checkUserIsEffective(sysUser);
//        if(!result.isSuccess()) {
//            return result;
//        }
//
//        //2. 校验用户名或密码是否正确
//        String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
//        String syspassword = sysUser.getPassword();
//        if (!syspassword.equals(userpassword)) {
//            //update-begin-author:taoyan date:2022-11-7 for: issues/4109 平台用户登录失败锁定用户
//            addLoginFailOvertimes(username);
//            //update-end-author:taoyan date:2022-11-7 for: issues/4109 平台用户登录失败锁定用户
//            result.error500("用户名或密码错误");
//            return result;
//        }
//
//        //用户登录信息
//        userInfo(sysUser, result);
//        //update-begin--Author:liusq  Date:20210126  for：登录成功，删除redis中的验证码
//        redisUtil.del(realKey);
//        //update-begin--Author:liusq  Date:20210126  for：登录成功，删除redis中的验证码
//        redisUtil.del(CommonConstant.LOGIN_FAIL + username);
//        LoginUser loginUser = new LoginUser();
//        BeanUtils.copyProperties(sysUser, loginUser);
//        baseCommonService.addLog("用户名: " + username + ",登录成功！", CommonConstant.LOG_TYPE_1, null,loginUser);
//        //update-end--Author:wangshuai  Date:20200714  for：登录日志没有记录人员
        return result;
    }
}
