package com.xiasq.mock.config;

import com.alibaba.fastjson.JSON;
import com.xiasq.mock.pojo.DataBean;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiasq
 * @version Config, v0.1 2018/8/27 14:38
 */
@Component
public class Config {
    @Autowired
    MyResourceLoader myResourceLoader;

    public static ConcurrentHashMap<String,DataBean> config = new ConcurrentHashMap();

    @PostConstruct
    public void init() throws IOException {
        Resource resource =  myResourceLoader.getResource("classpath:test.json");
        String content = IOUtils.toString(resource.getInputStream(),"utf-8");
        System.out.println("=============");
        System.out.println(content);
        List<DataBean> beanList = JSON.parseArray(content, DataBean.class);
        for (DataBean bean:beanList){
            config.putIfAbsent(bean.getRequest().getUri()+"-"+ bean.getRequest().getMethod().toUpperCase(),bean);
        }
    }
}
