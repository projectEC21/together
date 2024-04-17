package net.kdigital.ec21.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.kdigital.ec21.dto.CustomerDTO;
import net.kdigital.ec21.entity.CustomerEntity;
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
    
}
