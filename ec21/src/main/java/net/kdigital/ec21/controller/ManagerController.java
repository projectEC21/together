package net.kdigital.ec21.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kdigital.ec21.dto.CustomerDTO;
import net.kdigital.ec21.dto.CustomerListModalDTO;
import net.kdigital.ec21.dto.ModelPredictDTO;
import net.kdigital.ec21.dto.ModelPredictModalDTO;
import net.kdigital.ec21.dto.ProductDTO;
import net.kdigital.ec21.dto.ReportedCustomerWithInfoDTO;
import net.kdigital.ec21.service.ManagerService;
import net.kdigital.ec21.service.ProductService;

import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ManagerController {
	private final ManagerService managerService;
	private final ProductService productService;

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
	public String productList() {
		return "manager/productList";
	}

	/**
	 * ajax - 전체 상품 화면에서 상품 리스트 반환
	 * (카테고리, 검색어 조건에 따라 다르게 반환, 기본적으로 최신 등록일자 순으로 반환)
	 * (아직 버튼 처리 안함)
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/manager/productList/getList", method = RequestMethod.GET)
	public String getProductList(@RequestParam(name = "category", defaultValue = "total") String category,
			@RequestParam(name = "searchWord", defaultValue = "") String searchWord, Model model) {

		List<ProductDTO> dtoList = managerService.selectProductBySearch(category, searchWord);
		model.addAttribute("list", dtoList);

		return "/manager/productList::#result";
	}

	/**
	 * 모델로 이상상품 판별된 상품 리스트 화면 요청
	 * 
	 * @return
	 */
	@GetMapping("manager/modelPredict")
	public String modelPredict() {
		return "manager/modelPredict";
	}

	/**
	 * ajax - 모델이 이상으로 판별하고 관리자가 아직 처리하지 않은 상품 (lstm_predict==0 && judge==null) 중
	 * 전달 받은 카테고리와 검색어에 해당하는 상품을 최신 등록일 순으로 반환
	 * (아직 버튼 처리 안함)
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/manager/modelPredict/getList", method = RequestMethod.GET)
	public String getList(@RequestParam(name = "category", defaultValue = "total") String category,
			@RequestParam(name = "searchWord", defaultValue = "") String searchWord, Model model) {

		List<ModelPredictDTO> dtoList = managerService.selectModelPredictWeirdBySearch(category, searchWord);
		model.addAttribute("list", dtoList);

		return "/manager/modelPredict::#result";
	}

	/**
	 * 전달받은 상품 ID에 해당하는 금지어유사도 결과 리스트를 JSON 데이터로 반환
	 * @param productId
	 * @return
	 * @throws JsonProcessingException
	 */
	@ResponseBody
	@GetMapping("/manager/modelPredict/getProhibitSimilarWordDTOs")
	public String getProhibitSimilarWordDTOs(@RequestParam(name = "productId") String productId) throws JsonProcessingException {
		List<ModelPredictModalDTO>result = managerService.getProhibitSimilarWordDTOs(productId);
		if (result==null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(result);
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
	 * 전달받은 상품 ID에 해당하는 금지어유사도 결과 리스트를 JSON 데이터로 반환
	 * 
	 * @param productId
	 * @return
	 * @throws JsonProcessingException
	 */
	@ResponseBody
	@GetMapping("/manager/customerList/getCustomerProductDTOs")
	public String getCustomerProductDTOs(@RequestParam(name = "customerId") String customerId)
			throws JsonProcessingException {
		log.info(customerId);
		List<CustomerListModalDTO> result = managerService.getCustomerProductDTOs(customerId); 
		if (result == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(result);
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
	 * @param category
	 * @param searchWord
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/manager/reportedCustomerList/getList", method = RequestMethod.GET)
	public String getList(@RequestParam(name = "reportCustomerId", defaultValue = "-100") Long reportCustomerId,
			@RequestParam(name = "reportedId", defaultValue = "") String reportedId,
			@RequestParam(name = "category", defaultValue = "total") String category,
			@RequestParam(name = "searchWord", defaultValue = "") String searchWord,
			Model model) {
		if (reportCustomerId != -100) {
			// 블랙버튼 : 신고당한 회원 블랙리스트에 추가
			if (reportedId != "") {
				managerService.reportedIdToBlackList(reportCustomerId, reportedId);
			}
			// 정상버튼 : 관리자 처리 완료 상태로 변경
			managerService.reportCustomerUpdateManagerCheck(reportCustomerId);
		}

		List<ReportedCustomerWithInfoDTO> dtoList = managerService.selectReportedCustomerBySearch(category, searchWord);
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
	 * @param blacklistId
	 * @param category
	 * @param searchWord
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/manager/blackList/getList", method = RequestMethod.GET)
	public String getList(@RequestParam(name = "blacklistId", defaultValue = "-100") Long blacklistId,
			@RequestParam(name = "category", defaultValue = "total") String category,
			@RequestParam(name = "searchWord", defaultValue = "") String searchWord, Model model) {

		if (blacklistId != -100) {
			// 정상버튼 : 블랙리스트 테이블에서 해당 블랙리스트ID(일련번호) 삭제
			managerService.deleteFromBalcklist(blacklistId);
		}
		List<ReportedCustomerWithInfoDTO> dtoList = managerService.selectblackListBySearch(category, searchWord);
		model.addAttribute("list", dtoList);

		return "/manager/blackList::#result";
	}

}
