package net.kdigital.ec21.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kdigital.ec21.dto.ProductDTO;
import net.kdigital.ec21.dto.check.ProductCategory;
import net.kdigital.ec21.dto.check.YesOrNo;
import net.kdigital.ec21.entity.CustomerEntity;
import net.kdigital.ec21.entity.ProductEntity;
import net.kdigital.ec21.repository.CustomerRepository;
import net.kdigital.ec21.repository.ProductRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    //===================================== main/index ======================================
    /**
     * hitCount기준 DESC, createDate 기준 DESC 순으로 8개를 가져옴
     * judge=='Y' && customer_id에 해당하는 Customer의 blacklist_check=='N'
     * @return
     */
    public List<ProductDTO> getTopProductList() {
        List<ProductDTO> dtoList = new ArrayList<>();
        // Pageable에 정렬조건과 조회 개수 조건 담아서 보냄
        Pageable topEight = PageRequest.of(0, 8, Sort.by(
                Sort.Order.desc("hitCount"),
                Sort.Order.desc("lstmPredictProba"),
                Sort.Order.desc("createDate")));
        Page<ProductEntity> productPage = productRepository.findTopProductsByJudgeAndBlacklistCheckAndNotDeleted(YesOrNo.Y,
                YesOrNo.N, topEight);
        List<ProductEntity> entityList = productPage.getContent();
        
        entityList.forEach((entity)->{
            dtoList.add(ProductDTO.toDTO(entity, entity.getCustomerEntity().getCustomerId()));
        });

        return dtoList;
    }
    

    //===================================== main/productsDetail ======================================
    
    /**
     * productDetail 요청이 있을 때 해당 productId에 해당하는 Product의 hitCount값에 +1 하는 함수
     * @param productId
     */
    @Transactional
    public void updateHitCount(String productId) {
        ProductEntity entity = productRepository.findById(productId).get();
        entity.setHitCount(entity.getHitCount()+1);
    }

    /**
     * 전달받은 상품 아이디에 해당하는 상품을 DTO로 변환해 반환 (for productsDetail.html)
     * @param productId
     * @return
     */
    public ProductDTO getProduct(String productId) {
        ProductEntity entity = productRepository.findById(productId).get();
        
        return ProductDTO.toDTO(entity, entity.getCustomerEntity().getCustomerId());
    }

    /**
     * 전달받은 카테고리에 해당하는 상품들 중 전달받은 상품Id에 해당하는 상품은 제외한 상품들 중 최대 5개를 리스트로 반환하는 함수 
     * (hitCount,lstmPredictProba,createDate 기준으로 Decs 순으로 top 5개)
     * @param productCategory
     * @return
     */
    public List<ProductDTO> getSameCategoryProducts(ProductCategory targetCategory, String productId) {
        List<ProductDTO> result = new ArrayList<>();
        // top 5개 가져오기 
        // Pageable에 정렬 조건과 개수 조건 담아서 보내기
        Pageable topFive = PageRequest.of(0, 5, Sort.by(
                Sort.Order.desc("hitCount"),
                Sort.Order.desc("lstmPredictProba"),
                Sort.Order.desc("createDate")));
        Page<ProductEntity> productPage = productRepository.findTopProductsByCategoryAndJudgeAndBlacklistCheckAndNotDeletedExcludingProductId(targetCategory,productId,topFive);
        List<ProductEntity> entityList = productPage.getContent();

        entityList.forEach((entity)->{
            result.add(ProductDTO.toDTO(entity, entity.getCustomerEntity().getCustomerId()));
        });
        return result;
    }


    //===================================== main/list ======================================
    
    /**
     * 전달받은 카테고리에 해당하고 상품명에 입력받은 검색어가 포함된 상품을 
     * DTO로 변환해 최신 등록일 순으로 리스트 반환 (for list.html)
     *  + 추가 조건 : (judge==Y & deleteCheck==N & customerId의 blacklistCheck==N)
     * @param category
     * @param searchWord 
     * @return
     */
    public List<ProductDTO> getProductList(String category, String searchWord) {
        List<ProductEntity> entityList = new ArrayList<>();
        List<ProductDTO> dtoList = new ArrayList<>();

        // 전체를 대상으로 조회 (judge==Y & deleteCheck==N & customerId의 blacklistCheck==N)
        if (category.equals("total")) {
            entityList = productRepository.findProductsByProductNameContainingAndAdditionalConditions(searchWord.toLowerCase());
        } 
        // 전달받은 카테고리를 대상으로 조회 (judge==Y & deleteCheck==N & customerId의 blacklistCheck==N)
        else{
            // String -> Enum 타입으로 변경
            ProductCategory targetCategory = ProductCategory.valueOf(category);
            entityList = productRepository.findProductsByProductNameContainingAndAdditionalConditionsAndCategory(searchWord,targetCategory);
        }

        entityList.forEach((entity)->{
            dtoList.add(ProductDTO.toDTO(entity, entity.getCustomerEntity().getCustomerId()));
        });

        return dtoList;
    }

    // ===================================== main/myproducts ======================================

    /**
     * 전달 받은 상품ID에 해당하는 상품의 productDelete 값을 Y로 변경하는 함수
     * @param productId
     */
    public void updateDeleteCheck(String productId) {
        ProductEntity entity = productRepository.findById(productId).get();
        entity.setProductDelete(YesOrNo.Y);
    }

    /**
     * 전달받은 회원 ID에 해당하는 entity를 찾아서 entity가 자식으로 갖는 ProductEntity를 반환하는 함수 (productDelete가 N인 상품들만 포함)
     * @param customerId
     * @return
     */
    public List<ProductDTO> getCustomerProducts(String customerId) {
        CustomerEntity customerEntity = customerRepository.findById(customerId).get();
        List<ProductDTO> result = new ArrayList<>();

        List<ProductEntity> productList = customerEntity.getProductEntity();
        
        productList.forEach((entity)->{
            // productDelete 값이 Y인 경우는 제외
            if (entity.getProductDelete() == YesOrNo.N) {
                result.add(ProductDTO.toDTO(entity, customerId));
            }
        });

        return result;
    }








    
}
