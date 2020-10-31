package com.mj.ddingdong.main.controller;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.account.form.SignUpForm;
import com.mj.ddingdong.account.service.AccountService;
import com.mj.ddingdong.main.CurrentAccount;
import com.mj.ddingdong.main.EmailForm;
import com.mj.ddingdong.main.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;
    private final AccountService accountService;

    @GetMapping("/")
    public String main(@CurrentAccount Account account, Model model)
    {
        model.addAttribute("account",account);
        return "index";
    }

    @GetMapping("/login")
    public String logIn(){
        return "account/login";
    }

    @GetMapping("/sign-up")
    public String signUpview(Model model){
        model.addAttribute(new SignUpForm());

        return "account/sign-up";
    }

    @GetMapping("/find-account")
    public String findAccountView(Model model){
        model.addAttribute(new EmailForm());
        return "find-account";
    }

    @PostMapping("/find-account")
    public String findAccount(@Valid EmailForm emailForm, Errors errors, Model model){
        if (errors.hasErrors()) {
            return "find-account";
        }
        String email = emailForm.getEmail();
        if(!accountService.validateEmail(email)){
            model.addAttribute("message","유효하지 않은 이메일입니다. 가입할 때 입력하신 이메일을 입력해주세요.");
            return "find-account";
        }
        mainService.sendFindAccountEmail(email);
        model.addAttribute("email", email);
        return "sended-email";
    }


}
