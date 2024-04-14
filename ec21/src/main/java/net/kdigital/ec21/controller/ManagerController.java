package net.kdigital.ec21.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kdigital.ec21.dto.CustomerDTO;
import net.kdigital.ec21.dto.ProductDTO;
import net.kdigital.ec21.dto.ReportedCustomerWithInfoDTO;
import net.kdigital.ec21.service.ManagerService;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

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
		return "manager/reportedCustomerList";
	}

	/**
	 * ajax - 신고당한 회원 리스트 반환 (미처리/처리(블랙버튼/정상버튼)에 따라 다르게 처리)
	 * 
	 * @param reportCustomerId
	 * @param reportedId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/manager/reportedCustomerList/getList", method = RequestMethod.GET)
	public String getList(@RequestParam(name = "reportCustomerId") Long reportCustomerId,
			@RequestParam(name = "reportedId") String reportedId,
			Model model) {
		if (reportCustomerId != -100) {
			// 블랙버튼 : 신고당한 회원 블랙리스트에 추가
			if (reportedId != "") {
				managerService.reportedIdToBlackList(reportCustomerId, reportedId);
			}
			// 정상버튼 : 관리자 처리 완료 상태로 변경
			managerService.reportCustomerUpdateManagerCheck(reportCustomerId);
		}

		List<ReportedCustomerWithInfoDTO> dtoList = managerService.selectReportedCustomer();
		model.addAttribute("list", dtoList);

		return "/manager/reportedCustomerList::#result";
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

	/**
	 * ajax - 블랙리스트 반환 (정상버튼 클릭시는 다르게 처리)
	 * 
	 * @param reportCustomerId
	 * @param reportedId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/manager/blackList/getList", method = RequestMethod.GET)
	public String getList(@RequestParam(name = "blacklistId") Long blacklistId, Model model) {
		if (blacklistId != -100) {
			// 정상버튼 : 블랙리스트 테이블에서 해당 블랙리스트ID(일련번호) 삭제
			managerService.deleteFromBalcklist(blacklistId);
		}
		List<ReportedCustomerWithInfoDTO> dtoList = managerService.selectblackList();
		model.addAttribute("list", dtoList);

		return "/manager/blackList::#result";
	}

}
