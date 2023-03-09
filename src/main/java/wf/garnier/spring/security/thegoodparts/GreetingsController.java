package wf.garnier.spring.security.thegoodparts;

import org.springframework.security.core.Authentication;
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
	public String privatePage(Model model, Authentication authentication) {
		model.addAttribute("name", getName(authentication));
		return "private";
	}

	private static String getName(Authentication authentication) {
		return authentication.getName();
	}

}
