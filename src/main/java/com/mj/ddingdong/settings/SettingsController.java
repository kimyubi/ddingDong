package com.mj.ddingdong.settings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.account.service.AccountService;
import com.mj.ddingdong.main.CurrentAccount;
import com.mj.ddingdong.settings.form.NotificationForm;
import com.mj.ddingdong.tag.domain.DepartmentTag;
import com.mj.ddingdong.tag.domain.FieldTag;
import com.mj.ddingdong.tag.domain.IntroduceTag;
import com.mj.ddingdong.tag.form.TagForm;
import com.mj.ddingdong.tag.repository.DepartmentTagRepository;
import com.mj.ddingdong.tag.repository.FieldTagRepository;
import com.mj.ddingdong.tag.repository.IntroduceTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.rule.Mode;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SettingsController {

    private final AccountService accountService;
    private final DepartmentTagRepository departmentTagRepository;
    private final ObjectMapper objectMapper;
    private final IntroduceTagRepository introduceTagRepository;
    private final FieldTagRepository fieldTagRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/settings/tags")
    public String getTagsView(@CurrentAccount Account account, Model model) throws JsonProcessingException {
        model.addAttribute(account);

        Set<DepartmentTag> departmentTags = accountService.getDepartmentTags(account);
        model.addAttribute("departmentTags",departmentTags.stream().map(DepartmentTag :: getTitle).collect(Collectors.toList()));
        List<String> allDepartmentTags = departmentTagRepository.findAll().stream().map(DepartmentTag :: getTitle).collect(Collectors.toList());
        model.addAttribute("departmentWhitelist", objectMapper.writeValueAsString(allDepartmentTags));


        Set<IntroduceTag> introduceTags = accountService.getIntroduceTags(account);
        model.addAttribute("introduceTags",introduceTags.stream().map(IntroduceTag :: getTitle).collect(Collectors.toList()));
        List<String> allIntroduceTags = introduceTagRepository.findAll().stream().map(IntroduceTag :: getTitle).collect(Collectors.toList());
        model.addAttribute("introduceWhitelist", objectMapper.writeValueAsString(allIntroduceTags));

        Set<FieldTag> fieldTags = accountService.getFieldTags(account);
        model.addAttribute("fieldTags",fieldTags.stream().map(FieldTag :: getTitle).collect(Collectors.toList()));
        List<String> allfieldTags = fieldTagRepository.findAll().stream().map(FieldTag :: getTitle).collect(Collectors.toList());
        model.addAttribute("fieldWhitelist", objectMapper.writeValueAsString(allfieldTags));

        return "settings/tags";
    }

    @PostMapping("/settings/departmentTags/add")
    @ResponseBody
    public ResponseEntity addDepartmentTags(@CurrentAccount Account account, Model model, @RequestBody TagForm tagForm){
        model.addAttribute(account);
        DepartmentTag departmentTag = departmentTagRepository.findByTitle(tagForm.getTagTitle());
        if (departmentTag == null) {
            return ResponseEntity.badRequest().build();
        }
        accountService.addDepartmentTag(account, departmentTag);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/settings/departmentTags/remove")
    @ResponseBody
    public ResponseEntity removeDepartmentTags(@CurrentAccount Account account, Model model, @RequestBody TagForm tagForm){
        model.addAttribute(account);
        DepartmentTag departmentTag = departmentTagRepository.findByTitle(tagForm.getTagTitle());
        if (departmentTag == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.removeDepartmentTag(account, departmentTag);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/settings/introduceTags/add")
    @ResponseBody
    public ResponseEntity addIntroduceTags(@CurrentAccount Account account, Model model, @RequestBody TagForm tagForm){
        model.addAttribute(account);
        IntroduceTag introduceTag = introduceTagRepository.findByTitle(tagForm.getTagTitle());
        if (introduceTag == null) {
            introduceTag = IntroduceTag.builder().title(tagForm.getTagTitle()).build();
            introduceTagRepository.save(introduceTag);
        }
        accountService.addIntroduceTag(account, introduceTag);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/settings/introduceTags/remove")
    @ResponseBody
    public ResponseEntity removeIntroduceTagsTags(@CurrentAccount Account account, Model model, @RequestBody TagForm tagForm){
        model.addAttribute(account);
        IntroduceTag introduceTag = introduceTagRepository.findByTitle(tagForm.getTagTitle());
        if (introduceTag == null) {
            return ResponseEntity.badRequest().build();
        }
        accountService.removeIntroduceTag(account, introduceTag);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/settings/fieldTags/add")
    @ResponseBody
    public ResponseEntity addFieldTags(@CurrentAccount Account account, Model model, @RequestBody TagForm tagForm){
        model.addAttribute(account);
        FieldTag fieldTag = fieldTagRepository.findByTitle(tagForm.getTagTitle());
        if (fieldTag == null) {
            return ResponseEntity.badRequest().build();
        }
        accountService.addFieldTag(account, fieldTag);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/settings/fieldTags/remove")
    @ResponseBody
    public ResponseEntity removeFieldTags(@CurrentAccount Account account, Model model, @RequestBody TagForm tagForm){
        model.addAttribute(account);
        FieldTag fieldTag = fieldTagRepository.findByTitle(tagForm.getTagTitle());
        if (fieldTag == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.removeFieldTag(account, fieldTag);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/settings/notification")
    public String notificationSettingview(@CurrentAccount Account account, Model model){
        model.addAttribute(account);
        model.addAttribute("notifications",modelMapper.map(account, NotificationForm.class));
        return "settings/notification";
    }

    @PostMapping("/settings/notification")
    public String updateNotificationSetting(@CurrentAccount Account account, RedirectAttributes rttr, @Valid NotificationForm notificationForm, Errors errors, Model model){
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return "settings/notification";
        }

        accountService.updateNotification(account,notificationForm);

        rttr.addFlashAttribute("message","알림 설정이 업데이트 되었습니다.");
        return "redirect:/settings/notification";

    }


}
