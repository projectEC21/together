package net.kdigital.ec21.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.kdigital.ec21.dto.check.ProductCategory;
import net.kdigital.ec21.dto.check.YesOrNo;
import net.kdigital.ec21.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {
        // ================== 메인보드 : count 관련 ========================
        // 당일 등록 상품 개수
        Long countByCreateDateBetween(LocalDateTime startDate, LocalDateTime endDate);
        
        // 당일 등록된 이상 상품 개수
        Long countByCreateDateBetweenAndLstmPredict(LocalDateTime startDate, LocalDateTime endDate, boolean lstmPredict);
        
        // ============================ 관리자 전체 상품 화면 =================================
        
        // 회원ID, 상품ID, 상품명 (3개지 필드 비교)에 전달받은 검색어가 포함된 상품들을 정렬 조건순으로 반환
        @Query("SELECT p FROM ProductEntity p WHERE " +
        "LOWER(p.customerEntity.customerId) LIKE %:searchWord% OR " +  
        "LOWER(p.productId) LIKE %:searchWord% OR " +                 
        "LOWER(p.productName) LIKE %:searchWord%")                    
        List<ProductEntity> findByMultipleFieldsContaining(@Param("searchWord") String searchWord, Sort sort);

        // 전달받은 카테고리에 해당하는 상품 중 회원ID, 상품ID, 상품명에 전달받은 검색어가 포함된 상품들을 정렬 조건순으로 반환
        @Query("SELECT p FROM ProductEntity p WHERE " +
        "p.category = :category AND " +  // 카테고리 조건 추가
        "(LOWER(p.customerEntity.customerId) LIKE %:searchWord% OR " +
        "LOWER(p.productId) LIKE %:searchWord% OR " +
        "LOWER(p.productName) LIKE %:searchWord%)")
        List<ProductEntity> findByCategoryAndMultipleFieldsContaining(
                @Param("category") ProductCategory category,   // 카테고리 파라미터 추가
                @Param("searchWord") String searchWord, 
                Sort sort);
                

        // ============================ 모델로 이상 상품 판별 화면 =================================
        // lstmPredict 값이 이상, 즉 false(0)인 모든 ProductEntity 리스트로 반환
        List<ProductEntity> findByLstmPredict(boolean lstmPredict);
                
        // lstmPredict 값이 이상, 즉 false(0)이고 judge가 null(관리자 미처리)인 모든 ProductEntity 리스트 반환 (검색어 X, 최신 상품 등록일 순으로)
        List<ProductEntity> findByLstmPredictAndJudgeOrderByCreateDateDesc(boolean lstmPredict, String judgeIsNull);
        
        // lstmPredict 값이 이상, 즉 false(0)이고 judge가 null(관리자 미처리)인 모든 ProductEntity들 중에
        // 전달 받은 검색어가 회원ID, 상품ID, 상품명에 포함된 상품들의 리스트를 최신 등록 순으로 반환 
        @Query("SELECT p FROM ProductEntity p WHERE " +
                        "(LOWER(p.customerEntity.customerId) LIKE %:searchWord% OR " +
                        "LOWER(p.productId) LIKE %:searchWord% OR " +
                        "LOWER(p.productName) LIKE %:searchWord%) AND " +
                        "p.lstmPredict = false AND " +
                        "p.judge IS NULL " +
                        "ORDER BY p.createDate DESC")
        List<ProductEntity> findByMultipleFieldsContainingOrderByCreateDateDesc(@Param("searchWord") String searchWord);

        // lstmPredict 값이 이상, 즉 false(0)이고 judge가 null(관리자 미처리)인 모든 ProductEntity들 중에
        // 전달 받은 카테고리에 해당하는 상품들 중에 전달 받은 검색어가 회원ID, 상품ID, 상품명에 포함된 상품들의 리스트를 최신 등록 순으로 반환 
        @Query("SELECT p FROM ProductEntity p WHERE " +
                        "p.category = :category AND " +
                        "(LOWER(p.customerEntity.customerId) LIKE %:searchWord% OR " +
                        "LOWER(p.productId) LIKE %:searchWord% OR " +
                        "LOWER(p.productName) LIKE %:searchWord%) AND " +
                        "p.lstmPredict = false AND " +
                        "p.judge IS NULL " +
                        "ORDER BY p.createDate DESC")
        List<ProductEntity> findByCategoryAndMultipleFieldsContainingOrderByCreateDateDesc(
                        @Param("category") ProductCategory category, @Param("searchWord") String searchWord);

        // ============================== 메인페이지 ==================================

        // 상위(조회수, lstmPredictProba, 등록일) 8개 상품 데이터 조회를 위해 Pageable 사용 ( pageable :정렬 조건과 조회 개수 조건 넘김)
        @Query("SELECT p FROM ProductEntity p JOIN p.customerEntity c " +
                "WHERE p.judge = :judge AND c.blacklistCheck = :blacklistCheck AND p.productDelete = 'N'")
        Page<ProductEntity> findTopProductsByJudgeAndBlacklistCheckAndNotDeleted(
                @Param("judge") YesOrNo judge,
                @Param("blacklistCheck") YesOrNo blacklistCheck,
                Pageable pageable);

        // ============================ 메인 카테고리별 상품 목록 화면 =================================

        // 상품명에 전달받은 검색어가 포함된 상품들 중 
        // judge==Y & productDelete==N & customerEntity의 blacklistCheck == N 인 상품들을 
        // 최신 등록 순으로 반환
        @Query("SELECT p FROM ProductEntity p " +
        "WHERE LOWER(p.productName) LIKE %:searchWord% " +
        "AND p.judge = 'Y' " +
        "AND p.productDelete = 'N' " +
        "AND p.customerEntity.blacklistCheck = 'N' " +
        "ORDER BY p.createDate DESC")
        List<ProductEntity> findProductsByProductNameContainingAndAdditionalConditions( @Param("searchWord") String searchWord);

        // 위의 jpa에 카테고리 조건 추가 ver
        @Query("SELECT p FROM ProductEntity p " +
        "WHERE LOWER(p.productName) LIKE %:searchWord% " +
        "AND p.judge = 'Y' " +
        "AND p.productDelete = 'N' " +
        "AND p.customerEntity.blacklistCheck = 'N' " +
        "AND p.category = :category " +
        "ORDER BY p.createDate DESC")
        List<ProductEntity> findProductsByProductNameContainingAndAdditionalConditionsAndCategory(
                @Param("searchWord") String searchWord,
                @Param("category") ProductCategory category);


}
