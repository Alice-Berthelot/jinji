package com.jinji.backend.repository;

import com.jinji.backend.model.projection.TeamSummary;
import com.jinji.backend.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    boolean existsByLabel(String label);

    List<TeamSummary> findAllByOrderByLabelAsc();

    List<Team> findAllByIdIn(Collection<Long> ids);

    List<Team> findByEmployees_Id(Long employeeId);
}