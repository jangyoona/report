package com.auto.report.report.repository;


import com.auto.report.common.Status;
import com.auto.report.report.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

    ReportEntity findByStatus(Status status);


}
