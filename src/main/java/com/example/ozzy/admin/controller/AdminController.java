package com.example.ozzy.admin.controller;

import com.example.ozzy.admin.entity.UserDetail;
import com.example.ozzy.admin.entity.Users;
import com.example.ozzy.admin.service.AdminService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @Value("${admin.login.id}")
    private String ID;

    @Value("${admin.login.pwd}")
    private String PWD;

    private final AdminService adminService;

    @GetMapping()
    public String login() {
        return "admin/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestBody Map<String, String> map, HttpSession session) {
        if (ID.equals(map.get("id")) && PWD.equals(map.get("pwd"))) {
            session.setAttribute("admin",true);
            return "ok";
        }
        return "fail";
    }

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model,
                       HttpSession session) {

        if (session.getAttribute("admin") == null) {
            return "redirect:/admin";
        }

        List<Users> items = adminService.selectUsers(page, size);
        int totalItems = adminService.selectUsersCount();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        model.addAttribute("items", items);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "admin/list";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam(defaultValue = "0") int seq, Model model, HttpSession session) {

        if (session.getAttribute("admin") == null) {
            return "redirect:/admin";
        }

        List<UserDetail> userDetails = adminService.selectDetail(seq);
        model.addAttribute("items", userDetails);
        return "admin/detail";
    }

}
