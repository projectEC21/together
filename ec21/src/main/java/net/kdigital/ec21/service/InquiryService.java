package net.kdigital.ec21.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.kdigital.ec21.dto.InquiryDTO;
import net.kdigital.ec21.dto.InquiryModalDTO;
import net.kdigital.ec21.dto.check.InquiryEnum;
import net.kdigital.ec21.entity.InquiryEntity;
import net.kdigital.ec21.entity.ProductEntity;
import net.kdigital.ec21.repository.InquiryBlockRepository;
import net.kdigital.ec21.repository.InquiryRepository;
import net.kdigital.ec21.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class InquiryService {
    private final InquiryRepository inquiryRepository;
    private final InquiryBlockRepository inquiryBlockRepository;
    private final ProductRepository productRepository;
    
    // ============================= 인콰이어리 등록 ==============================



    
    //===================================== Received Page ===============================
    
    /**
     * receiverId가 customerId와 일치하는 모든 인콰이어리들을 리스트로 반환하는 함수
     * (조건 : span==N, trash ==N, senderId != blackId)
     * @param customerId
     * @return
     */
    public List<InquiryDTO> getReceivedInquiry(String customerId) {
        List<InquiryEntity> inquiryEntities = inquiryRepository.findNonBlockedInquiries(customerId);
        List<InquiryDTO> dtos = new ArrayList<>();

        inquiryEntities.forEach((entity)->{
            dtos.add(InquiryDTO.toDTO(entity,entity.getCustomerEntity().getCustomerId(), entity.getProductEntity().getProductId()));
        });
        
        return dtos;
    }


    /**
     * inquiryId에 해당하는 인콰이어리 정보들과 productId에 해당하는 상품 정보들 중
     * 인콰이어리 모달창에 필요한 데이터들로 새로운 DTO를 만들어 반환하는 함수
     * @param inquiryId
     * @return
     */
	public InquiryModalDTO getInquiryModalDTO(String inquiryId) {
        Long id = Long.parseLong(inquiryId);
        // 인콰이어리 정보 가져오기
        InquiryEntity inquiryEntity = inquiryRepository.findById(id).get();
        // 상품 정보 가져오기
        ProductEntity productEntity = productRepository.findById(inquiryEntity.getProductEntity().getProductId()).get();
        // 새로운 인콰이어리 모달 DTO 생성
        InquiryModalDTO dto = new InquiryModalDTO(id, inquiryEntity.getCustomerEntity().getCustomerId(), 
                                    inquiryEntity.getReceiverId(), productEntity.getProductName(), 
                                    productEntity.getCategory(), inquiryEntity.getQuantity(), 
                                    productEntity.getUnit(), inquiryEntity.getInquiryTitle(), 
                                    inquiryEntity.getInquiryContent(), inquiryEntity.getOriginalFileName(), 
                                    inquiryEntity.getSavedFileName());
        return dto;
	}


    /**
     * 전달받은 인콰이어리ID에 해당하는 인콰이어리DTO 반환하는 함수
     * @param id
     * @return
     */
    public InquiryDTO getInquiry(String inquiryId) {
        Long id = Long.parseLong(inquiryId);
        InquiryEntity entity = inquiryRepository.findById(id).get();
        return InquiryDTO.toDTO(entity, entity.getCustomerEntity().getCustomerId(), entity.getProductEntity().getProductId());
    }


    /************************
     * Receiver의 saved 관련
    *************************/

    /**
     * 저장 X : 전달받은 inquiryId에 해당하는 인콰이어리의 receiver의 saved 값을 NN혹은 YN으로 변경
     * @param inquiryId
     * @return
     */
    @Transactional
    public Boolean receiverSavedNo(String inquiryId) {
        Long id = Long.parseLong(inquiryId);
        Optional<InquiryEntity> inquiryEntity = inquiryRepository.findById(id);

        if (!inquiryEntity.isPresent()) {
            return false;
        }
        InquiryEntity entity = inquiryEntity.get();

        // saved 상태 변경
        if (entity.getSaved() == InquiryEnum.NY) {
            entity.setSaved(InquiryEnum.NN);
        } else if (entity.getSaved() == InquiryEnum.YY) {
            entity.setSaved(InquiryEnum.YN);
        }

        return true;
    }

    /**
     * 저장 O : 전달받은 inquiryId에 해당하는 인콰이어리의 receiver의 saved 값을 NY혹은 YY로 변경
     * @param inquiryId
     * @return
     */
    @Transactional
    public Boolean receiverSavedYes(String inquiryId) {
        Long id = Long.parseLong(inquiryId);
        Optional<InquiryEntity> inquiryEntity = inquiryRepository.findById(id);

        if (!inquiryEntity.isPresent()) {
            return false;
        }
        InquiryEntity entity = inquiryEntity.get();

        // saved 상태 변경
        if (entity.getSaved() == InquiryEnum.NN) {
            entity.setSaved(InquiryEnum.NY);
        } else if(entity.getSaved() == InquiryEnum.YN){
            entity.setSaved(InquiryEnum.YY);
        }

        return true;
    }
    
    /************************
     * Receiver의 spam 관련
     *************************/
    
    /**
     * 입력받은 인콰이어리 ID를 Long형으로 변경한 후 spam 값을 N->Y로 변경하는 함수
     * @param inquiryId
     * @return
     */
    @Transactional
    public Boolean receiverToSpam(String inquiryId) {
        Long id = Long.parseLong(inquiryId);
        Optional <InquiryEntity> inquiryEntity = inquiryRepository.findById(id);

        if (!inquiryEntity.isPresent()) {
            return false;
        }
        InquiryEntity entity = inquiryEntity.get(); 
        if (entity.getSpam()==InquiryEnum.NN) {
            entity.setSpam(InquiryEnum.NY);
        }else{
            entity.setSpam(InquiryEnum.YY);
        }
        return true;
    }

    /************************
     * Receiver의 trash 관련
     *************************/

    /**
     * 입력받은 인콰이어리 ID를 Long형으로 변경한 후 trash 값을 N->Y로 변경하는 함수
     * 
     * @param inquiryId
     * @return
     */
    @Transactional
    public Boolean receiverToTrash(String inquiryId) {
        Long id = Long.parseLong(inquiryId);
        Optional<InquiryEntity> inquiryEntity = inquiryRepository.findById(id);

        if (!inquiryEntity.isPresent()) {
            return false;
        }
        InquiryEntity entity = inquiryEntity.get();
        if (entity.getTrash() == InquiryEnum.NN) {
            entity.setTrash(InquiryEnum.NY);
        } else {
            entity.setTrash(InquiryEnum.YY);
        }
        return true;
    }

    // ================================= Sent Page ===================================

    /**
     * senderId가 전달받은 customerId와 일치하는 인콰이어리 반환 (trash==NY or trash==NN)
     * @param customerId
     * @return
     */
    public List<InquiryDTO> getSentInquiry(String customerId) {
        List<InquiryEntity> inquiryEntities = inquiryRepository.findInquiriesByCustomerIdAndTrashStatus(customerId);
        List<InquiryDTO> dtos = new ArrayList<>();

        inquiryEntities.forEach((entity)->{
            dtos.add(InquiryDTO.toDTO(entity, entity.getCustomerEntity().getCustomerId(), entity.getProductEntity().getProductId()));
        });
        
        return dtos;
    }

    /************************
     * Sender의 saved 관련
     *************************/

    /**
     * 전달받은 인콰이어리ID에 해당하는 인콰이어리의 sender의 saved 값을 N으로 변경하는 함수
     * @param inquiryId
     * @return
     */
    @Transactional
    public Boolean senderSavedNo(String inquiryId) {
        Long id = Long.parseLong(inquiryId);
        Optional<InquiryEntity> inquiryEntity = inquiryRepository.findById(id);

        if (!inquiryEntity.isPresent()) {
            return false;
        }
        InquiryEntity entity = inquiryEntity.get();

        // saved 상태 변경
        if (entity.getSaved() == InquiryEnum.YN) {
            entity.setSaved(InquiryEnum.NN);
        } else if (entity.getSaved() == InquiryEnum.YY) {
            entity.setSaved(InquiryEnum.NY);
        }

        return true;
    }

    /**
     * 전달받은 인콰이어리ID에 해당하는 인콰이어리의 sender의 saved 값을 N으로 변경하는 함수
     * @param inquiryId
     * @return
     */
    @Transactional
    public Boolean senderSavedYes(String inquiryId) {
        Long id = Long.parseLong(inquiryId);
        Optional<InquiryEntity> inquiryEntity = inquiryRepository.findById(id);

        if (!inquiryEntity.isPresent()) {
            return false;
        }
        InquiryEntity entity = inquiryEntity.get();

        // saved 상태 변경
        if (entity.getSaved() == InquiryEnum.NN) {
            entity.setSaved(InquiryEnum.YN);
        } else if (entity.getSaved() == InquiryEnum.NY) {
            entity.setSaved(InquiryEnum.YY);
        }

        return true;
    }


    /************************
     * Sender의 trash 관련
     *************************/
    
    /**
      * 전달받은 인콰이어리Id를 Long형으로 바꾸고 sender의 trash값 Y로 변환하는 함수
      * @param inquiryId
      * @return
      */
    @Transactional
    public Boolean senderToTrash(String inquiryId) {
        Long id = Long.parseLong(inquiryId);
        Optional<InquiryEntity> inquiryEntity = inquiryRepository.findById(id);

        if (!inquiryEntity.isPresent()) {
            return false;
        }
        InquiryEntity entity = inquiryEntity.get();
        if (entity.getTrash() == InquiryEnum.NN) {
            entity.setTrash(InquiryEnum.YN);
        } else if(entity.getTrash() == InquiryEnum.NY){
            entity.setTrash(InquiryEnum.YY);
        }
        return true;
    }

    //=========================== Saved Page ==========================


    /**
     * 인콰이어리의 receiverId 또는 senderId와 전달받은 회원ID가 일치하는 
     * 모든 인콰이어리들 중에 saved값이 Y인 인콰이어리들 반환하는 함수  
     * @param customerId
     * @return
     */
    public List<InquiryDTO> getSavedInquiry(String customerId) {
        // 두 쿼리 결과를 각각 조회
        List<InquiryEntity> inquiriesByReceiver = inquiryRepository.findValidInquiries(customerId);
        List<InquiryEntity> inquiriesBySender = inquiryRepository.findInquiriesBySender(customerId);

        // 두 리스트를 스트림으로 합치고 sendDate로 내림차순 정렬
        List<InquiryEntity> resultEntities = Stream.concat(inquiriesByReceiver.stream(), inquiriesBySender.stream())
                .sorted((i1, i2) -> i2.getSendDate().compareTo(i1.getSendDate()))
                .collect(Collectors.toList());

        List<InquiryDTO> resultDtos = new ArrayList<>();
        resultEntities.forEach((entity)->{
            resultDtos.add(InquiryDTO.toDTO(entity, entity.getCustomerEntity().getCustomerId(),entity.getProductEntity().getProductId()));
        });
        
        return resultDtos;
    }
    



}
