package net.kdigital.ec21.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.kdigital.ec21.dto.CustomerDTO;
import net.kdigital.ec21.dto.ProductDTO;
import net.kdigital.ec21.entity.CustomerEntity;
import net.kdigital.ec21.entity.ProductEntity;
import net.kdigital.ec21.repository.CustomerRepository;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    /**
     * 회원 ID를 전달받아 회원DTO를 반환하는 함수
     * @param customerId
     * @return
     */
    public CustomerDTO getCustomer(String customerId) {
        CustomerEntity customerEntity = customerRepository.findById(customerId).get();
        return CustomerDTO.toDTO(customerEntity);
    }

    /**
     * 회원 등록 - 전달 받은 회원DTO를 Entity로 변환하여 DB에 저장하는 함수
     * @param customerDTO
     */
    public void insertCustomer(CustomerDTO customerDTO) {
        CustomerEntity entity = CustomerEntity.toEntity(customerDTO);
        customerRepository.save(entity);
    }

    /**
     * 회원 정보 수정 - 전달받은 회원DTO의 내용으로 정보 수정
     * @param dto
     * @return
     */
    @Transactional
    public CustomerDTO updateCustomer(CustomerDTO dto) {
        CustomerEntity entity = customerRepository.findById(dto.getCustomerId()).get();

        // 바꿀수 없는 값 : ID
        entity.setCustomerPw(dto.getCustomerPw());
        entity.setCustomerName(dto.getCustomerName());
        entity.setEmail(dto.getEmail());
        entity.setCustomerDepartment(dto.getCustomerDepartment());
        entity.setCustomerGubun(dto.getCustomerGubun());
        entity.setCompName(dto.getCompName());
        entity.setCompDesc(dto.getCompDesc());
        entity.setCompUrl(dto.getCompUrl());
        entity.setAddress(dto.getAddress());
        entity.setZipNo(dto.getZipNo());
        entity.setFaxNo(dto.getFaxNo());
        entity.setBusiNo(dto.getBusiNo());
        entity.setTradeNo(dto.getTradeNo());

        return CustomerDTO.toDTO(entity);
    }

    
    
}
