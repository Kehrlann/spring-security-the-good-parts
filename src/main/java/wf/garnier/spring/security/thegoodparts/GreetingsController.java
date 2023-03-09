package wf.garnier.spring.security.thegoodparts;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class GreetingsController {
	@GetMapping("/")
	public String publicPage() {
		return "public";
	}

	@GetMapping("/private")
	public String privatePage(Model model) {
		model.addAttribute("name", getName());
		return "private";
	}

	private static String getName() {
		return "UNKNOWN USER";
	}

}
