package net.kdigital.ec21.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import net.kdigital.ec21.dto.ProductDTO;
import net.kdigital.ec21.service.MainPageServiceDy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class MainPageControllerDY {
    private final MainPageServiceDy mainService;

    //================= main/index.html =====================
    
    /**
     * 전달받은 상품 아이디에 해당하는 상품DTO를 model에 담아 상품 디테일 페이지로 보냄
     * @param productId
     * @param model
     * @return
     */
    @GetMapping("/main/productsDetail")
    public String productsDetail(@RequestParam(name = "productId") String productId, Model model) {
        ProductDTO dto = mainService.getProduct(productId);
        model.addAttribute("product", dto);
        return "/main/productsDetail";
    }

    /**
     * 전달받은 상품 카테고리에 해당하는 상품 리스트를 model에 담아 상품 목록 페이지로 보냄
     * @param category
     * @param model
     * @return
     */
    @GetMapping("/main/list")
    public String list(@RequestParam(name = "category", defaultValue = "total") String category, 
                        @RequestParam(name = "searchWord", defaultValue = "") String searchWord, Model model) {
        List<ProductDTO> dtoList = mainService.getProductList(category,searchWord);
        model.addAttribute("list", dtoList);
        return "/main/list";
    }
    
    

    
}
