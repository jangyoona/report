package com.auto.report.data.entity;


import com.auto.report.common.Status;
import com.auto.report.data.dto.DataDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "data")
public class DataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 8)
    private String birth;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false, length = 10)
    private String gender;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createAt;

    @PrePersist
    public void prePersist() {
        this.status = Status.PENDING;
        this.createAt = LocalDateTime.now();
    }

    public static DataEntity from(DataDto data) {
        return DataEntity.builder()
                .name(data.getName())
                .birth(data.getBirth())
                .age(data.getAge())
                .gender(data.getGender())
                .build();
    }

}
