package com.project.hiptour.sync.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "load_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoadStatus {
    @Id
    private String jobName;

    private String lastSucceededAreaCode;

    private int lastSucceededPageNo;
}
