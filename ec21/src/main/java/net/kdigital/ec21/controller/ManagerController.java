package net.kdigital.ec21.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class ManagerController {
	//======================== manager_index.html ==========================
	/**
	 * 관리자 메인보드 화면 요청
	 * @return
	 */
	@GetMapping("/manager/manager_index")
	public String manager_index() {
		return "/manager/manager_index";
	}
	/**
	 * 전체 상품 리스트 화면 요청
	 * @return
	 */
	@GetMapping("/manager/productList")
	public String productList() {
		return "/manager/productList";
	}
	
	/**
	 * 모델로 이상상품 판별된 상품 리스트 화면 요청
	 * @return
	 */
	@GetMapping("/manager/modelPredict")
	public String modelPredict() {
		return "/manager/modelPredict";
	}

	
	/**
	 * 정상회원 리스트 화면 요청
	 * @return
	 */
	@GetMapping("/manager/customerList")
	public String customerList() {
		return "/manager/customerList";
	}

	/**
	 * 신고당한 회원 리스트 화면 요청
	 * @return
	 */
	@GetMapping("/manager/reportedCustomerList")
	public String reportedCustomerList() {
		return "/manager/reportedCustomerList";
	}

	/**
	 * 블랙리스트 회원 리스트 화면 요청 
	 * @return
	 */
	@GetMapping("/manager/blackList")
	public String blackList() {
		return "/manager/blackList";
	}

	/**
	 * 블랙리스트 회원 리스트 화면 요청 
	 * @return
	 */
	@GetMapping("/main/login")
	public String login() {
		return "/main/login";
	}





	//=================================================================================
	



	//============================= modelPredict.html ====================================================
	
	




	@GetMapping("/tables")
	public String tables() {
		return "tables";
	}
	@GetMapping("/projects")
	public String projects() {
		return "projects";
	}
	@GetMapping("/alltables")
	public String alltables() {
		return "alltables";
	}
	@GetMapping("/contentables")
	public String contentables() {
		return "contentables";
	}
	
	@GetMapping("/blacktables")
	public String blacktables() {
		return "blacktables";
	}


}
