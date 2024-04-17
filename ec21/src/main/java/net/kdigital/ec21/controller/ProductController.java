package net.kdigital.ec21.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kdigital.ec21.dto.ProductDTO;
import net.kdigital.ec21.service.ProductService;


@Controller
@Slf4j
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    /**
     * main/myproducts에서 상품등록 페이지 productsWrite 요청 (회원ID를 받아서 model에 담아 보냄)
     * 
     * @return
     */
    @GetMapping("main/productsWrite")
    public String productsWrite(@RequestParam(name = "customerId", defaultValue = "jooyoungyoon") String customerId, Model model) {
        model.addAttribute("customerId", customerId);
        return "main/productsWrite";
    }

    /**
     * 전달받은 상품 아이디에 해당하는 상품DTO와 해당 상품과 동일한 카테고리에 속한 상품들 최대 5개를 
     *  model에 담아 상품 상세 정보 페이지로 보냄
     * @param productId
     * @param model
     * @return
     */
    @GetMapping("main/productsDetail")
    public String productsDetail(@RequestParam(name = "productId", defaultValue = "CO00006-20240409") String productId, Model model) {
        
        // productId에 해당하는 Product 가져오기
        ProductDTO dto = productService.getProduct(productId);

        // productId에 해당하는 Product와 동일한 카테고리의 상품들 최대 5개 가져오기
        List<ProductDTO> dtoList = productService.getSameCategoryProducts(dto.getCategory(), productId);

        log.info("지금 컨트롤러에서 dto랑 관련 리스트 가져왔어. 이제 조회수 증가할 거야");
        // productId에 해당하는 Product의 hitCount 증가
        productService.updateHitCount(productId);
        
        model.addAttribute("product", dto);
        model.addAttribute("list", dtoList);
        
        log.info("지금 모델에 다 담았어. 이제 html로 갈거야");
        

        return "main/productsDetail";
    }
    

    /**
     * 전달받은 상품 카테고리와 검색어에 해당하는 상품 리스트를 model에 담아 상품 목록 페이지로 보냄
     * 
     * @param category
     * @param model
     * @return 
     */
    @GetMapping("main/list")
    public String list(@RequestParam(name = "category", defaultValue = "total") String category,
            @RequestParam(name = "searchWord", defaultValue = "") String searchWord, Model model) {
        List<ProductDTO> dtoList = productService.getProductList(category, searchWord);
        log.info("=========== 카테고리 : {}",category);
        log.info("=========== 검색어 : {}",searchWord);

        model.addAttribute("list", dtoList);
        model.addAttribute("category", category);
        model.addAttribute("searchWord", searchWord);
        
        return "main/list";
    }


}
