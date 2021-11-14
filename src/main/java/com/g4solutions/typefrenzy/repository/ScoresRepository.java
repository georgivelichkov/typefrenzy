package com.g4solutions.typefrenzy.repository;

import com.g4solutions.typefrenzy.domain.Score;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ScoresRepository extends
    PagingAndSortingRepository<Score, String>,
    JpaSpecificationExecutor<Score> {

}
