package com.hakspace.repository;
import com.hakspace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:login) OR LOWER(u.username) = LOWER(:login)")
    Optional<User> findByEmailOrUsername(@Param("login") String login);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    List<User> findByRole(User.Role role);

    @Query("SELECT u FROM User u WHERE u.role = 'USER' AND " +
           "(:specialization IS NULL OR :specialization = '' OR LOWER(u.specialization) = LOWER(:specialization)) AND " +
           "(:query IS NULL OR :query = '' OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<User> searchCommunityMembers(@Param("specialization") String specialization, @Param("query") String query);
}