package net.kdigital.ec21.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.kdigital.ec21.dto.InquiryDTO;
import net.kdigital.ec21.entity.InquiryEntity;

public interface InquiryRepository extends JpaRepository<InquiryEntity, Long>{
    
    // receivedId와 전달받은 customerId가 일치하는 모든 인콰이어리 데이터 조회
    List<InquiryEntity> findByReceiverId(String receiverId);


    // spam과 trash의 값이 NN이거나 YN이고 receiver가 신고한 회원ID에 senderId가 포함되지 않는 데이터만 조회
    @Query("SELECT i FROM InquiryEntity i WHERE i.receiverId = :customerId AND " +
            "(i.spam = 'NN' OR i.spam = 'YN') AND " +
            "(i.trash = 'NN' OR i.trash = 'YN') AND " +
            "NOT EXISTS (SELECT b FROM InquiryBlockEntity b WHERE b.customerEntity.customerId = i.receiverId AND b.blockedId = i.customerEntity.customerId)")
    List<InquiryEntity> findNonBlockedInquiries(String customerId);

    
}
