package depromeet.onepiece.common.auth.presentation;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Profile("dev")
@Controller
@RequestMapping("/login")
public class LoginRedirectController {
  @GetMapping
  public String login() {
    return "redirect:http://localhost:3000/login"; // dev
  }
}
