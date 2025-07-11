package com.pmf.course.api_gateway.storage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
@Table(name = "api_key")
public class ApiKeyEntity {

    @Id
    @NotNull
    @Size(max = 48)
    @Column(nullable = false, name = "secret", length = 48)
    private String secret;

    @NotNull
    @Column(nullable = false, name = "user_id")
    private Long userId;

    public ApiKeyEntity() {
    }

    public ApiKeyEntity(String secret, Long userId) {
        this.secret = secret;
        this.userId = userId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ApiKeyEntity{" +
                "secret='" + secret.substring(0, 4) + "...<redacted>'" +
                ", userId=" + userId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ApiKeyEntity that = (ApiKeyEntity) o;
        return Objects.equals(secret, that.secret);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(secret);
    }
}