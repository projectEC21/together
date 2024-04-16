package net.kdigital.ec21.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainPageControllerhj {
    // ==================register.html=====================
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
     * main페이지에서 전체상품 페이지 요청
     * 
     * @return
     */
    @GetMapping("/main/list")
    public String list() {
        return "/main/list";
    }

    /**
     * main페이지에서 내 상품 페이지 요청
     * 
     * @return
     */
    @GetMapping("/main/myproducts")
    public String myProducts() {
        return "/main/myproducts";
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

    /**
     * main페이지에서 상품등록하기 요청
     * 
     * @return
     */
    @GetMapping("/main/productsWrite")
    public String productsWrite() {
        return "/main/productsWrite";
    }

}
