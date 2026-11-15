package com.saveup.api.user.repository;

import com.saveup.api.user.entity.ApiUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiUserRepository extends JpaRepository<ApiUser, Long>
{
    @EntityGraph(attributePaths = "roles")
    Optional<ApiUser> findByEmail(String email);

    boolean existsByEmail(String email);
}
