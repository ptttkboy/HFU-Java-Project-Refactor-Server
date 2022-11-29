package com.hfu.project_server.admin_mvc_controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * login跟about的mapping
 */
@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminBaseController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
