package kr.co.company.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("member")
@Controller
public class MemberController {
	
	@GetMapping("login")
	@ResponseBody
	public String login() {
		return "Hello World!";
	};

}
