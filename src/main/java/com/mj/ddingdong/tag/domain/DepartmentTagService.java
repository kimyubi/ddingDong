package com.mj.ddingdong.tag.domain;

import com.mj.ddingdong.tag.repository.DepartmentTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentTagService {

    private final DepartmentTagRepository departmentTagRepository;

    @PostConstruct
    public void initDepartmentData() throws IOException {
        if (departmentTagRepository.count() == 0) {
            Resource resource = new ClassPathResource("department_major.csv");
            List<DepartmentTag> departmentTagList = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8)
                    .stream().map(line -> { return DepartmentTag.builder().title(line).build();}).collect(Collectors.toList());
            departmentTagRepository.saveAll(departmentTagList);
        }
    }

}
