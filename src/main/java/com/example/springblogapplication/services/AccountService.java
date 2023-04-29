package com.example.springblogapplication.services;

import com.example.springblogapplication.models.Account;
import com.example.springblogapplication.models.Authority;
import com.example.springblogapplication.repositories.AccountRepository;
import com.example.springblogapplication.repositories.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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
    private AuthorityRepository authorityRepository;

    //Singleton
    private AccountService() {}
    public static synchronized AccountService getInstance() {
        if (instance == null) {
            instance = new AccountService();
        }
        return instance;
    }

    public Account save(Account account) {

        if (account.getId() == null) {
            if (account.getAuthorities().isEmpty()) {
                Set<Authority> authorities = new HashSet<>();
                authorityRepository.findById("ROLE_USER").ifPresent(authorities::add);
                account.setAuthorities(authorities);
            }
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    public Account update(Account account) {
        Optional<Account> optionalAccount = accountRepository.findOneByEmailIgnoreCase(account.getEmail());
        if (optionalAccount.isPresent()) {
            Account currentAccount = optionalAccount.get();
            currentAccount.setFirstName(account.getFirstName());
            currentAccount.setLastName(account.getLastName());
            currentAccount.setPassword(passwordEncoder.encode(account.getPassword()));
            return accountRepository.save(currentAccount);
        }
        return null;
    }


    public Optional<Account> findOneByEmail(String email) {
        return accountRepository.findOneByEmailIgnoreCase(email);
    }
}