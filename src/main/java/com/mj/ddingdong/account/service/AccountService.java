package com.mj.ddingdong.account.service;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.account.domain.UserAccount;
import com.mj.ddingdong.account.form.SignUpForm;
import com.mj.ddingdong.account.repository.AccountRepository;
import com.mj.ddingdong.profiles.form.ProfileForm;
import com.mj.ddingdong.tag.domain.DepartmentTag;
import com.mj.ddingdong.tag.domain.FieldTag;
import com.mj.ddingdong.tag.domain.IntroduceTag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper = new ModelMapper();

    public Account signUp(SignUpForm signUpForm) {
        Account newAccount = saveAccount(signUpForm);
        return newAccount;
    }

    private Account saveAccount(SignUpForm signUpForm) {
        Account account = Account.builder()
                .signUpId(signUpForm.getSignUpId())
                .name(signUpForm.getName())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .signUpAsManager(signUpForm.isSignUpAsManager())
                .signUpAt(LocalDateTime.now())
                .build();
        return  accountRepository.save(account);
    }

    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String signUpId) throws UsernameNotFoundException {
        Account account = accountRepository.findBySignUpId(signUpId);
        if(account == null){
            throw new UsernameNotFoundException(signUpId);
        }

        return new UserAccount(account);
    }

    public void updateProfile(Account account, ProfileForm profileForm) {
        modelMapper.map(profileForm,account);
        accountRepository.save(account);
    }

    public Set<DepartmentTag> getDepartmentTags(Account account) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        return byId.orElseThrow().getDepartmentTags();
    }

    public void addDepartmentTag(Account account, DepartmentTag departmentTag) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        byId.ifPresent(a -> a.getDepartmentTags().add(departmentTag));
    }

    public void removeDepartmentTag(Account account, DepartmentTag departmentTag) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        byId.ifPresent(a -> a.getDepartmentTags().remove(departmentTag));
    }

    public void addIntroduceTag(Account account, IntroduceTag introduceTag) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        byId.ifPresent(a -> a.getIntroduceTags().add(introduceTag));
    }

    public void removeIntroduceTag(Account account, IntroduceTag introduceTag) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        byId.ifPresent(a -> a.getIntroduceTags().remove(introduceTag));
    }

    public Set<IntroduceTag> getIntroduceTags(Account account) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        return byId.orElseThrow().getIntroduceTags();
    }

    public Set<FieldTag> getFieldTags(Account account) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        return byId.orElseThrow().getFieldTags();
    }

    public void addFieldTag(Account account, FieldTag fieldTag) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        byId.ifPresent(a -> a.getFieldTags().add(fieldTag));
    }

    public void removeFieldTag(Account account, FieldTag fieldTag) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        byId.ifPresent(a -> a.getFieldTags().remove(fieldTag));
    }
}
