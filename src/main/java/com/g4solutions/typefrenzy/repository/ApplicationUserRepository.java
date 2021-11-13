package com.g4solutions.typefrenzy.repository;

import com.g4solutions.typefrenzy.domain.ApplicationUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ApplicationUserRepository extends
    PagingAndSortingRepository<ApplicationUser, String>,
    JpaSpecificationExecutor<ApplicationUser> {

  Optional<ApplicationUser> findByIdAndDeletedAtNull(String id);

  Optional<ApplicationUser> findByEmail(String email);
}
