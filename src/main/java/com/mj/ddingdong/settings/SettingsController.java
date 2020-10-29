package com.mj.ddingdong.settings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.account.service.AccountService;
import com.mj.ddingdong.main.CurrentAccount;
import com.mj.ddingdong.tag.domain.DepartmentTag;
import com.mj.ddingdong.tag.form.TagForm;
import com.mj.ddingdong.tag.repository.DepartmentTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @GetMapping("/settings/tags")
    public String getTagsView(@CurrentAccount Account account, Model model) throws JsonProcessingException {
        model.addAttribute(account);

        Set<DepartmentTag> departmentTags = accountService.getDepartmentTags(account);
        model.addAttribute("departmentTags",departmentTags.stream().map(DepartmentTag :: getTitle).collect(Collectors.toList()));

        List<String> allDepartmentTags = departmentTagRepository.findAll().stream().map(DepartmentTag :: getTitle).collect(Collectors.toList());
        model.addAttribute("departmentWhitelist", objectMapper.writeValueAsString(allDepartmentTags));

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

}
