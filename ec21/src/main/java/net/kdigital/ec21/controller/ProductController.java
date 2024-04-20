package net.kdigital.ec21.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kdigital.ec21.dto.CustomerDTO;
import net.kdigital.ec21.dto.ProductDTO;
import net.kdigital.ec21.service.CustomerService;
import net.kdigital.ec21.service.ProductService;



@Controller
@Slf4j
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CustomerService customerService;
    
    // ====================================== 상품 등록 ========================================
    /**
     * main/myproducts에서 상품 등록 페이지 productsWrite 요청 (회원ID를 받아서 model에 담아 보냄)
     * @return
     */
    @GetMapping("main/productsWrite")
    public String productsWrite(@RequestParam(name = "customerId", defaultValue = "jooyoungyoon") String customerId, Model model) {
        model.addAttribute("customerId", customerId);
        return "main/productsWrite";
    }

    /**
     * 상품 등록 페이지에서 전달된 ProductDTO를 받아 DB에 저장 후 마이페이지 상품 리스트 화면 요청
     * @param entity
     * @return
     */
    @PostMapping("main/productInsert")
    public String productInsert(@ModelAttribute ProductDTO productDTO, Model model) {
        
        productService.insertProduct(productDTO);

        // 회원ID가 판매하는 상품 목록
        List<ProductDTO> productList = productService.getCustomerProducts(productDTO.getCustomerId());

        model.addAttribute("customerId", productDTO.getCustomerId());
        model.addAttribute("productList", productList);
        
        return "main/myproducts";
    }
    


    // ====================================== 상품 디테일 ========================================
    // 관리자 : 이상 버튼 (GET:/productsDetail/updateJudgeWeird) , 정상 버튼 (GET:/productsDetail/updateJudgeNormal)
    // 상품 등록한 사람 : edit버튼 (GET:/main/productsUpdate), delete 버튼 (GET:/main/productsDelete)
    /**
     * 전달받은 상품 아이디에 해당하는 상품DTO와 해당 상품과 동일한 카테고리에 속한 상품들 최대 5개를 
     *  model에 담아 상품 상세 정보 페이지로 보냄
     * @param productId
     * @param model
     * @return
     */
    @GetMapping("main/productsDetail")
    public String productsDetail(@RequestParam(name = "productId", defaultValue = "CO00006-20240409") String productId,
                                @RequestParam(name = "category", defaultValue = "total") String category,
                                @RequestParam(name = "searchWord", defaultValue = "") String searchWord, Model model) {
        // productId에 해당하는 Product의 hitCount 증가
        productService.updateHitCount(productId);
        
        // productId에 해당하는 Product 가져오기
        ProductDTO dto = productService.getProduct(productId);
        
        // productId에 해당하는 Product와 동일한 카테고리의 상품들 최대 5개 가져오기
        List<ProductDTO> dtoList = productService.getSameCategoryProducts(dto.getCategory(), productId);
        

        model.addAttribute("product", dto);
        model.addAttribute("list", dtoList);
        model.addAttribute("category", category);
        model.addAttribute("searchWord", searchWord);
        
        return "main/productsDetail";
    }
    
    // ====================================== 상품 수정 ========================================

    /**
     * 상품 수정 페이지 요청 (회원ID에 해당하는 회원DTO를 받아서 model에 담아 보냄)
     * 
     * @return
     */
    @GetMapping("main/productsUpdate")
    public String productsUpdate(@RequestParam(name = "productId", defaultValue = "CO00006-20240409") String productId,
            Model model) {
        // productId에 해당하는 Product 가져오기
        ProductDTO dto = productService.getProduct(productId);
        model.addAttribute("product", dto);
        // return "main/productsWrite";
        return "main/productsUpdate";
    }
    /**
     * 상품 수정 처리 요청 (회원ID에 해당하는 회원DTO를 받아서 model에 담아 보냄)
     * 
     * @return
     */
    @PostMapping("main/productsUpdateProc")
    public String productsUpdate(@ModelAttribute ProductDTO productDTO, Model model) {
        // 수정될 값이 담긴 DTO 받아서 해당 productId의 Product의 값 다시 세팅
        ProductDTO dto = productService.updateProduct(productDTO);

        // productId에 해당하는 Product와 동일한 카테고리의 상품들 최대 5개 가져오기
        List<ProductDTO> dtoList = productService.getSameCategoryProducts(dto.getCategory(), dto.getProductId());

        model.addAttribute("product", dto);
        model.addAttribute("list", dtoList);

        return "main/productsDetail";
    }
    
    // ====================================== 상품 삭제 ========================================

    /**
     * ajax : 상품 상세 페이지에서 상품 삭제(삭제하고자 하는 productId 받아서 해당 product의 productDelete 값 Y로 변경) 요청 후 
     * boolean 값 반환
     * 
     * @return
     */
    @GetMapping("main/productsDelete")
    @ResponseBody
    public Boolean productsDelete(@RequestParam(name = "productId", defaultValue = "CO00006-20240409") String productId) {
        
        // 상품 삭제 여부 값 변경 (N->Y)
        return productService.updateDeleteCheck(productId);
    }



    //================================= 메인화면 상품 목록(카테고리별) ===================================
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
        log.info("=========== 카테고리 : {}",category);
        log.info("=========== 검색어 : {}",searchWord);
        List<ProductDTO> dtoList = productService.getProductList(category, searchWord);

        model.addAttribute("list", dtoList);
        model.addAttribute("category", category);
        model.addAttribute("searchWord", searchWord);
        
        return "main/list";
    }

    
    


}
