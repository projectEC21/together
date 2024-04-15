package net.kdigital.ec21.controller;

import java.util.List;

import net.kdigital.ec21.dto.ProductDTO;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import net.kdigital.ec21.service.MainPageServiceDy;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final MainPageServiceDy mainService;

	//================= main/index.html =====================
	/**
	 * 첫 화면 및 메인 페이지 화면 요청
	 * @return
	 */
	@GetMapping({ "", "/" })
	public String index(Model model) {
		List<ProductDTO> dtoList = mainService.getTopProductList();
		return "main/index";

	}

	private void getTopProductList() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getTopProductList'");
	}
	// hitCount기준 DESC, createDate 기준 DESC 순으로 8개를 가져옴
	// (lstmPredict==1 && judge=='N' 인 데이터들 중에서) 
	

}

