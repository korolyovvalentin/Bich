package org.fuc.account;

import org.fuc.core.QuerySingle;
import org.fuc.entities.Account;
import org.fuc.core.criterias.EmailCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.security.Principal;

@Controller
@Secured("ROLE_USER")
class AccountController {
    @Autowired
    @Qualifier("accountByEmailQuery")
    private QuerySingle<Account> findAccountByEmail;

    @RequestMapping(value = "account/current", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Account accounts(Principal principal) {
        Assert.notNull(principal);
        return findAccountByEmail.query(new EmailCriteria(principal.getName()));
    }
}
