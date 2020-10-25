package com.mj.ddingdong.main.controller;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.main.CurrentAccount;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String main(@CurrentAccount Account account, Model model)
    {
        model.addAttribute("account",account);
        return "index";
    }

}
