package com.project.hiptour.sync.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "load_status")
@Getter
@Setter
@NoArgsConstructor
public class LoadJobStatus {
    @Id
    @Enumerated(EnumType.STRING)
    private LoadJobType jobName;

    @Enumerated(EnumType.STRING)
    private JobExecutionStatus status;

    private String lastSucceededAreaCode;

    private int lastSucceededPageNo;

    public LoadJobStatus(LoadJobType jobName) {
        this.jobName = jobName;
        this.status = JobExecutionStatus.RUNNING;
        this.lastSucceededAreaCode = null;
        this.lastSucceededPageNo = 0;
    }
}
