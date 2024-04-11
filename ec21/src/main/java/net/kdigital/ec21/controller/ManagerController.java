package net.kdigital.ec21.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManagerController {
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
