<<<<<<< HEAD
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
>>>>>>> 15fb48aec3b88996044c30b04e07a2761ed1910d
