<<<<<<< HEAD
package net.kdigital.ec21.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	@GetMapping({"","/"})
	public String index() {
		return ("index");
	}
}
=======
package net.kdigital.ec21.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	@GetMapping({"","/"})
	public String index() {
		return "index";
	}
}
>>>>>>> 1a1ecd553fc320897f03b38db9609eb13cd29bc3
