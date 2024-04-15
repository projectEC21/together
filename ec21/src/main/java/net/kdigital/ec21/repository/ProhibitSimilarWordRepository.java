package net.kdigital.ec21.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.kdigital.ec21.entity.ProhibitSimilarWordEntity;

public interface ProhibitSimilarWordRepository extends JpaRepository<ProhibitSimilarWordEntity,Long>{

    // productId에 해당하는 데이터들을 이상단어 유사확률이 높은 순으로 리스트 반환
    List<ProhibitSimilarWordEntity> findProbaByProductEntity_ProductIdOrderBySimilarProbaDesc(String productId);
    

}
