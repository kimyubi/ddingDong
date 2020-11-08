package com.mj.ddingdong.circle.controller;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.circle.CircleFormValidator;
import com.mj.ddingdong.circle.domain.Activity;
import com.mj.ddingdong.circle.domain.Circle;
import com.mj.ddingdong.circle.form.ActivityForm;
import com.mj.ddingdong.circle.form.CircleForm;
import com.mj.ddingdong.circle.repository.ActivityRepository;
import com.mj.ddingdong.circle.repository.CircleRepository;
import com.mj.ddingdong.circle.service.ActivityService;
import com.mj.ddingdong.circle.service.CircleService;
import com.mj.ddingdong.main.CurrentAccount;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
public class CircleController {

    private final CircleFormValidator circleFormValidator;
    private final CircleService circleService;
    private final CircleRepository circleRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final ActivityRepository activityRepository;
    private final ActivityService activityService;

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
    public String circleActivitiy(@CurrentAccount Account account, Model model, @PathVariable String path,
                                  @PageableDefault(size = 10) Pageable pageable){
        Circle circle = circleService.validatePath(path);

        model.addAttribute(account);
        model.addAttribute(circle);
        model.addAttribute("isManager",account.isRecognizedManager() && account.isRecognizedManager());

        Page<Activity> activities = activityRepository.findByCircle(circle,pageable);
        model.addAttribute("activities",activities);
        model.addAttribute("pageable",pageable);

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

    @PostMapping("/circle/{path}/activity/write")
    public String saveCircleActivity(@CurrentAccount Account account, Model model, @PathVariable String path,
                                     @Valid ActivityForm activityForm, Errors errors, RedirectAttributes rttr) throws UnsupportedEncodingException {
        Circle circle = circleRepository.findByPath(path);
        if(errors.hasErrors()){
            model.addAttribute(account);
            model.addAttribute(circle);
            return "circle/write";
        }

        circleService.saveActivity(circle,modelMapper.map(activityForm, Activity.class));
        rttr.addFlashAttribute("success","게시글이 정상적으로 등록되었습니다.");
        rttr.addFlashAttribute("activities",circle.getActivities());

        return "redirect:/circle/"+circle.getEncodedPath(path)+"/activity";
    }

    @GetMapping("/circle/{path}/activity/detail")
    public String viewActivityDetail(@CurrentAccount Account account, Model model, @PathVariable String path, @RequestParam("id") String id, @RequestParam("page") int page){
        Circle circle = circleService.validatePath(path);
        model.addAttribute(account);
        model.addAttribute(circle);

        model.addAttribute("isManager",account.isRecognizedManager() && account.isRecognizedManager());

        Optional<Activity> activity = activityRepository.findById(Long.valueOf(id));
        model.addAttribute("activity",activity.get());
        model.addAttribute("page",page);

        activityService.plusViewCount(circle,activity);

        return "circle/detail-activity";

    }

    @GetMapping("/circle/{path}/activity/modify")
    public String modifyActivityView(@CurrentAccount Account account, Model model, @PathVariable String path,
                                     @RequestParam("id") String id, @RequestParam("page") int page){

        Circle circle = circleService.validatePath(path);
        if(circleService.circleManagedByManager(circle,account)) {
            model.addAttribute(account);
            model.addAttribute(circle);
            Optional<Activity> activity = activityRepository.findById(Long.valueOf(id));
            model.addAttribute(modelMapper.map(activity.get(),ActivityForm.class));
            model.addAttribute("id",id);
            model.addAttribute("page",page);
        }
        return "circle/modify-activity";
    }

    @PostMapping("/circle/{path}/activity/modify")
    public String modifyActivity(@CurrentAccount Account account, Model model, @PathVariable String path, @Valid ActivityForm activityForm,
                                 Errors errors,@RequestParam("id") String id, @RequestParam("page") int page,RedirectAttributes rttr) throws UnsupportedEncodingException {
        Circle circle = circleService.validatePath(path);
        if(errors.hasErrors()){
            model.addAttribute(account);
            model.addAttribute(circle);
            model.addAttribute("id",id);
            model.addAttribute("page",page);

            return "circle/modify-activity";
        }
        Optional<Activity> activity = activityRepository.findById(Long.valueOf(id));
        activityService.modifyActivity(activity.get(),activityForm);
        rttr.addFlashAttribute("success","게시글이 성공적으로 수정되었습니다.");

        return "redirect:/circle/"+circle.getEncodedPath(path)+"/activity/detail?id="+id+"&page="+page;
    }

    @PostMapping("/circle/{path}/activity/delete")
    public String deleteActivity(@CurrentAccount Account account, Model model, @PathVariable String path, @RequestParam("id") int id, @RequestParam("page") int page, RedirectAttributes rttr) throws UnsupportedEncodingException {
        Circle circle = circleService.validatePath(path);
        Optional<Activity> activity = activityRepository.findById(Long.valueOf(id));
        activityService.deleteActivity(activity.get());

        rttr.addFlashAttribute("deleteSuccess","게시글이 성공적으로 삭제되었습니다.");

        return "redirect:/circle/"+ circle.getEncodedPath(path)+"/activity?id="+id+"&page="+page;
    }


}
