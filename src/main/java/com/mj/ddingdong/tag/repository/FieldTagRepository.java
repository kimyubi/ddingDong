package com.mj.ddingdong.tag.repository;

import com.mj.ddingdong.tag.domain.FieldTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface FieldTagRepository extends JpaRepository<FieldTag, Long> {
    FieldTag findByTitle(String tagTitle);
}
