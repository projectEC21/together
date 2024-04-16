<<<<<<< HEAD
package net.kdigital.ec21.controller;

import java.util.List;

import net.kdigital.ec21.dto.ProductDTO;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kdigital.ec21.service.MainPageServiceDy;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {
	private final MainPageServiceDy mainPageService;

	//================= main/index.html =====================
	/**
	 * 첫 화면 및 메인 페이지 화면 요청
	 * @return
	 */
	@GetMapping({ "", "/" })
	public String index(Model model) {
		// hitCount기준 DESC, createDate 기준 DESC 순으로 상위 8개를 가져옴
		// (judge=='Y' && customerId에 해당하는 Customer의 blacklistCheck=='N'인 데이터들 중에서) 
		List<ProductDTO> dtoList = mainPageService.getTopProductList();
		model.addAttribute("list", dtoList);
		return "main/index";
	}

}

=======
package net.kdigital.ec21.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	// @GetMapping({ "", "/" })
	// public String index() {
	// return "manager/manager_index";
	// }
	@GetMapping({ "", "/" })
	public String index() {
		return "main/index";
	}
}
>>>>>>> 358ed0d93fad9ebcedd617ce8d6dc87f810d1a0f
