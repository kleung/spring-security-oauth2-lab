package com.test.authorizationserver.repository;

import com.test.authorizationserver.model.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long>  {
}
