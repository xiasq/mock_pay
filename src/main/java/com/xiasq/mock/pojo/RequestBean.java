package com.xiasq.mock.pojo;

import lombok.Data;

/**
 * @author xiasq
 * @version RequestBean, v0.1 2018/8/27 14:39
 */
@Data
public class RequestBean {
    private String uri;
    private String method;
    private String forms;
    private Boolean check = true;
}
