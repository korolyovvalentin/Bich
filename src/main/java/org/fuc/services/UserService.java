package org.fuc.services;

import org.fuc.core.criterias.AccountCriteria;
import org.fuc.core.Command;
import org.fuc.core.QuerySingle;
import org.fuc.entities.Account;
import org.fuc.core.criterias.EmailCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.PostConstruct;
import java.util.Collections;

public class UserService implements UserDetailsService {
    @Autowired
    @Qualifier("accountByEmailQuery")
    private QuerySingle<Account> findAccountByEmail;
    @Autowired
    @Qualifier("createAccountCommand")
    private Command createAccount;

    @PostConstruct
    public void initialize() {
        Account admin = findAccountByEmail.query(new EmailCriteria("admin"));
        if (admin == null) {
            createAccount.execute(new AccountCriteria(new Account("admin", "admin", "ROLE_ADMIN")));
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = findAccountByEmail.query(new EmailCriteria(username));
        if (account == null) {
            throw new UsernameNotFoundException("user not found");
        }
        return createUser(account);
    }

    public void signin(Account account) {
        SecurityContextHolder.getContext().setAuthentication(authenticate(account));
    }

    private Authentication authenticate(Account account) {
        return new UsernamePasswordAuthenticationToken(createUser(account), null, Collections.singleton(createAuthority(account)));
    }

    private User createUser(Account account) {
        return new User(account.getEmail(), account.getPassword(), Collections.singleton(createAuthority(account)));
    }

    private GrantedAuthority createAuthority(Account account) {
        return new SimpleGrantedAuthority(account.getRole());
    }

}
