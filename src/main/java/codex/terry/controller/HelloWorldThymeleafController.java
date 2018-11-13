package codex.terry.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 编写人: yh.zeng
 * 编写时间: 2018-8-28
 * 文件描述: Thymeleaf模板引擎Demo
 */
@Controller  //@Controller,不能使用@RestController
@RequestMapping(value = "/hello")
public class HelloWorldThymeleafController
{

    @RequestMapping(value = "/thymeleaf")
    public ModelAndView hello(){

        ModelAndView modelAndView = new ModelAndView("helloWorld/helloThymeleaf.html");
        modelAndView.addObject("msg","Hello Thymeleaf!");

        return  modelAndView;
    }

}
