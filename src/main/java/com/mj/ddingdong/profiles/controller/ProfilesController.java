package com.mj.ddingdong.profiles.controller;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.account.repository.AccountRepository;
import com.mj.ddingdong.account.service.AccountService;
import com.mj.ddingdong.main.CurrentAccount;
import com.mj.ddingdong.profiles.form.ProfileForm;
import com.mj.ddingdong.profiles.validator.ProfileFormValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class ProfilesController {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final AccountService accountService;
    private final ProfileFormValidator profileFormValidator;

    @InitBinder("profileForm")
    public void initbinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(profileFormValidator);
    }
    
    @GetMapping("/profiles/{nickname}")
    public String profileView(@CurrentAccount Account account, @PathVariable String nickname, Model model){
        Account byNickname = accountRepository.findByNickname(nickname);
        if(byNickname == null){
            throw new IllegalArgumentException(nickname+"에 해당하는 사용자가 존재하지 않습니다.");
        }
        model.addAttribute("isOwner",account.equals(byNickname));
        model.addAttribute("account",account);
        model.addAttribute("byNickname",byNickname);

        return "profiles/view";
    }

    @GetMapping("/settings/profile")
    public String prifileSettingView(@CurrentAccount Account account, Model model){
        model.addAttribute(account);
        model.addAttribute("profileForm",modelMapper.map(account, ProfileForm.class));
        return "profiles/form";
    }

    @PostMapping("/settings/profile")
    public String profileSettingUpdate(@CurrentAccount Account account, Model model, RedirectAttributes rttr, @Valid ProfileForm profileForm, Errors errors){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return "profiles/form";
        }
        accountService.updateProfile(account,profileForm);
        rttr.addFlashAttribute("message","프로필이 업데이트 되었습니다.");
        rttr.addFlashAttribute("profileForm",profileForm);
        return "redirect:/settings/profile";
    }
}
