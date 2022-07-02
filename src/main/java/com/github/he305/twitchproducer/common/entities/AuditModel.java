package com.github.he305.twitchproducer.common.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties
@Getter
@Setter
public abstract class AuditModel implements Serializable {
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
