package net.kdigital.ec21.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import net.kdigital.ec21.dto.ProductDTO;
import net.kdigital.ec21.service.ProductService;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService ProductService;
    
    // /**
    // * main페이지에서 내 상품 페이지 요청
    // *
    // * @return
    // */
    // @GetMapping("/main/myproducts")
    // public String myProducts(@RequestParam(name = "customerId")String customerId,
    // Model model) {
    // CustomerDTO customerDTO = customerService.getCustomer(customerId);
    // model.addAttribute("customer", customerDTO);
    // return "/main/myproducts";
    // }

    /**
     * main 폴더에 있는 마이페이지 버튼 클릭 시 내가 판매하는 상품 목록 화면 요청
     * 
     * @return
     */
    @GetMapping("/main/myproducts")
    public String myProducts() {
        return "/main/myproducts";
    }

    /**
     * main페이지에서 상품등록 페이지 productsWrite 요청
     * 
     * @return
     */
    @GetMapping("/main/productsWrite")
    public String productsWrite() {
        return "/main/productsWrite";
    }

    /**
     * 전달받은 상품 아이디에 해당하는 상품DTO를 model에 담아 상품 디테일 페이지로 보냄
     * 
     * @param productId
     * @param model
     * @return
     */
    @GetMapping("/main/productsDetail")
    public String productsDetail(@RequestParam(name = "productId") String productId, Model model) {
        ProductDTO dto = ProductService.getProduct(productId);
        model.addAttribute("product", dto);
        return "/main/productsDetail";
    }

    /**
     * 전달받은 상품 카테고리에 해당하는 상품 리스트를 model에 담아 상품 목록 페이지로 보냄
     * 
     * @param category
     * @param model
     * @return
     */
    @GetMapping("/main/list")
    public String list(@RequestParam(name = "category", defaultValue = "total") String category,
            @RequestParam(name = "searchWord", defaultValue = "") String searchWord, Model model) {
        List<ProductDTO> dtoList = ProductService.getProductList(category, searchWord);
        model.addAttribute("list", dtoList);
        return "/main/list";
    }


}
