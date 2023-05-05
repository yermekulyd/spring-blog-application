package com.example.springblogapplication.controllers;


import com.example.springblogapplication.models.Account;
import com.example.springblogapplication.models.Post;
import com.example.springblogapplication.services.AccountService;
import com.example.springblogapplication.services.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

// ProfileController.java
@Controller
public class ProfileController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/profile")
    public String showProfilePage(Model model, Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findOneByEmail(email);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            model.addAttribute("account", account);
            return "profile";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/profile/edit")
    public String showProfileEditPage(Model model, Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findOneByEmail(email);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            model.addAttribute("account", account);
            return "editProfile";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/profile/edit")
    public String handleProfileEdit(@ModelAttribute("account") Account account, Authentication authentication, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findOneByEmail(email);
        if (optionalAccount.isPresent()) {
            Account currentAccount = optionalAccount.get();
            currentAccount.setFirstName(account.getFirstName());
            currentAccount.setLastName(account.getLastName());
            currentAccount.setPassword(account.getPassword());

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            account.setPhotos(fileName);

            currentAccount.setPhotos(fileName);
            String uploadDir = "user-photos/" + currentAccount.getId();

            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            accountService.update(currentAccount);
            return "redirect:/profile";
        } else {
            return "redirect:/";
        }
    }

}


