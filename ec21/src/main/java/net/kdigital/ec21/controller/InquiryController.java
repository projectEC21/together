package net.kdigital.ec21.controller;

import java.util.List;

import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.kdigital.ec21.dto.CustomerDTO;
import net.kdigital.ec21.dto.ProductDTO;
import net.kdigital.ec21.service.CustomerService;
import net.kdigital.ec21.service.ProductService;

@Controller
@Slf4j
@RequiredArgsConstructor
public class InquiryController {
    private final CustomerService customerService;
    private final ProductService productService;

    /**
     *  보낸 인콰이어리
     * @param customerId
     * @param model
     * @return
     */
    @GetMapping("/main/inboxSended")
    public String inboxSended(
        @RequestParam(name = "customerId", defaultValue = "jooyoungyoon") String customerId,
            Model model ){

                CustomerDTO customerDTO = customerService.getCustomer(customerId);
                model.addAttribute("customer", customerDTO);
                
                List<ProductDTO> productList = productService.getCustomerProducts(customerId);
                model.addAttribute("customerId", customerId);
                model.addAttribute("productList", productList);
        
        return "main/inboxSended";
    }

    /**
     * 저장된 메세지
     * @param customerId
     * @param model
     * @return
     */
    @GetMapping("/main/inboxSaved")
    public String inboxSaved(
        @RequestParam(name = "customerId", defaultValue = "jooyoungyoon") String customerId,
            Model model ){

                CustomerDTO customerDTO = customerService.getCustomer(customerId);
                model.addAttribute("customer", customerDTO);
                
                List<ProductDTO> productList = productService.getCustomerProducts(customerId);
                model.addAttribute("customerId", customerId);
                model.addAttribute("productList", productList);
        
        return "main/inboxSaved";
    }

    /**
     * 스팸 메세지
     * @param customerId
     * @param model
     * @return
     */
    @GetMapping("/main/inboxSpam")
    public String inboxSpam(
        @RequestParam(name = "customerId", defaultValue = "jooyoungyoon") String customerId,
            Model model ){

                CustomerDTO customerDTO = customerService.getCustomer(customerId);
                model.addAttribute("customer", customerDTO);
                
                List<ProductDTO> productList = productService.getCustomerProducts(customerId);
                model.addAttribute("customerId", customerId);
                model.addAttribute("productList", productList);
        
        return "main/inboxSpam";
    }

    /**
     * 차단한 메세지
     * @param customerId
     * @param model
     * @return
     */
    @GetMapping("/main/inboxBlock")
    public String inboxBlock(
        @RequestParam(name = "customerId", defaultValue = "jooyoungyoon") String customerId,
            Model model ){

                CustomerDTO customerDTO = customerService.getCustomer(customerId);
                model.addAttribute("customer", customerDTO);
                
                List<ProductDTO> productList = productService.getCustomerProducts(customerId);
                model.addAttribute("customerId", customerId);
                model.addAttribute("productList", productList);
        
        return "main/inboxBlock";
    }
    
    /**
     * 쓰레기통으로 보낸 메세지
     * @param customerId
     * @param model
     * @return
     */
    @GetMapping("/main/inboxTrash")
    public String inboxTrash(
        @RequestParam(name = "customerId", defaultValue = "jooyoungyoon") String customerId,
            Model model ){

                CustomerDTO customerDTO = customerService.getCustomer(customerId);
                model.addAttribute("customer", customerDTO);
                List<ProductDTO> productList = productService.getCustomerProducts(customerId);
                model.addAttribute("customerId", customerId);
                model.addAttribute("productList", productList);
        
        return "main/inboxTrash";
    }


}
