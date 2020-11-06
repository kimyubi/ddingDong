package com.mj.ddingdong.circle.controller;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.circle.CircleFormValidator;
import com.mj.ddingdong.circle.domain.Circle;
import com.mj.ddingdong.circle.form.ActivityForm;
import com.mj.ddingdong.circle.form.CircleForm;
import com.mj.ddingdong.circle.repository.CircleRepository;
import com.mj.ddingdong.circle.service.CircleService;
import com.mj.ddingdong.main.CurrentAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Controller
@RequiredArgsConstructor
public class CircleController {

    private final CircleFormValidator circleFormValidator;
    private final CircleService circleService;
    private final CircleRepository circleRepository;

    @InitBinder("circleForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(circleFormValidator);
    }

    @GetMapping("/create-circles")
    public String createCircleForm(@CurrentAccount Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(new CircleForm());

        circleService.validateAccountManager(account);
        return "circle/form";
    };

    @PostMapping("/create-circles")
    public String createCircle(@CurrentAccount Account account, @Valid CircleForm circleForm,
                               Errors errors, Model model) throws UnsupportedEncodingException {
        if(errors.hasErrors()){
            model.addAttribute(account);
            return "circle/form";
        }
        Circle circle = circleService.saveCircle(account,circleForm);
        return "redirect:/circle/"+circle.getEncodedPath(circle.getPath());
    }

    @GetMapping("/circle/{path}")
    public String circleDetail(@CurrentAccount Account account, Model model, @PathVariable String path){
        Circle circle = circleService.validatePath(path);

        model.addAttribute(circle);
        model.addAttribute(account);

        return "circle/detail";
    }

    @GetMapping("/circle/{path}/member")
    public String circleMembers(@CurrentAccount Account account, Model model, @PathVariable String path){
        Circle circle = circleService.validatePath(path);
        circleService.validateAccountMemberOrManager(account,circle);

        model.addAttribute(account);
        model.addAttribute(circle);

        return "circle/member";
    }

    @GetMapping("/circle/{path}/activity")
    public String circleActivitiy(@CurrentAccount Account account, Model model, @PathVariable String path){
        Circle circle = circleService.validatePath(path);
        model.addAttribute(account);
        model.addAttribute(circle);
        model.addAttribute("isManager",account.isRecognizedManager() && account.isRecognizedManager());
        return "circle/activity";
    }

    @GetMapping("/circle/{path}/activity/write")
    public String createCircleActivity(@CurrentAccount Account account, Model model, @PathVariable String path){
        Circle circle = circleService.validatePath(path);
        if(circleService.circleManagedByManager(circle,account)){
            model.addAttribute(account);
            model.addAttribute(circle);
            model.addAttribute(new ActivityForm());
        }
        return "circle/write";
    }


}
