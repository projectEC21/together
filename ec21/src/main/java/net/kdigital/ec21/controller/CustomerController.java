package net.kdigital.ec21.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kdigital.ec21.dto.CustomerDTO;
import net.kdigital.ec21.dto.ProductDTO;
import net.kdigital.ec21.service.CustomerService;
import net.kdigital.ec21.service.ProductService;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final ProductService productService;

    // ================== 회원가입, 로그인, 로그아웃 ====================
    /**
     * register(회원가입) 페이지 요청
     * 
     * @return
     */
    @GetMapping("/main/register")
    public String register() {
        return "main/register";
    }

    /**
     * 회원가입 처리 요청
     * 
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
        return "main/login";
    }

    /**
     * 로그아웃 요청 (처리 후 메인페이지로)
     * 
     * @param param
     * @return
     */
    @GetMapping("/main/logout")
    public String logout(@RequestParam String param) {
        return "redirect:/";
    }

    // ============== 회원 마이페이지 =================

    /**
     * myproducts(mypage) 페이지 요청
     * main 폴더에 있는 마이페이지 버튼 클릭 시 전달받은 회원ID가 판매하는 상품 목록 화면 요청
     * 
     * @return
     */
    @GetMapping("/main/myproducts")
    public String myProducts(@RequestParam(name = "customerId", defaultValue = "jooyoungyoon") String customerId,
            @RequestParam(name = "category", defaultValue = "total") String category,
            @RequestParam(name = "searchWord", defaultValue = "") String searchWord, Model model) {

        // Flash Attribute를 통해 전달받은 상품 목록이 없는 경우 
        if (!model.containsAttribute("productList")) {
            // 회원ID에 해당하는 회원이 판매하고 있는 상품 리스트
            List<ProductDTO> productList = productService.getCustomerProducts(customerId);
            model.addAttribute("productList", productList);
        }

        model.addAttribute("customerId", customerId);
        model.addAttribute("category", category);
        model.addAttribute("searchWord", searchWord);
        
        return "main/myproducts";
    }


    /**
     * [마이페이지 > 나의 상품 목록 화면에서 delete]
     * 전달 받은 상품ID에 해당하는 상품의 삭제 요청 및
     * 다시 마이페이지의 자신이 판매하는 상품목록 화면 재요청 처리 (상품페이지에서 delete 버튼 클릭시는 다른 요청의 형태로 만들 것임)
     * 
     * @param productId
     * @return
     */
    @GetMapping("main/myproducts/delete")
    public String myProductsDelete(
            @RequestParam(name = "productId", defaultValue = "CO00006-20240409") String productId,
            @RequestParam(name = "customerId", defaultValue = "jooyoungyoon") String customerId,
            RedirectAttributes attributes) {

        // 상품ID의 productDelete값 변경 (N->Y)
        productService.updateDeleteCheck(productId);

        // 회원ID가 판매하는 상품 목록
        List<ProductDTO> productList = productService.getCustomerProducts(customerId);
        // 리다이렉트 시 Flash Attributes에 상품 목록 저장
        attributes.addFlashAttribute("productList", productList);

        attributes.addAttribute("customerId", customerId);

        return "redirect:/main/myproducts";
    }

    /**
     * myinfo(mypage) 페이지 요청
     * 마이페이지에서 Info 버튼 클릭 시 전달받은 or 로그인한 회원ID의 회원 정보가 채워진 상태의 화면 요청
     * 
     * @return
     */
    @GetMapping("/main/myinfo")
    public String myinfo(@RequestParam(name = "customerId", defaultValue = "jooyoungyoon") String customerId,
            Model model) {
        CustomerDTO customerDTO = customerService.getCustomer(customerId);
        model.addAttribute("customer", customerDTO);
        return "main/myinfo";
    }

    /**
     * inbox (inquiry) 페이지 요청
     * 전달받은 or 로그인한 회원 ID에 해당하는 DTO를 model에 담아 인콰이어리 화면 요청
     * 
     * @return
     */
    @GetMapping("/main/inbox")
    public String inbox(@RequestParam(name = "customerId", defaultValue = "jooyoungyoon") String customerId,
            Model model) {
        CustomerDTO customerDTO = customerService.getCustomer(customerId);
        model.addAttribute("customer", customerDTO);

        // ==================================================================================================================
        // 수정 예정 : 지금 일단 myproducts와 동일한 html 쓰고 있어서!!!! 일단 myproduct에서 필요한 애들 model에
        // 담아서 보냄
        // 회원ID에 해당하는 회원이 판매하고 있는 상품 리스트
        List<ProductDTO> productList = productService.getCustomerProducts(customerId);
        model.addAttribute("customerId", customerId);
        model.addAttribute("productList", productList);
        // ==================================================================================================================

        return "main/inbox";
    }

}
