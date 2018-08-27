package com.xiasq.mock.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiasq.mock.config.Config;
import com.xiasq.mock.pojo.DataBean;
import com.xiasq.mock.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author xiasq
 * @version CoreController, v0.1 2018/8/27 14:42
 */
@Controller
@Slf4j
public class CoreController {


    @RequestMapping("/list2")
    public String userList2(Model model) throws Exception {
        model.addAttribute("hello","Hello, Spring Boot!");
        model.addAttribute("userList", null);
        return "/list2";
    }


    @RequestMapping("/tt")
    @ResponseBody
    public Map test(){
        Map map = new HashMap();
        map.put("status",200);
        map.put("msg","success");
        return map;
    }

    @RequestMapping("/test1")
    @ResponseBody
    public Object post(HttpServletRequest request){
        System.out.println("*********************");
        String uri = request.getRequestURI();
        String method = request.getMethod();
        System.out.println("uri:"+uri+",method:"+method);
        DataBean bean = Config.config.get(uri+"-"+method);
        if (bean == null){
            return Result.result(400,"没有对应的方法");
        }
        Result result = this.check(bean,request);
        if (result.getStatus() != HttpStatus.OK.value()){
            return result;
        }
        return result.getData().toString();
    }


    private Result check(DataBean dataBean, HttpServletRequest request){
        System.out.println("************************requestMap:");
        Map requestMap = request.getParameterMap();
        if (CollectionUtils.isEmpty(requestMap)){
            return Result.result(300,"请求参数为空");
        }
        String forms = dataBean.getRequest().getForms();
        Boolean check = dataBean.getRequest().getCheck();
        log.info("校验value标志:{}",check);
        JSONObject configJson = JSON.parseObject(forms);
        Set<String> configKeys = configJson.keySet();
        Set<String> requestKeys = requestMap.keySet();
        for (String key:configKeys){
            if (!requestKeys.contains(key)){
                log.error("没有请求参数[{}]",key);
                return Result.result(300,"没有请求参数["+key+"]");
            }
            if (check){
                log.info("请求参数[{}]值:{},配置预期值:{}",key,request.getParameter(key),configJson.getString(key));
                if (!"@@".equals(request.getParameter(key)) && !configJson.getString(key).equals(request.getParameter(key))){
                    log.error("请求参数[{}]与预期值不匹配",key);
                    return Result.result(300,"请求参数["+key+"]与预期值不匹配");
                }
            }
        }
        String responseStr = dataBean.getResponse();
        return Result.success("成功",responseStr);
    }
}

