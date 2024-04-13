package net.kdigital.ec21.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kdigital.ec21.dto.CustomerDTO;
import net.kdigital.ec21.dto.ProductDTO;
import net.kdigital.ec21.dto.ReportedCustomerWithInfoDTO;
import net.kdigital.ec21.service.ManagerService;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ManagerController {
	private final ManagerService managerService;

	// ============================= 메인보드 =============================
	/**
	 * 관리자 메인보드 화면 요청
	 * 
	 * @return
	 */
	@GetMapping("manager/manager_index")
	public String manager_index() {

		return "manager/manager_index";
	}

	/**
	 * ajax - 당일 등록 상품 수, 당일 이상상품 등록 수, 당일 회원가입 수, 미처리된 신고 개수 반환
	 * 
	 * @return
	 */
	@GetMapping("manager/count")
	@ResponseBody
	public Map<String, Long> count() {
		Map<String, Long> result = managerService.getCount();
		return result;
	}

	// ============================= 상품 관리 =============================

	/**
	 * 전체 상품 리스트 화면 요청
	 * 
	 * @return
	 */
	@GetMapping("manager/productList")
	public String productList(Model model) {
		List<ProductDTO> dtoList = managerService.selectAll();
		model.addAttribute("list", dtoList);
		return "manager/productList";
	}

	/**
	 * 모델로 이상상품 판별된 상품 리스트 화면 요청
	 * 
	 * @return
	 */
	@GetMapping("manager/modelPredict")
	public String modelPredict() {
		managerService.selectWeird();
		return "manager/modelPredict";
	}

	// ============================= 회원 관리 =============================

	/**
	 * 정상회원 리스트 화면 요청
	 * 
	 * @return
	 */
	@GetMapping("manager/customerList")
	public String customerList(Model model) {
		List<CustomerDTO> dtoList = managerService.selectNotBlacklist();
		model.addAttribute("list", dtoList);
		return "manager/customerList";
	}

	/**
	 * 신고당한 회원 리스트 화면 요청
	 * 
	 * @return
	 */
	@GetMapping("manager/reportedCustomerList")
	public String reportedCustomerList(Model model) {
		List<ReportedCustomerWithInfoDTO> dtoList = managerService.selectReportedCustomer();
		model.addAttribute("list", dtoList);
		return "manager/reportedCustomerList";
	}

	/**
	 * 블랙리스트 회원 리스트 화면 요청
	 * 
	 * @return
	 */
	@GetMapping("manager/blackList")
	public String blackList() {
		return "manager/blackList";
	}

}
