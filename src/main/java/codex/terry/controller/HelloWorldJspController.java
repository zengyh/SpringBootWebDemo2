package codex.terry.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 文件名称: HelloJspController.java
 * 编写人: yh.zeng
 * 编写时间: 2018/9/4 11:39
 * 文件描述: 访问Jsp页面demo
 */
@Controller //@Controller,不能使用@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/hello")
public class HelloWorldJspController
{
    @RequestMapping(value = "/jsp")
    public String hello(){
        return "helloWorld/helloworld.jsp";
    }

}
