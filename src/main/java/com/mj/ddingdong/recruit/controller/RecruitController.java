package com.mj.ddingdong.recruit.controller;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.circle.domain.Circle;
import com.mj.ddingdong.circle.service.CircleService;
import com.mj.ddingdong.main.CurrentAccount;
import com.mj.ddingdong.recruit.RecruitValidator;
import com.mj.ddingdong.recruit.domain.Recruit;
import com.mj.ddingdong.recruit.form.RecruitForm;
import com.mj.ddingdong.recruit.repository.RecruitRepository;
import com.mj.ddingdong.recruit.service.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/circle/{path}")
public class RecruitController {

    private final CircleService circleService;
    private final RecruitValidator recruitValidator;
    private final RecruitService recruitService;
    private final RecruitRepository recruitRepository;

    @InitBinder("recruitForm")
    public void initbinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(recruitValidator);
    }

    @GetMapping("/recruit")
    public String recruitView(@CurrentAccount Account account, @PathVariable String path, Model model){
        Circle circle = circleService.validatePath(path);
        model.addAttribute(account);
        model.addAttribute(circle);
        model.addAttribute("isManager",account.isRecognizedManager() && account.isRecognizedManager());

        return "circle/recruit/view";
    }

    @GetMapping("/recruit-ended")
    public String recruitEndedView(@CurrentAccount Account account, @PathVariable String path, Model model){
        Circle circle = circleService.validatePath(path);
        model.addAttribute(account);
        model.addAttribute(circle);
        model.addAttribute("isManager",account.isRecognizedManager() && account.isRecognizedManager());

        return "circle/recruit/view-ended";
    }

    @GetMapping("/recruit/create")
    public String createRecruitView(@CurrentAccount Account account, @PathVariable String path, Model model){
        Circle circle = circleService.validatePath(path);
        model.addAttribute(account);
        model.addAttribute(circle);

        circleService.validateAccountMemberOrManager(account,circle);
        model.addAttribute(new RecruitForm());
        return "circle/recruit/create";
    }

    @PostMapping("/recruit/create")
    public String createRecruit(@CurrentAccount Account account, @PathVariable String path,
                                @Valid RecruitForm recruitForm, Errors errors,
                                Model model, RedirectAttributes rttr) throws UnsupportedEncodingException {
        Circle circle = circleService.validatePath(path);
        circleService.validateAccountMemberOrManager(account,circle);
        model.addAttribute(account);
        model.addAttribute(circle);

        if(errors.hasErrors()){
            return "circle/recruit/create";
        }

        Recruit recruit = recruitService.createRecruit(recruitForm,circle,account);
        rttr.addFlashAttribute("success","공고가 성공적으로 등록되었습니다.");

        return "redirect:/circle/"+circle.getEncodedPath(path)+"/recruit/detail?id="+recruit.getId();

    }

    @GetMapping("/recruit/detail")
    public String recruitDetailView(@CurrentAccount Account account, @PathVariable String path,
                                    Model model, Long id){
        Circle circle = circleService.validatePath(path);
        circleService.validateAccountMemberOrManager(account,circle);
        model.addAttribute(account);
        model.addAttribute(circle);

        Optional<Recruit> byId = recruitRepository.findById(id);
        if(byId.get() != null){
            Recruit recruit = byId.get();
            model.addAttribute(recruit);
            model.addAttribute("managers",recruit.getManagers());
            model.addAttribute("isManager",account.isRecognizedManager() && account.isRecognizedManager());
        }
        else{
            throw new IllegalArgumentException(id+"에 해당하는 공고가 없습니다.");
        }

        return "circle/recruit/detail";
    }
}
