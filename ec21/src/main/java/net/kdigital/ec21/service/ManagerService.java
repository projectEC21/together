package net.kdigital.ec21.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kdigital.ec21.dto.CustomerDTO;
import net.kdigital.ec21.dto.ProductDTO;
import net.kdigital.ec21.dto.ReportedCustomerWithInfoDTO;
import net.kdigital.ec21.dto.check.ReportCategory;
import net.kdigital.ec21.dto.check.YesOrNo;
import net.kdigital.ec21.entity.CustomerEntity;
import net.kdigital.ec21.entity.ProductEntity;
import net.kdigital.ec21.entity.ReportCustomerEntity;
import net.kdigital.ec21.repository.CustomerRepository;
import net.kdigital.ec21.repository.ProductRepository;
import net.kdigital.ec21.repository.ReportCustomerRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerService {
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final ReportCustomerRepository reportCustomerRepository;

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

    /**
     * 모든 상품 DTO 리스트로 반환하는 함수
     * 
     * @return
     */
    public List<ProductDTO> selectAll() {
        List<ProductEntity> entityList = productRepository.findAll();
        List<ProductDTO> dtoList = new ArrayList<>();
        entityList.forEach((entity) -> {
            dtoList.add(ProductDTO.toDTO(entity, entity.getCustomerEntity().getCustomerId()));
        });
        return dtoList;
    }

    /**
     * lstm_predict 값이 0인
     */
    public void selectWeird() {
    }

    /**
     * 정상회원(blacklist_check==N)인 CustomerDTO 리스트 반환
     */
    public List<CustomerDTO> selectNotBlacklist() {
        List<CustomerEntity> entityList = customerRepository.findByBlacklistCheck(YesOrNo.N);
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
        List<ReportCustomerEntity> reportCustomerEntities = reportCustomerRepository.findByManagerCheck(YesOrNo.N); // 관리자가
                                                                                                                    // 처리하지
                                                                                                                    // 않은
                                                                                                                    // 데이터
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

}
