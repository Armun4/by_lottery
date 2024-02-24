package com.bynder.lottery.repository.jpa;

import com.bynder.lottery.repository.entity.BallotEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BallotJpaRepository extends JpaRepository<BallotEntity, Long> {

  List<BallotEntity> findAllByLotteryId(long lotteryId);
}
