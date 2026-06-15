package com.auto.report.report.entity;


import com.auto.report.common.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "report")
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private Long dataId; // data id

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false, length = 30)
    private LocalDateTime startedAt;

    @Column(nullable = false, length = 30)
    private LocalDateTime finishedAt;


    public static ReportEntity toEntity(Long dataId, Status status, LocalDateTime startedAt, LocalDateTime finishedAt) {
        return ReportEntity.builder()
                .dataId(dataId)
                .status(status)
                .startedAt(startedAt)
                .finishedAt(finishedAt)
                .build();
    }

}
