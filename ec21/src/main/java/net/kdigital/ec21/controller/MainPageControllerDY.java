package net.kdigital.ec21.controller;

import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import net.kdigital.ec21.service.MainPageServiceDy;

@Controller
@RequiredArgsConstructor
public class MainPageControllerDY {
    private final MainPageServiceDy mainService;

    //================= main/index.html =====================
    
    // hitCount기준 DESC, createDate 기준 DESC 순으로 8개를 가져옴
    // (lstmPredict==1 && judge=='N' 인 데이터들 중에서) 
    

    
}
