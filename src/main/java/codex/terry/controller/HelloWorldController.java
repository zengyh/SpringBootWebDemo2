package codex.terry.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 编写人: yh.zeng
 * 编写时间: 2018-8-28
 * 文件描述:
 */
@RestController
public class HelloWorldController {

    @RequestMapping(value = "/hello")
    public String hello(){
        return "Hello SpringBoot!";
    }

}
