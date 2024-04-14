package net.kdigital.ec21.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kdigital.ec21.dto.BlacklistDTO;
import net.kdigital.ec21.dto.CustomerDTO;
import net.kdigital.ec21.dto.ModelPredictDTO;
import net.kdigital.ec21.dto.ProductDTO;
import net.kdigital.ec21.dto.ReportedCustomerWithInfoDTO;
import net.kdigital.ec21.dto.check.ProductCategory;
import net.kdigital.ec21.dto.check.YesOrNo;
import net.kdigital.ec21.entity.BlacklistEntity;
import net.kdigital.ec21.entity.CustomerEntity;
import net.kdigital.ec21.entity.ProductEntity;
import net.kdigital.ec21.entity.ProhibitSimilarWordEntity;
import net.kdigital.ec21.entity.ReportCustomerEntity;
import net.kdigital.ec21.repository.BlacklistRepository;
import net.kdigital.ec21.repository.CustomerRepository;
import net.kdigital.ec21.repository.ProductRepository;
import net.kdigital.ec21.repository.ProhibitSimilarWordRepository;
import net.kdigital.ec21.repository.ReportCustomerRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerService {
    // ==================================== Repository ====================================
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final ReportCustomerRepository reportCustomerRepository;
    private final BlacklistRepository blacklistRepository;
    private final ProhibitSimilarWordRepository prohibitSimilarWordRepository;

    
    
    //==================================== 메인 보드 ====================================
    
    /**
     * 당일 등록된 상품 개수, 당일 이상상품 개수, 당일 등록한 고객 수, 미처리된 신고 개수 반환하는 함수
     * 
     * @return
     */
    public Map<String, Long> getCount() {
        Map<String, Long> result = new HashMap<>();

        // 시스템상 당일 정보
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().plusDays(1).atStartOfDay();

        // 당일 등록 상품 개수
        Long todayRegisteredProd = productRepository.countByCreateDateBetween(todayStart, todayEnd);
        result.put("todayRegisteredProd", todayRegisteredProd);

        // 당일 등록된 이상 상품 개수
        Long todayWeirdProd = productRepository.countByCreateDateBetweenAndLstmPredict(todayStart, todayEnd, false);
        result.put("todayWeirdProd", todayWeirdProd);

        // 당일 등록한 회원 수
        Long todayRegisteredCustomer = customerRepository.countByCreateDateBetween(todayStart, todayEnd);
        result.put("todayRegisteredCustomer", todayRegisteredCustomer);

        // 미처리된 신고 개수
        Long unprocessedReport = reportCustomerRepository.countByManagerCheck(YesOrNo.N);
        result.put("unprocessedReport", unprocessedReport);

        return result;
    }



    //==================================== 상품관리 ======================================
    
    /**
     * 모든 상품 DTO 리스트로 반환하는 함수
     * 
     * @return
     */
    public List<ProductDTO> selectAll() {
        List<ProductEntity> entityList = productRepository.findAll(Sort.by(Direction.DESC,"createDate"));
        List<ProductDTO> dtoList = new ArrayList<>();
        entityList.forEach((entity) -> {
            dtoList.add(ProductDTO.toDTO(entity, entity.getCustomerEntity().getCustomerId()));
        });
        return dtoList;
    }

    /**
     * 전달받은 카테고리와 검색어에 해당하는 상품DTO 리스트 반환 (디폴트 : total/"")
     * @param category
     * @param searchWord
     * @return
     */
    public List<ProductDTO> selectProductBySearch(String category, String searchWord) {
        List<ProductEntity> productEntities = new ArrayList<>();
        List<ProductDTO> dtoList = new ArrayList<>();

        if (category.equals("total")) {
            // 회원ID, 상품ID, 상품명에 검색어가 포함된 상품들을 최신 등록일 순으로 가져오기
            productEntities = productRepository.findByMultipleFieldsContaining(searchWord.toLowerCase(), Sort.by(Direction.DESC, "createDate"));
        }else{
            // 카테고리 타입 변경 : String -> Enum
            ProductCategory targetCategory = ProductCategory.valueOf(category);
            // 전달받은 카테고리에 해당하고 회원ID, 상품ID, 상품명에 검색어가 포함된 상품들을 최신 등록일 순으로 가져오기
            productEntities = productRepository.findByCategoryAndMultipleFieldsContaining(targetCategory, searchWord.toLowerCase(), Sort.by(Direction.DESC, "createDate"));
        }

        productEntities.forEach((entity)->{
            dtoList.add(ProductDTO.toDTO(entity, entity.getCustomerEntity().getCustomerId()));
        });

        return dtoList;
    }


    /**
     * lstm 예측값이 0(false)이고 관리자가 미처리한 상품들을 
     * 새로운 ModelPredictDTO에 담아 리스트로 반환하는 함수
     * @return
     */
    public List<ModelPredictDTO> selectAllModelPredictWeird() {

        // lstm==false && judge==null인 상품 데이터들 리스트로 가져오기
        List<ProductEntity> productEntities = productRepository.findByLstmPredictAndJudgeOrderByCreateDateDesc(false,null);
        List<ModelPredictDTO> result = new ArrayList<>();

        productEntities.forEach((prodEntity)->{
            // productId에 해당하는 금지어유사도 결과 데이터들 가져오기 (금지어 유사 확률 높은 순)
            List<ProhibitSimilarWordEntity> entityList = prohibitSimilarWordRepository.findProbaByProductEntity_ProductIdOrderBySimilarProbaDesc(prodEntity.getProductId());
            // 금지 유사확률이 가장 높은 데이터 가져오기
            ProhibitSimilarWordEntity prohibitSimilarWordEntity = entityList.get(0); 
            
            // 화면 출력을 위한 새로운 ModelPredictDTO 생성
            ModelPredictDTO dto = new ModelPredictDTO(prodEntity.getCustomerEntity().getCustomerId(), prodEntity.getProductId(), 
                prodEntity.getProductName(), prodEntity.getProductDesc(), 
                prodEntity.getLstmPredictProba(), prodEntity.isLstmPredict(), 
                prohibitSimilarWordEntity.getSimilarWord(), prohibitSimilarWordEntity.getSimilarProba(), 
                prohibitSimilarWordEntity.getProhibitWordEntity().getProhibitWord(), 
                prohibitSimilarWordEntity.getProhibitWordEntity().getProhibitReason());
            
            result.add(dto);
        });
        return result;
    }

    /**
     * lstm 예측값이 0(false)이고 관리자가 미처리한 상품들 중
     *  전달받은 카테고리와 검색어에 해당하는 상품들을 
     *  새로운 ModelPredictDTO에 담아 리스트로 반환하는 함수
     * @param category
     * @param searchWord
     * @return
     */
    public List<ModelPredictDTO> selectModelPredictWeirdBySearch(String category, String searchWord) {
        List<ProductEntity> productEntities = new ArrayList<>();
        List<ModelPredictDTO> result = new ArrayList<>();
        if (category.equals("total")) {
            // lstm==false && judge==null && 회원ID, 상품ID, 상품명에 검색어가 포함된 상품들을 최신 등록일 순으로 가져오기
            productEntities = productRepository.findByMultipleFieldsContainingOrderByCreateDateDesc(searchWord.toLowerCase());
        } else {
            // 카테고리 타입 변경 : String -> Enum
            ProductCategory targetCategory = ProductCategory.valueOf(category);
            // 전달받은 카테고리에 해당하고 회원ID, 상품ID, 상품명에 검색어가 포함된 상품들을 최신 등록일 순으로 가져오기
            productEntities = productRepository.findByCategoryAndMultipleFieldsContainingOrderByCreateDateDesc(targetCategory, searchWord.toLowerCase());
        }

        productEntities.forEach((prodEntity) -> {
            // productId에 해당하는 금지어유사도 결과 데이터들 가져오기 (금지어 유사 확률 높은 순)
            List<ProhibitSimilarWordEntity> entityList = prohibitSimilarWordRepository
                    .findProbaByProductEntity_ProductIdOrderBySimilarProbaDesc(prodEntity.getProductId());
            // 금지 유사확률이 가장 높은 데이터 가져오기
            ProhibitSimilarWordEntity prohibitSimilarWordEntity = entityList.get(0);

            // 화면 출력을 위한 새로운 ModelPredictDTO 생성
            ModelPredictDTO dto = new ModelPredictDTO(prodEntity.getCustomerEntity().getCustomerId(),
                    prodEntity.getProductId(),
                    prodEntity.getProductName(), prodEntity.getProductDesc(),
                    prodEntity.getLstmPredictProba(), prodEntity.isLstmPredict(),
                    prohibitSimilarWordEntity.getSimilarWord(), prohibitSimilarWordEntity.getSimilarProba(),
                    prohibitSimilarWordEntity.getProhibitWordEntity().getProhibitWord(),
                    prohibitSimilarWordEntity.getProhibitWordEntity().getProhibitReason());

            result.add(dto);
        });

        return result;
    }


    //==================================== 회원관리 ======================================

    /**
     * 정상회원(blacklist_check==N)인 CustomerDTO 최신 가입 순으로 반환
     */
    public List<CustomerDTO> selectNotBlacklist() {
        List<CustomerEntity> entityList = customerRepository.findByBlacklistCheckOrderByCreateDateDesc(YesOrNo.N);
        List<CustomerDTO> dtoList = new ArrayList<>();
        entityList.forEach((entity) -> {
            dtoList.add(CustomerDTO.toDTO(entity));
        });

        return dtoList;
    }

    /**
     * 관리자가 처리하지 않은 신고당한 회원 DTO를 반환하고자 하는데 회원의 기본적인 정보를 같이 담기 위해 새로운 DTO에 담아 리스트를
     * 반환하는 함수
     * (신고당한 회원 ID, 신고회원테이블ID, 신고당한 회원의 신고당한 횟수, 구분, 이름, 회사명, 부서명, IP, 신고 카테고리, 신고
     * 사유)
     * 
     * @return
     */
    public List<ReportedCustomerWithInfoDTO> selectReportedCustomer() {
        // 관리자가 처리하지 않은 데이터(최신 신고날짜 순으로) 가져오기
        List<ReportCustomerEntity> reportCustomerEntities = reportCustomerRepository.findByManagerCheckOrderByReportDateDesc(YesOrNo.N);
        List<ReportedCustomerWithInfoDTO> result = new ArrayList<>();

        reportCustomerEntities.forEach((entity) -> {
            CustomerEntity customerEntity = customerRepository.findById(entity.getReported_id()).get();
            result.add(new ReportedCustomerWithInfoDTO(entity.getReported_id(), entity.getReportCustomerId(),
                    customerEntity.getReportedCnt(), customerEntity.getCustomerGubun(),
                    customerEntity.getCustomerName(), customerEntity.getCompName(),
                    customerEntity.getCustomerDepartment(), customerEntity.getRemoteIp(),
                    entity.getReportCategory(), entity.getReportReason()));
        });

        return result;
    }

    /**
     * 신고회원 관리자 처리 여부 변경 (N->Y)
     */
    @Transactional
    public void reportCustomerUpdateManagerCheck(Long reportCutomerId) {
        ReportCustomerEntity entity = reportCustomerRepository.findById(reportCutomerId).get();
        entity.setManagerCheck(YesOrNo.Y);
    }

    /**
     * 신고 회원을 블랙회원으로 변경하는 함수 <br>
     * 신고회원 테이블 pk와 신고당한 회원 아이디를 입력받은 후,<br>
     * 1. 신고당한 아이디의 blacklist_check 값 변경 <br>
     * 2. 회원테이블의 회원id, compName, remoteIp, country, 신고회원 테이블의 신고카테고리, 신고 사유로 블랙리스트
     * 엔티티생성해서 테이블에 저장
     * 
     * @param reportCustomerId
     * @param reportedId
     */
    @Transactional
    public void reportedIdToBlackList(Long reportCutomerId, String reportedId) {

        // customer의 blacklistCheck값 Y로 변경
        CustomerEntity customerEntity = customerRepository.findById(reportedId).get();
        customerEntity.setBlacklistCheck(YesOrNo.Y);

        // 블랙리스트DTO 생성
        ReportCustomerEntity reportCustomerEntity = reportCustomerRepository.findById(reportCutomerId).get();

        BlacklistDTO dto = new BlacklistDTO(reportedId, customerEntity.getCompName(), customerEntity.getRemoteIp(),
                customerEntity.getCountry(), reportCustomerEntity.getReportCategory(),
                reportCustomerEntity.getReportReason());

        // 블랙리스트 DB에 저장
        BlacklistEntity entity = blacklistRepository.save(BlacklistEntity.toEntity(dto));

    }

    /**
     *  블랙리스트 테이블에서 전달받은 ID를 삭제하는 함수 
     * @param blacklistId
     */
    @Transactional
    public void deleteFromBalcklist(Long blacklistId) {
        blacklistRepository.deleteById(blacklistId);
    }


    /**
     * 블랙리스트 회원 DTO를 반환하고자 하는데 회원의 기본적인 정보를 같이 담기 위해 새로운 DTO에 담아 리스트를 반환하는 함수 
     * (신고당한 회원 ID, 신고회원테이블ID, 신고당한 회원의 신고당한 횟수, 구분, 이름, 회사명, 부서명, IP, 신고 카테고리, 신고 사유)
     * @return
     */
    public List<ReportedCustomerWithInfoDTO> selectblackList() {
        // 모든 블랙리스트 데이터 가져오기
        List<BlacklistEntity> blacklistEntityList = blacklistRepository.findAll();
        List<ReportedCustomerWithInfoDTO> result = new ArrayList<>();

        blacklistEntityList.forEach((entity) -> {
            // 회원 정보 가져오기 위한 CustomerEntity 가져오기
            CustomerEntity customerEntity = customerRepository.findById(entity.getCustomerId()).get();

            result.add(new ReportedCustomerWithInfoDTO(entity.getCustomerId(), entity.getBlacklistId(),
                    customerEntity.getReportedCnt(), customerEntity.getCustomerGubun(),
                    customerEntity.getCustomerName(), customerEntity.getCompName(),
                    customerEntity.getCustomerDepartment(), customerEntity.getRemoteIp(),
                    entity.getBlackType(), entity.getBlackReason()));
        });

        return result;
    }

    
}
