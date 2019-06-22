package com.lzp.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

/**
 * @description: 微信接口controller
 * @author: KM
 * 2018年4月14日 下午5:27:20
 */
@Controller
@RequestMapping("/wechat")
public class WechatController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//@Autowired
	//private WechatService wechatService;
	
	@Autowired
	private WeChatAuthService weChatAuthService;
	
	
	
	@RequestMapping("")
	@ResponseBody
	public String weixin(String signature,String timestamp,String nonce,String echostr){
		logger.info("微信-----》signature:"+signature+";timestamp:"+timestamp+";nonce:"+nonce+";echostr:"+echostr);
		if (WechatSignUtil.checkSignature(signature, timestamp, nonce)) {
			logger.info("接入成功");
            return echostr;
        }
		logger.error("接入失败");
        return "";
	}
	
	// 调用微信业务类接收消息、处理消息跟推送消息
    @RequestMapping(value = "",method = RequestMethod.POST)
    @ResponseBody
    public  String post(HttpServletRequest req){
        String respMessage ="";
        return respMessage;
    }
    
    //公众号点击登录
    @RequestMapping(value = "/wxMobileLogin")
    public String wxMobileLogin() throws Exception {
        String uri = weChatAuthService.getAuthorizationUrl("mobile",null);
        logger.info(uri);
        return "redirect:"+uri;
    }
    
    //pc点击微信登录，生成登录二维码
    @RequestMapping(value = "/wxLoginPage",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> wxLoginPage(HttpServletRequest request) throws Exception {
    	String sessionId = request.getSession().getId();
    	logger.info("sessionId:"+sessionId);
        String uri = weChatAuthService.getAuthorizationUrl("pc",sessionId);
        logger.info(uri);
        Map<String,String> map = new HashMap<String,String>();
        map.put("sessionId", sessionId);
        map.put("uri", uri);
        return map;
    }
    
    //扫描二维码授权成功，取到code，回调方法
    @RequestMapping(value = "/pcAuth")
    @ResponseBody
    public String pcCallback(String code,String state,HttpServletRequest request,HttpServletResponse response) throws Exception {
        String result = weChatAuthService.getAccessToken(code);
        JSONObject jsonObject = JSONObject.parseObject(result);
        
        //String refresh_token = jsonObject.getString("refresh_token");
        String access_token = jsonObject.getString("access_token");
        String openId = jsonObject.getString("openId");
        
        
        logger.info("------------授权成功----------------");
        JSONObject infoJson = weChatAuthService.getUserInfo(access_token,openId);
        if(infoJson!=null){
        	String nickname = infoJson.getString("nickName");
            logger.info("-----nickname-----"+nickname);
            logger.info("-----sessionId-----"+state);
            infoJson.put("openId", openId);
            infoJson.put("infoJson", infoJson.toJSONString());
            //redisTemplate.setValueSerializer(new EntityRedisSerializer());
            //redisTemplate.opsForValue().set(state, infoJson, 10*60, TimeUnit.SECONDS);
            return "登录成功！";
        }
	    return "登录失败！";
    }
    
    //移动端登录操作
    @RequestMapping(value = "/mobileAuth")
    public String mobileCallback(String code,HttpServletRequest request,HttpServletResponse response) throws Exception {
        String result = weChatAuthService.getAccessToken(code);
        JSONObject jsonObject = JSONObject.parseObject(result);

        String access_token = jsonObject.getString("access_token");
        String openId = jsonObject.getString("openId");

        logger.info("------------授权成功----------------");
        JSONObject infoJson = weChatAuthService.getUserInfo(access_token,openId);
        String nickname = infoJson.getString("nickName");
        logger.info("-----nickname-----"+nickname);
        //根据openId判断用户是否已经登陆过
        /*User user = userService.selectUserByWechat(openId);
        if (user == null) {
        	User newuser = new User();
        	newuser.setWechat(openId);
        	newuser.setNickname(nickname);
        	String sex = (String)infoJson.get("sex");
        	newuser.setSex(sex);
        	newuser.setPassword(openId);
        	int i = userService.insertUser(newuser);
        	if(i<1){
        		logger.info("mobile:登录失败");
    	        return "error";
        	}
        }*/
        //登录操作
	    try {
	    	/*UsernamePasswordToken token = new UsernamePasswordToken(openId,openId);
	        SecurityUtils.getSubject().login(token);
	        logger.info("mobile:登录成功");
	        
	        //更新用户最后登录时间
	        Subject  currentUser = SecurityUtils.getSubject(); 
			User luser = (User) currentUser.getPrincipal();
	        User user1 = new User();
	        user1.setId(luser.getId());
	        user1.setLastLogDate(new Date());
	        userService.updateUserByIdSelective(user1);*/
	    } catch (Exception e) {
	        logger.error("message", "未知系统错误:" + e.getMessage());
	    }
	    return "redirect:/borrow/wechatBorrowPage";
    }
    
    //轮询查询key
    @RequestMapping(value="/polling",method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> polling(String sessionId,HttpServletRequest request,HttpServletResponse response){
    	Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		return resultMap;
    	
    	/*if(redisTemplate.hasKey(sessionId)){
    		redisTemplate.setValueSerializer(new EntityRedisSerializer());
    		JSONObject infoJson = (JSONObject)redisTemplate.opsForValue().get(sessionId);
    		redisTemplate.opsForValue().getOperations().delete(sessionId);
    		String openId = (String)infoJson.get("openId");
    		//根据openId判断我们网站是否存在该用户，数据库用户表会保存用户
            User user = userService.selectUserByWechat(openId);
            if (user == null) {
            	String nickname = (String)infoJson.get("nickName");
            	String sex = (String)infoJson.get("sex");
            	User newuser = new User();
            	newuser.setSex(sex);
            	newuser.setPassword(openId);
            	newuser.setWechat(openId);
            	newuser.setNickname(nickname);
            	int i = userService.insertUser(newuser);//新增用户
            	if(i<1){
            		resultMap.put("status", 500);
        	        resultMap.put("message", "登录失败:");
        	        return resultMap;
            	}
            }
            //登录操作
    	    try {
    	    	UsernamePasswordToken token = new UsernamePasswordToken(openId, openId);//这里是用shiro登录，反正该openId已经微信扫码验证
    	        SecurityUtils.getSubject().login(token);
    	        Subject  currentUser = SecurityUtils.getSubject(); 
    			User luser = (User) currentUser.getPrincipal();
    	        if(luser.getState()==0){
    	        	resultMap.put("status", 501);
    		        resultMap.put("message", "登录失败：用户未激活！");
    		        SecurityUtils.getSubject().logout();
    		        return resultMap;
    	        }
    	        if(luser.getState()==2){
    	        	resultMap.put("status", 502);
    		        resultMap.put("message", "登录失败：用户被禁用！");
    		        SecurityUtils.getSubject().logout();
    		        return resultMap;
    	        }
    	        
    	        resultMap.put("status", 200);
    	        resultMap.put("message", "登录成功");
    	        
    	        //更新用户最后登录时间
    	        User user1 = new User();
    	        user1.setId(luser.getId());
    	        user1.setLastLogDate(new Date());
    	        userService.updateUserByIdSelective(user1);
    	        
    	        //获取cookie保存的图书
    	        Set<Book> sb = new HashSet<Book>();
    			Cookie[] cookies = request.getCookies();
    			if (null != cookies && cookies.length > 0) {
    				for(int i=0;i<cookies.length;i++){
    		    		Cookie cookie = cookies[i];
    		    		if(("USER_BOOK_SET").equals(cookie.getName())){//该用户是否有保存书在cookie
    		    			String cv = cookie.getValue();
    		    			if(!"".equals(cv)&&cv!=null){
    							try {
    								sb = JSON.parseObject(URLDecoder.decode(cv, "UTF-8"), new TypeReference<Set<Book>>(){});
    							} catch (UnsupportedEncodingException e) {
    								e.printStackTrace();
    								logger.error("解码发生错误。。。");
    							}
    		    			}
    		    			break;
    		    		}
    		    	}
    			}
    			if(sb.size()>0){
    				//登录后插入到缓存后删除cookie
    				for(Book book:sb){
    					if(book.getBookid()!=null){
    						setOperations.add(user.getUsername() + "USER_BOOK_SET", book.getBookid());
    					}
    				}
    				Cookie cookie = new Cookie("USER_BOOK_SET",null);
    	            cookie.setPath("/");
    	            cookie.setMaxAge(0);
    	            response.addCookie(cookie);
    			}
    	    } catch (Exception e) {
    	        resultMap.put("message", "未知系统错误:" + e.getMessage());
    	    }
    	    return resultMap;
    	}else{//not has key
    		resultMap.put("status", 0);
    		return resultMap;
    	}*/
    }
}
