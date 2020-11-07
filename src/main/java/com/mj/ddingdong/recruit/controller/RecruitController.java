package com.mj.ddingdong.recruit.controller;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.circle.domain.Circle;
import com.mj.ddingdong.circle.service.CircleService;
import com.mj.ddingdong.main.CurrentAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class RecruitController {

    private final CircleService circleService;

    @GetMapping("/circle/{path}/recruit")
    public String recruitView(@CurrentAccount Account account, @PathVariable String path, Model model){
        Circle circle = circleService.validatePath(path);
        model.addAttribute(account);
        model.addAttribute(circle);

        return "circle/recruit/view";
    }

    @GetMapping("/circle/{path}/recruit-ended")
    public String recruitEndedView(@CurrentAccount Account account, @PathVariable String path, Model model){
        Circle circle = circleService.validatePath(path);
        model.addAttribute(account);
        model.addAttribute(circle);

        return "circle/recruit/view-ended";
    }
}
