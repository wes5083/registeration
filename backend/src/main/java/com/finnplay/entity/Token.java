package com.finnplay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;


@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tokens", indexes = {
        @Index(name = "index_tokens_email", columnList = "email"),
        @Index(name = "index_tokens_token", columnList = "token", unique = true)
})
public class Token implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, length = 200)
    private String token;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "last_see_at", nullable = false)
    private LocalDateTime lastSeeAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
