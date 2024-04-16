package net.kdigital.ec21.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import net.kdigital.ec21.dto.CustomerDTO;
import net.kdigital.ec21.service.CustomerService;

@Controller
@RequiredArgsConstructor
public class MainPageControllerhj {
    private final CustomerService customerService;

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

    // /**
    //  * main페이지에서 내 상품 페이지 요청
    //  * 
    //  * @return
    //  */
    // @GetMapping("/main/myproducts")
    // public String myProducts(@RequestParam(name = "customerId")String customerId, Model model) {
    //     CustomerDTO customerDTO = customerService.getCustomer(customerId);
    //     model.addAttribute("customer", customerDTO);
    //     return "/main/myproducts";
    // }

    /**
     * main 폴더에 있는 마이 페이지 버튼 클릭 시 내가 판매하는 상품 목록 화면 요청
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
<<<<<<< HEAD
     * main페이지에서 상품등록하기 요청
=======
     * main페이지에서 상품등록 페이지 productsWrite 요청
>>>>>>> 85af773644e6ef7eeb2d4e793732f8ae59670c32
     * 
     * @return
     */
    @GetMapping("/main/productsWrite")
    public String productsWrite() {
        return "/main/productsWrite";
    }
}
