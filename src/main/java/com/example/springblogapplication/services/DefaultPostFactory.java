package com.example.springblogapplication.services;

import com.example.springblogapplication.models.Account;
import com.example.springblogapplication.models.Post;
import com.example.springblogapplication.repositories.PostFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

//Factory
@Service
public class DefaultPostFactory implements PostFactory {
    private final AccountService accountService;

    @Autowired
    public DefaultPostFactory(AccountService accountService) {
        this.accountService = accountService;
    }

    public Post createPost() {
        Post post = new Post();
        String authUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Account> optionalAccount = accountService.findOneByEmail(authUsername);
        if (optionalAccount.isPresent()) {
            post.setAccount(optionalAccount.get());
        }
        return post;
    }
}

