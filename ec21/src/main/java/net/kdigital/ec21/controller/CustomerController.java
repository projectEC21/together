package net.kdigital.ec21.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import net.kdigital.ec21.service.CustomerService;

@Controller
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    
    /**
     * login 페이지 요청
     * 
     * @return
     */
    @GetMapping("/main/login")
    public String login() {
        return "/main/login";
    }

    /**
     * register 페이지 요청
     * 
     * @return
     */
    @GetMapping("/main/register")
    public String register() {
        return "/main/register";
    }

    /**
     * myinfo(mypage) 페이지 요청
     * 
     * @return
     */
    @GetMapping("/main/myinfo")
    public String myinfo() {
        return "/main/myinfo";
    }

    /**
     * main페이지에서 내 인콰이어리 inbox 요청
     * 
     * @return
     */
    @GetMapping("/main/inbox")
    public String inbox() {
        return "/main/inbox";
    }
}
