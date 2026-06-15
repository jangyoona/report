package com.auto.report.data.repository;

import com.auto.report.common.Status;
import com.auto.report.data.entity.DataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataRepository extends JpaRepository<DataEntity, Long> {

    List<DataEntity> findByStatus(Status status);
}
