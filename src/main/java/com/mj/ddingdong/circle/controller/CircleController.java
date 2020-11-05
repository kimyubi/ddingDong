package com.mj.ddingdong.circle.controller;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.circle.CircleFormValidator;
import com.mj.ddingdong.circle.domain.Circle;
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

        if(!(account.isRecognizedManager()&&account.isRecognizedManager())){
            throw new AccessDeniedException("해당 기능을 이용하실 수 없습니다.");
        }
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
        model.addAttribute(account);
        Circle circle = circleRepository.findByPath(path);

        if(circle == null){
            throw new IllegalArgumentException(path +"에 해당하는 동아리가 존재하지 않습니다.");
        }
        model.addAttribute(circle);

        return "circle/detail";
    }

    @GetMapping("/circle/{path}/member")
    public String circleMembers(@CurrentAccount Account account, Model model, @PathVariable String path){
        Circle circle = circleRepository.findByPath(path);
        if(circle == null){
            throw new IllegalArgumentException(path +"에 해당하는 동아리가 존재하지 않습니다.");
        }
        if(!((account.isRecognizedManager()&&account.isSignUpAsManager())|| circle.isMember(account))){
            throw new AccessDeniedException("해당 기능을 이용하실 수 없습니다.");
        }

        model.addAttribute(account);
        model.addAttribute(circle);

        return "circle/member";
    }
}
