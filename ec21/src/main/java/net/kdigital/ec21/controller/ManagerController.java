package net.kdigital.ec21.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class ManagerController {
	//======================== manager_index.html ==========================
	/**
	 * 관리자 메인보드 화면 요청
	 * @return
	 */
	@GetMapping("/manager/manager_index")
	public String manager_index() {
		return "manager/manager_index.html";
	}
	/**
	 * 전체 상품 리스트 화면 요청
	 * @return
	 */
	@GetMapping("/manager/productList")
	public String productList() {
		return "/manager/productList.html";
	}
	
	/**
	 * 모델로 이상상품 판별된 상품 리스트 화면 요청
	 * @return
	 */
	@GetMapping("/manager/modelPredict")
	public String modelPredict() {
		return "manager/modelPredict.html";
	}

	
	/**
	 * 정상회원 리스트 화면 요청
	 * @return
	 */
	@GetMapping("/manager/customerList")
	public String customerList() {
		return "manager/customerList.html";
	}

	/**
	 * 신고당한 회원 리스트 화면 요청
	 * @return
	 */
	@GetMapping("/manager/reportedCustomerList")
	public String reportedCustomerList() {
		return "manager/reportedCustomerList.html";
	}

	/**
	 * 블랙리스트 회원 리스트 화면 요청 
	 * @return
	 */
	@GetMapping("/manager/blackList")
	public String blackList() {
		return "manager/blackList.html";
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
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}

}