package net.kdigital.ec21.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.kdigital.ec21.entity.InquiryEntity;

public interface InquiryRepository extends JpaRepository<InquiryEntity, Long>{
    
    // receivedId와 전달받은 customerId가 일치하는 모든 인콰이어리 데이터 조회
    List<InquiryEntity> findByReceiverId(String receiverId);


    // Received : spam과 trash의 값이 NN이거나 YN이고 receiver가 신고한 회원ID에 senderId가 포함되지 않는 데이터만 조회
    @Query("SELECT i FROM InquiryEntity i WHERE i.receiverId = :customerId AND " +
            "(i.spam = 'NN' OR i.spam = 'YN') AND " +
            "(i.trash = 'NN' OR i.trash = 'YN') AND " +
            "NOT EXISTS (SELECT b FROM InquiryBlockEntity b WHERE b.customerEntity.customerId = i.receiverId AND b.blockedId = i.customerEntity.customerId)")
    List<InquiryEntity> findNonBlockedInquiries(String customerId);

    // Sent : senderId와 전달받은 customerId가 일치하는 데이터들 중 trash값이 NN이거나 NY인 데이터 조회
    @Query("SELECT i FROM InquiryEntity i WHERE i.customerEntity.id = :customerId AND i.trash IN ('NN', 'NY') ORDER BY i.sendDate DESC")
    List<InquiryEntity> findInquiriesByCustomerIdAndTrashStatus(String customerId);
    

    // 받은 인콰이어리 중 saved한 인콰이어리들 리스트로 반환
    @Query("SELECT i FROM InquiryEntity i " +
            "WHERE i.receiverId = :receiverId " +
            "AND i.saved IN ('NY', 'YY') " +
            "AND i.spam IN ('NN', 'YN') " +
            "AND i.trash IN ('NN', 'YN') " +
            "AND NOT EXISTS ( " +
            "  SELECT 1 FROM InquiryBlockEntity ib " +
            "  WHERE ib.customerEntity.customerId = i.customerEntity.customerId " +
            "  AND ib.blockedId = i.customerEntity.customerId" +
            ")")
    List<InquiryEntity> findValidInquiries(@Param("receiverId") String customerId);

    // 보낸 인콰이어리 중 saved한 인콰이어리들 리스트로 반환
    @Query("SELECT i FROM InquiryEntity i " +
            "WHERE i.customerEntity.customerId = :customerId " +
            "AND i.saved IN ('YN', 'YY') " +
            "AND i.spam IN ('NN', 'YN') " +
            "AND i.trash IN ('NN', 'YN')")
    List<InquiryEntity> findInquiriesBySender(@Param("customerId") String customerId);

}
