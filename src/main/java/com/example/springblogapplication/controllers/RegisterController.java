package com.example.springblogapplication.controllers;

import com.example.springblogapplication.models.Account;
import com.example.springblogapplication.models.Authority;
import com.example.springblogapplication.repositories.AccountRepository;
import com.example.springblogapplication.repositories.AuthorityRepository;
import com.example.springblogapplication.services.AccountService;
import com.example.springblogapplication.services.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Controller
public class RegisterController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @GetMapping("/register")
    public String getRegisterForm(Model model) {

        Account account = new Account();
        model.addAttribute("account", account);
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute Account account, @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if (account.getId() == null) {
            if (account.getAuthorities().isEmpty()) {
                Set<Authority> authorities = new HashSet<>();
                authorityRepository.findById("ROLE_USER").ifPresent(authorities::add);
                account.setAuthorities(authorities);
            }
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        account.setPhotos(fileName);

        Account savedUser = accountRepository.save(account);

        String uploadDir = "user-photos/" + savedUser.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        return "redirect:/";
    }

}
