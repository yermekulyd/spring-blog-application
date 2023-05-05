package com.example.springblogapplication.controllers;

import com.example.springblogapplication.models.Account;
import com.example.springblogapplication.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/admin")
    public String adminPage(Model model, Authentication authentication) {
        // Check if the authenticated user is an admin
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            return "redirect:/";
        }

        // Get a list of all accounts from the account service
        List<Account> accounts = accountService.findAll();

        // Add the accounts to the model and return the admin page
        model.addAttribute("accounts", accounts);
        return "admin";
    }

    @PostMapping("/admin/delete")
    public String handleDeleteAccount(@RequestParam("id") Long id) {
        accountService.delete(id);
        return "redirect:/admin";
    }


}

