package net.kdigital.ec21.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.kdigital.ec21.entity.InquiryEntity;

public interface InquiryRepository extends JpaRepository<InquiryEntity, Long>{
    
}
