package com.lzp.controller;

import com.lzp.utils.HttpRequestUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by liuzp on 2018/5/9.
 */
@Controller
public class CoreController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${appid}")
    private String appid;

    @Value("${callBack}")
    private String callBack;

    @Value("${scope}")
    private String scope;

    @Value("${appsecret}")
    private String appsecret;




    @RequestMapping("/")
    public String index(Model model) throws UnsupportedEncodingException {
        String oauthUrl = "https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
        String redirect_uri = URLEncoder.encode(callBack, "utf-8"); ;
        oauthUrl =  oauthUrl.replace("APPID",appid).replace("REDIRECT_URI",redirect_uri).replace("SCOPE",scope);
        model.addAttribute("name","liuzp");
        model.addAttribute("oauthUrl",oauthUrl);
        return "index";
    }

    @RequestMapping("/1")
    public String index1(Model model) throws UnsupportedEncodingException {
        String redirect_uri = URLEncoder.encode(callBack, "utf-8"); ;
        model.addAttribute("name","liuzp");
        model.addAttribute("appid",appid);
        model.addAttribute("scope",scope);
        model.addAttribute("redirect_uri",redirect_uri);
        return "index1";
    }


    @RequestMapping("/callBack")
    public String callBack(String code,String state,Model model) throws Exception{
        logger.info("进入授权回调,code:{},state:{}",code,state);

        //1.通过code获取access_token
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        url = url.replace("APPID",appid).replace("SECRET",appsecret).replace("CODE",code);
        String tokenInfoStr =  HttpRequestUtils.httpGet(url,null,null);

        JSONObject tokenInfoObject = new JSONObject(tokenInfoStr);
        logger.info("tokenInfoObject:{}",tokenInfoObject);

        //2.通过access_token和openid获取用户信息
        String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
        userInfoUrl = userInfoUrl.replace("ACCESS_TOKEN",tokenInfoObject.getString("access_token")).replace("OPENID",tokenInfoObject.getString("openid"));
        String userInfoStr =  HttpRequestUtils.httpGet(userInfoUrl,null,null);
        logger.info("userInfoObject:{}",userInfoStr);

        model.addAttribute("tokenInfoObject",tokenInfoObject);
        model.addAttribute("userInfoObject",userInfoStr);

        return "result";
    }



}
