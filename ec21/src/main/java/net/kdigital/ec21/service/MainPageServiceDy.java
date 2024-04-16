package net.kdigital.ec21.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.kdigital.ec21.dto.ProductDTO;
import net.kdigital.ec21.dto.check.ProductCategory;
import net.kdigital.ec21.dto.check.YesOrNo;
import net.kdigital.ec21.entity.ProductEntity;
import net.kdigital.ec21.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class MainPageServiceDy {
    private final ProductRepository productRepository;

    //===================================== main/index.html ======================================
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
        Page<ProductEntity> productPage = productRepository.findTopProductsByJudgeAndBlacklistCheck(YesOrNo.Y,
                YesOrNo.N, topEight);
        List<ProductEntity> entityList = productPage.getContent();

        entityList.forEach((entity)->{
            dtoList.add(ProductDTO.toDTO(entity, entity.getCustomerEntity().getCustomerId()));
        });

        return dtoList;
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
     * 전달받은 카테고리에 해당하는 상품을 DTO로 변환해 최신 등록일 순으로 리스트 반환 (for list.html)
     * @param category
     * @param searchWord 
     * @return
     */
    public List<ProductDTO> getProductList(String category, String searchWord) {
        List<ProductEntity> entityList = new ArrayList<>();
        List<ProductDTO> dtoList = new ArrayList<>();

        // 전체를 대상으로 조회
        if (category.equals("total")) {
            entityList = productRepository.findProductsByMultipleFieldsContaining(searchWord.toLowerCase(),Sort.by(Direction.DESC, "createDate"));
        } 
        // 전달받은 카테고리를 대상으로 조회
        else{
            // String -> Enum 타입으로 변경
            ProductCategory targetCategory = ProductCategory.valueOf(category);
            entityList = productRepository.findByCategoryAndMultipleFieldsContaining(targetCategory,searchWord,Sort.by(Direction.DESC, "createDate"));
        }

        entityList.forEach((entity)->{
            dtoList.add(ProductDTO.toDTO(entity, entity.getCustomerEntity().getCustomerId()));
        });

        return dtoList;
    }
}
