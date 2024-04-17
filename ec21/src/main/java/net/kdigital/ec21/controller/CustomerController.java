package net.kdigital.ec21.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import net.kdigital.ec21.dto.CustomerDTO;
import net.kdigital.ec21.service.CustomerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    
    //===================================  회원가입, 로그인, 로그아웃 ===============================
    /**
     * register(회원가입) 페이지 요청
     * 
     * @return
     */
    @GetMapping("/main/register")
    public String register() {
        return "/main/register";
    }

    /**
     * 회원가입 처리 요청
     * @param customerDTO
     * @return
     */
    @PostMapping("/main/registerProc")
    public String registerProc(@ModelAttribute CustomerDTO customerDTO) {
        customerService.insertCustomer(customerDTO);
        return "redirect:/";
    }

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
     * 로그아웃 요청 (처리 후 메인페이지로)
     * @param param
     * @return
     */
    @GetMapping("/main/logout")
    public String logout(@RequestParam String param) {
        return "redirect:/";
    }
    
    //=================================== 회원 마이페이지 ===========================================

    /**
     * myproducts(mypage) 페이지 요청
     * main 폴더에 있는 마이페이지 버튼 클릭 시 전달받은 회원ID가 판매하는 상품 목록 화면 요청
     * @return
     */
    @GetMapping("/main/myproducts")
    public String myProducts(@RequestParam(name = "customerId", defaultValue = "jooyoungyoon") String customerId, Model model) {
        CustomerDTO customerDTO = customerService.getCustomer(customerId);
        model.addAttribute("customer", customerDTO);
        return "/main/myproducts";
    }


    /**
     * myinfo(mypage) 페이지 요청
     * 마이페이지에서 Info 버튼 클릭 시 전달받은 or 로그인한 회원ID의 회원 정보가 채워진 상태의 화면 요청
     * @return
     */
    @GetMapping("/main/myinfo")
    public String myinfo(@RequestParam(name = "customerId", defaultValue = "jooyoungyoon") String customerId, Model model) {
        CustomerDTO customerDTO = customerService.getCustomer(customerId);
        model.addAttribute("customer", customerDTO);
        return "/main/myinfo";
    }
    
    /**
     * inbox (inquiry) 페이지 요청
     * 전달받은 or 로그인한 회원 ID에 해당하는 DTO를 model에 담아 인콰이어리 화면 요청
     * @return
     */
    @GetMapping("/main/inbox")
    public String inbox(@RequestParam(name = "customerId", defaultValue = "jooyoungyoon") String customerId, Model model) {
        CustomerDTO customerDTO = customerService.getCustomer(customerId);
        model.addAttribute("customer", customerDTO);
        return "/main/inbox";
    }

    

}
