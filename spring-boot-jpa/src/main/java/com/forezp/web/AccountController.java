package com.forezp.web;

import com.forezp.dao.AccountDao;
import com.forezp.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by fangzhipeng on 2017/4/20.
 * 这里省略了service层，实际开发加上；
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountDao accountDao;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Account> getAccounts() {
        return accountDao.findAll();
    }
    
    @RequestMapping(value = "/list1", method = RequestMethod.GET)
    public Page<Account> getAccount1() {
        return accountDao.findByName("bbb", new PageRequest(0, 10));
    }
    
    @RequestMapping(value = "/list2", method = RequestMethod.GET)
    public Page<Account> getAccount2() {
        return accountDao.findByNameLike("bbb", new PageRequest(0, 10));
    }
    
    @RequestMapping(value = "/list3", method = RequestMethod.GET)
    public Page<Account> getAccount3() {
        return accountDao.findBymoney(1000, new PageRequest(0, 10));
    }
    
    @RequestMapping(value = "/list4", method = RequestMethod.GET)
    public List<Account> getAccount4() {
        return accountDao.findByName("bbb");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Account getAccountById(@PathVariable("id") int id) {
        return accountDao.findOne(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String updateAccount(@PathVariable("id") int id, @RequestParam(value = "name", required = true) String name,
                                @RequestParam(value = "money", required = true) double money) {
        Account account = new Account();
        account.setMoney(money);
        account.setName(name);
        account.setId(id);
        Account account1 = accountDao.saveAndFlush(account);

        return account1.toString();

    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String postAccount(@RequestParam(value = "name") String name,
                              @RequestParam(value = "money") double money) {
        Account account = new Account();
        account.setMoney(money);
        account.setName(name);
        Account account1 = accountDao.save(account);
        return account1.toString();

    }


}
