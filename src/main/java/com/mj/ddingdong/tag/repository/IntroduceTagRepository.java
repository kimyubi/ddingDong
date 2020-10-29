package com.mj.ddingdong.tag.repository;

import com.mj.ddingdong.tag.domain.IntroduceTag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface IntroduceTagRepository extends JpaRepository<IntroduceTag,Long> {
    IntroduceTag findByTitle(String tagTitle);
}
