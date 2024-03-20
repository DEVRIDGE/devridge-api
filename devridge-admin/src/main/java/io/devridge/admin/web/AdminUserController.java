package io.devridge.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class AdminUserController {

    @GetMapping("/login")
    public String loginView() {
        return "login";
    }
}
