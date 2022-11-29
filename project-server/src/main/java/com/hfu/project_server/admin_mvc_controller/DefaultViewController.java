package com.hfu.project_server.admin_mvc_controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 將root為 "/" 或是 "/admin" 都直接重新導向到 /admin/restaurants/list
 */
@Controller
@RequestMapping({"/", ""})
public class DefaultViewController {

    @GetMapping({"/", "/admin"})
    public String redirectToAdminIndex() {
        return "redirect:/admin/restaurants";
    }
}
