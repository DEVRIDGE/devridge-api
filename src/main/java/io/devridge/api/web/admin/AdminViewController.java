package io.devridge.api.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class AdminViewController {

    @GetMapping
    public String adminMain() {
        return "main";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
