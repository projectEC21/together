package net.kdigital.ec21.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import net.kdigital.ec21.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {
        // 당일 등록 상품 개수
        Long countByCreateDateBetween(LocalDateTime startDate, LocalDateTime endDate);

        // 당일 등록된 이상 상품 개수
        Long countByCreateDateBetweenAndLstmPredict(LocalDateTime startDate, LocalDateTime endDate,
                        boolean lstmPredict);
}
