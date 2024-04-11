package net.kdigital.ec21.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	/**
	 * 메인 첫 화면
	 * @return
	 */
	
	@GetMapping({"","/"})
	public String index() {
		return "main/index";
	}
	
}
