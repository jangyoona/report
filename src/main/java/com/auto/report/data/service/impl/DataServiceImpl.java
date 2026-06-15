package com.auto.report.data.service.impl;

import com.auto.report.data.dto.DataDto;
import com.auto.report.data.entity.DataEntity;
import com.auto.report.data.repository.DataRepository;
import com.auto.report.data.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

    private final DataRepository dataRepository;

    public long createData(DataDto data) {

        DataEntity save = dataRepository.save(DataEntity.from(data));
        return save.getId();
    }
}
