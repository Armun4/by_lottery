package com.bynder.lottery.repository.jpa;

import com.bynder.lottery.entity.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantJpaRepository extends JpaRepository<ParticipantEntity, Long> {}
