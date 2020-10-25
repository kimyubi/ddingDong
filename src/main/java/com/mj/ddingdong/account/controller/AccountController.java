package com.mj.ddingdong.account.controller;

import com.mj.ddingdong.account.SignUpValidator;
import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.account.form.SignUpForm;
import com.mj.ddingdong.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final SignUpValidator signUpValidator;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(signUpValidator);
    }
    
    @GetMapping("/sign-up")
    public String signUpview(Model model){
        model.addAttribute(new SignUpForm());

        return "account/sign-up";
    }
    
    @PostMapping("/sign-up")
    public String signUp(@Valid SignUpForm signUpForm, Errors errors){
        if (errors.hasErrors()) {
            return "account/sign-up";
        }
        Account account = accountService.signUp(signUpForm);
        accountService.login(account);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String logIn(){
        return "account/login";
    }


}
