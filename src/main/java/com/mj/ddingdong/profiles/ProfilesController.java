package com.mj.ddingdong.profiles;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.account.repository.AccountRepository;
import com.mj.ddingdong.main.CurrentAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ProfilesController {

    private final AccountRepository accountRepository;
    
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
}
