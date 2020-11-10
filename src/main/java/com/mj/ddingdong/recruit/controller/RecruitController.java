package com.mj.ddingdong.recruit.controller;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.circle.domain.Circle;
import com.mj.ddingdong.circle.service.CircleService;
import com.mj.ddingdong.main.CurrentAccount;
import com.mj.ddingdong.recruit.RecruitValidator;
import com.mj.ddingdong.recruit.domain.Enrollment;
import com.mj.ddingdong.recruit.domain.Recruit;
import com.mj.ddingdong.recruit.form.EnrollmentForm;
import com.mj.ddingdong.recruit.form.RecruitForm;
import com.mj.ddingdong.recruit.repository.EnrollmentRepository;
import com.mj.ddingdong.recruit.repository.RecruitRepository;
import com.mj.ddingdong.recruit.service.EnrollmentService;
import com.mj.ddingdong.recruit.service.RecruitService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.ErrorState;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/circle/{path}")
public class RecruitController {

    private final CircleService circleService;
    private final RecruitValidator recruitValidator;
    private final RecruitService recruitService;
    private final RecruitRepository recruitRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final EnrollmentService enrollmentService;
    private final EnrollmentRepository enrollmentRepository;

    @InitBinder("recruitForm")
    public void initbinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(recruitValidator);
    }

    @GetMapping("/recruit")
    public String recruitView(@CurrentAccount Account account, @PathVariable String path, Model model){
        Circle circle = circleService.validatePath(path);

        model.addAttribute(account);
        model.addAttribute(circle);

        model.addAttribute("isManager",circleService.isManagerToCircle(account,circle));

        List<Recruit> recruits = recruitRepository.findByCircleOrderByStartRecruitTime(circle);
        List<Recruit> currentRecruit = new ArrayList<>();
        recruits.forEach(e -> {
            if (e.getEndRecruitTime().isAfter(LocalDateTime.now())) {
                currentRecruit.add(e);
            }
        });

        model.addAttribute("currentRecruit", currentRecruit);
        return "circle/recruit/view";
    }

    @GetMapping("/recruit-ended")
    public String recruitEndedView(@CurrentAccount Account account, @PathVariable String path, Model model){
        Circle circle = circleService.validatePath(path);
        model.addAttribute(account);
        model.addAttribute(circle);

        model.addAttribute("isManager",circleService.isManagerToCircle(account,circle));

        List<Recruit> recruits = recruitRepository.findByCircleOrderByStartRecruitTime(circle);
        List<Recruit> endedRecruit = new ArrayList<>();
        recruits.forEach(e -> {
            if (e.getEndRecruitTime().isBefore(LocalDateTime.now())) {
                endedRecruit.add(e);
            }
        });

        model.addAttribute("endedRecruit", endedRecruit);

        return "circle/recruit/view-ended";
    }

    @GetMapping("/recruit/create")
    public String createRecruitView(@CurrentAccount Account account, @PathVariable String path, Model model){
        Circle circle = circleService.validatePath(path);
        model.addAttribute(account);
        model.addAttribute(circle);

        circleService.circleManagedByManager(account,circle);
        model.addAttribute(new RecruitForm());
        return "circle/recruit/create";
    }

    @PostMapping("/recruit/create")
    public String createRecruit(@CurrentAccount Account account, @PathVariable String path,
                                @Valid RecruitForm recruitForm, Errors errors,
                                Model model, RedirectAttributes rttr) throws UnsupportedEncodingException {
        Circle circle = circleService.validatePath(path);
        circleService.circleManagedByManager(account,circle);
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

        model.addAttribute(account);
        model.addAttribute(circle);

        Optional<Recruit> byId = recruitRepository.findById(id);
        if(byId.get() != null){
            Recruit recruit = byId.get();
            model.addAttribute(recruit);
            model.addAttribute("managers",recruit.getManagers());
            model.addAttribute("isManager",circleService.isManagerToCircle(account,circle));
            model.addAttribute(modelMapper.map(recruit, EnrollmentForm.class));
        }
        else{
            throw new IllegalArgumentException(id+"에 해당하는 공고가 없습니다.");
        }

        return "circle/recruit/detail";
    }

    @PostMapping("/recruit/enrollment")
    public String recruitEnrollment(@CurrentAccount Account account, @PathVariable String path,
                                    Model model, Long id, @Valid EnrollmentForm enrollmentForm,
                                    Errors errors, RedirectAttributes rttr) throws UnsupportedEncodingException {
        Circle circle = circleService.validatePath(path);
        Optional<Recruit> byId = recruitRepository.findById(id);
        Recruit recruit;
        if(byId.get() != null) {
            recruit = byId.get();
        } else{
            throw new IllegalArgumentException(id+"에 해당하는 공고가 없습니다.");
        }
        model.addAttribute(account);
        model.addAttribute(circle);
        model.addAttribute("recruit",recruit);

        if(errors.hasErrors()){
            return "circle/recruit/detail";
        }
        if(recruit.getEndRecruitTime().isBefore(LocalDateTime.now())||recruit.getStartRecruitTime().isAfter(LocalDateTime.now())){
            model.addAttribute("failure","현재는 모집 기간이 아닙니다.");
            return "circle/recruit/detail";
        }
        if(enrollmentRepository.isAlreadyEnrolled(account,recruit) != 0){
            model.addAttribute("failure","이미 지원한 공고입니다.");
            return "circle/recruit/detail";
        }

        enrollmentService.enrollmentToRecruit(account,circle,recruit,enrollmentForm);
        rttr.addFlashAttribute("success","동아리 지원이 성공적으로 접수되었습니다.");

        return "redirect:/circle/"+circle.getEncodedPath(path)+"/recruit";
    }

    @GetMapping("/recruit/delete")
    public String deleteRecruit(@CurrentAccount Account account,Long id, @PathVariable String path,RedirectAttributes rttr){
        Circle circle = circleService.validatePath(path);
        Optional<Recruit> byId = recruitRepository.findById(id);
        Recruit recruit;
        if(byId.get() != null) {
            recruit = byId.get();
        } else{
            throw new IllegalArgumentException(id+"에 해당하는 공고가 없습니다.");
        }
        recruitService.deleteRecruit(circle,recruit);
        rttr.addFlashAttribute("success","공고가 성공적으로 삭제 되었습니다.");
        return "redirect:/circle/"+path+"/recruit";

    }

    @GetMapping("/recruit/modify")
    public String deleteRecruit(@CurrentAccount Account account,Long id, @PathVariable String path, Model model){
        Circle circle = circleService.validatePath(path);
        Optional<Recruit> byId = recruitRepository.findById(id);
        Recruit recruit;
        if(byId.get() != null) {
            recruit = byId.get();
        } else{
            throw new IllegalArgumentException(id+"에 해당하는 공고가 없습니다.");
        }
        circleService.circleManagedByManager(account,circle);
        model.addAttribute(circle);
        model.addAttribute(account);
        model.addAttribute(recruit);
        model.addAttribute(modelMapper.map(recruit,RecruitForm.class));

        return "circle/recruit/modify";
    }

    @PostMapping("/recruit/modify")
    public String modifyRecruit(@CurrentAccount Account account, Long id, @PathVariable String path,
                                @Valid RecruitForm recruitForm, Errors errors, Model model, RedirectAttributes rttr) throws UnsupportedEncodingException {
        Circle circle = circleService.validatePath(path);
        Optional<Recruit> byId = recruitRepository.findById(id);
        Recruit recruit;
        if(byId.get() != null) {
            recruit = byId.get();
        } else{
            throw new IllegalArgumentException(id+"에 해당하는 공고가 없습니다.");
        }

        if(errors.hasErrors()){
            model.addAttribute(circle);
            model.addAttribute(account);
            model.addAttribute(recruit);
            return "circle/recruit/modify";
        }

        circleService.circleManagedByManager(account,circle);
        recruitService.modifyRecruit(recruit,recruitForm);

        rttr.addFlashAttribute("success","공고가 성공적으로 수정되었습니다.");
        return "redirect:/circle/"+circle.getEncodedPath(path)+"/recruit/detail?id="+id;

    }


    @GetMapping("/recruit/applications")
    public String applicationsView(@CurrentAccount Account account,Long id, @PathVariable String path, Model model){
        Circle circle = circleService.validatePath(path);
        Optional<Recruit> byId = recruitRepository.findById(id);
        Recruit recruit;
        if(byId.get() != null) {
            recruit = byId.get();
        } else{
            throw new IllegalArgumentException(id+"에 해당하는 공고가 없습니다.");
        }
        model.addAttribute(circle);
        model.addAttribute(account);
        model.addAttribute(recruit);

        circleService.circleManagedByManager(account,circle);
        List<Enrollment> enrollments = enrollmentRepository.findByRecruitIdOrderByEnrolledTime(recruit.getId());
        model.addAttribute("enrollments",enrollments);
        return "circle/recruit/applications";
    }
}
