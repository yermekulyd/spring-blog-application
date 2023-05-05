package com.example.springblogapplication.services;

import com.example.springblogapplication.models.Account;
import com.example.springblogapplication.models.Authority;
import com.example.springblogapplication.repositories.AccountRepository;
import com.example.springblogapplication.repositories.AuthorityRepository;
import com.example.springblogapplication.repositories.CommentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AccountService {

    //Singleton
    private static AccountService instance;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CommentRepository commentRepository;

    //Singleton
    private AccountService() {}
    public static synchronized AccountService getInstance() {
        if (instance == null) {
            instance = new AccountService();
        }
        return instance;
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account update(Account account) {
        Optional<Account> optionalAccount = accountRepository.findOneByEmailIgnoreCase(account.getEmail());
        if (optionalAccount.isPresent()) {
            Account currentAccount = optionalAccount.get();
            currentAccount.setFirstName(account.getFirstName());
            currentAccount.setLastName(account.getLastName());
            currentAccount.setPassword(passwordEncoder.encode(account.getPassword()));
            currentAccount.setPhotos(account.getPhotos());
            return accountRepository.save(currentAccount);
        }
        return null;
    }

    public void delete(Long id) {
        accountRepository.deleteById(id);
    }

    public Optional<Account> findOneByEmail(String email) {
        return accountRepository.findOneByEmailIgnoreCase(email);
    }
}