package net.kdigital.ec21.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.kdigital.ec21.dto.ProductDTO;
import net.kdigital.ec21.entity.ProductEntity;
import net.kdigital.ec21.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class MainPageServiceDy {
    private final ProductRepository productRepository;

    public List<ProductDTO> getTopProductList() {
        List<ProductEntity> entityList = new ArrayList<>();
        List<ProductDTO> dtoList = new ArrayList<>();

        return dtoList;
    }
}
