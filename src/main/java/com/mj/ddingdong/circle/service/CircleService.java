package com.mj.ddingdong.circle.service;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.circle.domain.Circle;
import com.mj.ddingdong.circle.form.CircleForm;
import com.mj.ddingdong.circle.repository.CircleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CircleService {

    private final CircleRepository circleRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public Circle saveCircle(Account account, CircleForm circleForm) {
        checkManager(account);
        Circle circle = modelMapper.map(circleForm, Circle.class);
        circle.getManagers().add(account);

        return circleRepository.save(circle);
    }

    private void checkManager(Account account) {
        if(!(account.isRecognizedManager()&& account.isRecognizedManager())){
            throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
        }
    }
}
