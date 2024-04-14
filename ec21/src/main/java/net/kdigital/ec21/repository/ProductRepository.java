package net.kdigital.ec21.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.kdigital.ec21.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {
        // 당일 등록 상품 개수
        Long countByCreateDateBetween(LocalDateTime startDate, LocalDateTime endDate);

        // 당일 등록된 이상 상품 개수
        Long countByCreateDateBetweenAndLstmPredict(LocalDateTime startDate, LocalDateTime endDate,
                        boolean lstmPredict);

        // lstmPredict 값이 이상 즉 false(0)인 모든 ProductEntity 리스트로 반환
        List<ProductEntity> findByLstmPredict(boolean lstmPredict);

        // lstmPredict 값이 이상 즉 false(0)이고 judge가 null(관리자 미처리)인 모든 ProductEntity 리스트 반환 (최신 상품 등록일 순으로)
        List<ProductEntity> findByLstmPredictAndJudgeOrderByCreateDateDesc(boolean lstmPredict, String judgeIsNull);
}
