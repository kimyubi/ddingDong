package com.mj.ddingdong.main.controller;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.account.form.SignUpForm;
import com.mj.ddingdong.account.repository.AccountRepository;
import com.mj.ddingdong.account.service.AccountService;
import com.mj.ddingdong.circle.domain.Circle;
import com.mj.ddingdong.circle.repository.CircleRepository;
import com.mj.ddingdong.main.CurrentAccount;
import com.mj.ddingdong.main.EmailForm;
import com.mj.ddingdong.main.PasswordForm;
import com.mj.ddingdong.main.UpdatePasswordValidator;
import com.mj.ddingdong.main.service.MainService;
import com.mj.ddingdong.recruit.domain.Enrollment;
import com.mj.ddingdong.recruit.repository.EnrollmentRepository;
import com.mj.ddingdong.tag.domain.FieldTag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;
    private final AccountService accountService;
    private final UpdatePasswordValidator updatePasswordValidator;
    private final EnrollmentRepository enrollmentRepository;
    private final CircleRepository circleRepository;
    private final AccountRepository accountRepository;

    @InitBinder("passwordForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(updatePasswordValidator);
    }


    @GetMapping("/")
    public String main(@CurrentAccount Account account, Model model)
    {  model.addAttribute("account",account);
        if(account == null){
            model.addAttribute("circleList",circleRepository.findFirst9OrderByCreatedTimeDesc());
            return "index";
        }
        account = accountRepository.findAccountById(account.getId());
        Set<FieldTag> fieldTags = account.getFieldTags();
        model.addAttribute("defalutCircleList",circleRepository.findFirst12OrderByCreatedTimeDesc());
        model.addAttribute("managedCircles",circleRepository.findByManager(account));
        model.addAttribute("joinedCircles",circleRepository.findByMember(account));

        return "logined-index";
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

    @GetMapping("/update-password")
    public String updatePasswordView(@RequestParam("token") String token, Model model){
        if(!accountService.validateToken(token)){
            model.addAttribute("message","유효하지 않은 링크입니다.");
            return "update-password-wrong";
        }
        model.addAttribute("token",token);
        model.addAttribute("passwordForm",new PasswordForm());
        return "update-password";
    }

    @PostMapping("/update-password")
    public String updatePassword(String token, @Valid PasswordForm passwordForm, Errors errors, RedirectAttributes rttr, Model model){
        if(errors.hasErrors()){
            model.addAttribute("token",token);
            return "update-password";
        }

        rttr.addFlashAttribute("successUpdatePassword","비밀번호 변경이 완료되었습니다.");
        accountService.updatePassword(token,passwordForm);
        return "redirect:/login";
    }

    @GetMapping("/mycircle/{nickname}")
    public String myCircleView(@CurrentAccount Account account, @PathVariable String nickname, Model model){
        if(!account.getNickname().equals(nickname)){
            throw new AccessDeniedException("접근할 수 없습니다.");
        }

        List<Enrollment> enrollments = enrollmentRepository.findByAccountId(account.getId());
        model.addAttribute("enrollments",enrollments);
        model.addAttribute("account",account);
        model.addAttribute("isManager",account.isRecognizedManager()&&account.isRecognizedManager());


        return "mycircle";
    }

    @GetMapping("/search")
    public String search(@PageableDefault(size = 6, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable, @CurrentAccount Account account, String keyword, Model model){
        Page<Circle> circlePage = circleRepository.findByKeyword(keyword,pageable);

        model.addAttribute("account",account);
        model.addAttribute("keyword",keyword);
        model.addAttribute("circlePage",circlePage);

        return "search";


    }


}
