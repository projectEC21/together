package net.kdigital.ec21.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kdigital.ec21.dto.ProductDTO;
import net.kdigital.ec21.dto.ProhibitSimilarWordDTO;
import net.kdigital.ec21.dto.check.ProductCategory;
import net.kdigital.ec21.dto.check.YesOrNo;
import net.kdigital.ec21.dto.modelDTO.Lstm;
import net.kdigital.ec21.entity.CustomerEntity;
import net.kdigital.ec21.entity.ProductEntity;
import net.kdigital.ec21.entity.ProhibitSimilarWordEntity;
import net.kdigital.ec21.entity.ProhibitWordEntity;
import net.kdigital.ec21.repository.CustomerRepository;
import net.kdigital.ec21.repository.ProductRepository;
import net.kdigital.ec21.repository.ProhibitSimilarWordRepository;
import net.kdigital.ec21.repository.ProhibitWordRepository;
import net.kdigital.ec21.util.FileService;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final ProhibitSimilarWordRepository prohibitSimilarWordRepository;
    private final ProhibitWordRepository prohibitWordRepository;
    private final ModelService modelService;

    @Value("${spring.servlet.multipart.location}")
    String uploadPath;


    // ===========================  prodId 생성을 위한 함수 ====================================
    private static int serialNumber = 1;

    private static String getCurrentDateAsString() {
        // 현재 날짜를 yyyyMMdd 형식의 문자열로 변환
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return currentDate.format(formatter);
    }

    public static String generateId(String prefix) {
        // 두 글자의 prefix와 일련번호, 그리고 현재 날짜를 조합하여 아이디 생성
        String id = prefix + String.format("%05d", serialNumber) + "-" + getCurrentDateAsString();
        serialNumber++; // 일련번호 증가
        return id;
    }


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

    /**
     * 전달받은 productDTO의 productId에 해당하는 productEntity의 값을 수정하고 수정된 productDTO 반환
     * @param productDTO
     * @return
     */
    @Transactional
	public ProductDTO updateProduct(ProductDTO productDTO) {
        ProductEntity entity = productRepository.findById(productDTO.getProductId()).get();

        // 수정
        entity.setProductName(productDTO.getProductName());
        entity.setProductDesc(productDTO.getProductDesc());
        entity.setPrice(productDTO.getPrice());
        entity.setOrigin(productDTO.getOrigin());
        entity.setMoq(productDTO.getMoq());
        entity.setUnit(productDTO.getUnit());
        entity.setCategory(productDTO.getCategory());

        return ProductDTO.toDTO(entity, productDTO.getCustomerId());
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
    @Transactional
    public Boolean updateDeleteCheck(String productId) {
        ProductEntity entity = productRepository.findById(productId).get();
        entity.setProductDelete(YesOrNo.Y);
        return true;

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


    /**
     * 새로운 상품 등록 
     * @param productDTO
     */
    public void insertProduct(ProductDTO productDTO) {
        // 상품 등록하는 회원
        CustomerEntity customerEntity = customerRepository.findById(productDTO.getCustomerId()).get();

        // prodId 생성
        String categoryCode = productDTO.getCategory().getCategoryCode();
        String prodId = ProductService.generateId(categoryCode);
        productDTO.setProductId(prodId);

        // 대표 이미지 저장을 위한 경로 
        String originalFileName = null;
        String savedFileName = null;

        // 첨부파일이 있으면 파일명 세팅 실시
        if (!productDTO.getUploadImg().isEmpty()) {
            originalFileName = productDTO.getUploadImg().getOriginalFilename();
            savedFileName = FileService.saveFile(productDTO.getUploadImg(), uploadPath);

            productDTO.setOriginalFileName(originalFileName);
            productDTO.setSavedFileName(savedFileName); // entity로 변환 전 dto의 savedFileName 변경해주기
        }
        log.info("============ 파일 저장해써");

        // lstm
        Lstm lstm = new Lstm(productDTO.getProductName(), productDTO.getProductDesc()); // lstm 객체 생성

        List<Map<String, Object>> result = modelService.predictLSTM(lstm);
        log.info("============ {}", result);
        log.info("============ {}", result.get(0).get("lstm_predict"));

        Boolean lstmPredict = false;
        Double lstmPredictProba = 0.0;

        // 리스트 사이즈 == 1 : lstm 값이 1인 경우 , lstm 값이 0인데 금지어 유사도가 결과가 나오지 않은 경우
        if (result.size() == 1) {
            lstmPredict = String.valueOf(result.get(0).get("lstm_predict")).equals("1") ? true : false;
            lstmPredictProba = Double.parseDouble(String.valueOf(result.get(0).get("lstm_predict_proba")));
        } // 리스트 사이즈 >= 1 : lstm 값이 0이고 금지어 유사도 결과가 1개 이상 나온 경우
        else {
            lstmPredict = String.valueOf(result.get(0).get("lstm_predict")).equals("1") ? true : false;
            lstmPredictProba = Double.parseDouble(String.valueOf(result.get(0).get("lstm_predict_proba")));

            result.remove(0);

            // 중복제거
            // LinkedHashSet을 사용하여 중복 제거하면서 순서 유지
            Set<Map<String, Object>> resultSet = new LinkedHashSet<>(result);
            result.clear();
            result.addAll(resultSet);

            for (int i = 0; i < result.size(); i++) {

                String similarWord = String.valueOf(result.get(i).get("Similar_Word"));
                String prohibitWord = String.valueOf(result.get(i).get("Prohibited_Word"));
                Double similarProba = Double.parseDouble(String.valueOf(result.get(i).get("Similarity_Score")));

                ProhibitSimilarWordDTO prohibitDTO = new ProhibitSimilarWordDTO(null,similarWord, similarProba,
                        prohibitWord, productDTO.getProductId());
                log.info("{}", prohibitDTO);
                
                // 여기서 prohibitSmilarDB에 save하면 될 듯
                ProhibitWordEntity prohibitWordEntity = prohibitWordRepository.findById(prohibitWord).get();
                ProductEntity productEntity = ProductEntity.toEntity(productDTO, customerEntity);
                prohibitSimilarWordRepository.save(ProhibitSimilarWordEntity.toEntity(prohibitDTO, prohibitWordEntity , productEntity));
                
            }
        }

        productDTO.setLstmPredict(lstmPredict);
        productDTO.setLstmPredictProba(lstmPredictProba);
        // lstmPredict가 1이면 judge를 Y로.. 0이면 null로 
        if (lstmPredict) {
            productDTO.setJudge(YesOrNo.Y);
        }
        log.info("============ dto에 저장해써 : {}", lstmPredict);

        // productDTO -> productEntity 변환 후 DB에 저장
        productRepository.save(ProductEntity.toEntity(productDTO,customerEntity));

    }
    
}
