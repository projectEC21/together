package net.kdigital.ec21.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import net.kdigital.ec21.entity.CustomerEntity;
import java.util.List;
import net.kdigital.ec21.dto.check.YesOrNo;

public interface CustomerRepository extends JpaRepository<CustomerEntity, String> {
    // 당일 등록한 회원 수 반환
    long countByCreateDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // 정상 회원 (blacklist_check==N)인 모든 customerEntity 최신 등록 순으로 리스트 반환
    List<CustomerEntity> findByBlacklistCheckOrderByCreateDateDesc(YesOrNo blacklistCheck);
}
