package com.pmf.course.api_gateway.storage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity,String> {
    ApiKeyEntity findBySecret(String secret);
}
