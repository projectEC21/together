package net.kdigital.ec21.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.kdigital.ec21.dto.check.YesOrNo;
import net.kdigital.ec21.entity.ReportCustomerEntity;
import java.util.List;

public interface ReportCustomerRepository extends JpaRepository<ReportCustomerEntity, Long> {
    // 미처리된 신고 개수 반환
    long countByManagerCheck(YesOrNo yesOrNo);

    // 미처리된 신고 정보 리스트 최근 신고된 날짜 순으로 반환
    List<ReportCustomerEntity> findByManagerCheckOrderByReportDateDesc(YesOrNo managerCheck);

}
