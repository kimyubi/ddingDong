package com.mj.ddingdong.circle.controller;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.circle.CircleFormValidator;
import com.mj.ddingdong.circle.domain.Circle;
import com.mj.ddingdong.circle.form.CircleForm;
import com.mj.ddingdong.circle.service.CircleService;
import com.mj.ddingdong.main.CurrentAccount;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/circle/{path}")
public class CircleSettingController {

    private final CircleService circleService;
    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/setting")
    public String circleSettingView(@CurrentAccount Account account, @PathVariable String path, Model model){
        Circle circle = circleService.validatePath(path);
        circleService.circleManagedByManager(account,circle);
        model.addAttribute(account);
        model.addAttribute(circle);
        model.addAttribute("circleForm" , modelMapper.map(circle, CircleForm.class));

        return "circle/setting/circle-modify";
    }

    @PostMapping("/setting")
    public String circleSetting(@CurrentAccount Account account, @PathVariable String path, Model model,
                                @Valid CircleForm circleForm, Errors errors, RedirectAttributes rttr) throws UnsupportedEncodingException {
        Circle circle = circleService.validatePath(path);
        circleService.circleManagedByManager(account,circle);
        if(errors.hasErrors()){
            model.addAttribute(account);
            model.addAttribute(circle);
            return "circle/setting/circle-modify";
        }

        circleService.modifyCircle(circle, circleForm);
        rttr.addFlashAttribute("success","동아리가 성공적으로 수정되었습니다.");

        return "redirect:/circle/"+circle.getEncodedPath(path)+"/setting";
    }

    @GetMapping("/setting/banner")
    public String bannerSetting(@CurrentAccount Account account, @PathVariable String path, Model model){
        Circle circle = circleService.validatePath(path);
        circleService.circleManagedByManager(account,circle);

        model.addAttribute(account);
        model.addAttribute(circle);

        return "circle/setting/banner";
    }

    @PostMapping("/setting/banner")
    public String bannerSubmit(@CurrentAccount Account account, @PathVariable String path,
                                   String image, RedirectAttributes attributes) throws UnsupportedEncodingException {
        Circle circle = circleService.validatePath(path);
        circleService.circleManagedByManager(account,circle);
        circleService.updateBanner(circle, image);
        attributes.addFlashAttribute("success", "배너 이미지를 수정했습니다.");
        return "redirect:/circle/" + circle.getEncodedPath(path) + "/setting/banner";
    }

    @PostMapping("/setting/banner/enable")
    public String enableStudyBanner(@CurrentAccount Account account, @PathVariable String path) throws UnsupportedEncodingException {
        Circle circle = circleService.validatePath(path);
        circleService.circleManagedByManager(account,circle);
        circleService.enableStudyBanner(circle);
        return "redirect:/circle/" + circle.getEncodedPath(path) + "/setting/banner";
    }

    @PostMapping("/setting/banner/disable")
    public String disableStudyBanner(@CurrentAccount Account account, @PathVariable String path) throws UnsupportedEncodingException {
        Circle circle = circleService.validatePath(path);
        circleService.circleManagedByManager(account,circle);
        circleService.disableStudyBanner(circle);
        return "redirect:/circle/" + circle.getEncodedPath(path) + "/setting/banner";
    }
}
