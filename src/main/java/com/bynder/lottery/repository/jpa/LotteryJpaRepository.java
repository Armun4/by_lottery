package com.bynder.lottery.repository.jpa;

import com.bynder.lottery.entity.LotteryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotteryJpaRepository extends JpaRepository<LotteryEntity, Long> {}
