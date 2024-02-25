package com.bynder.lottery.repository;

import com.bynder.lottery.domain.Ballot;
import com.bynder.lottery.repository.entity.BallotEntity;
import com.bynder.lottery.repository.jpa.BallotJpaRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BallotRepository {

  private final BallotJpaRepository jpaRepository;

  public List<Ballot> saveAll(List<Ballot> ballots) {
    var result =
        jpaRepository.saveAll(ballots.stream().map(Ballot::toEntity).collect(Collectors.toList()));

    return result.stream().map(BallotEntity::toDomain).collect(Collectors.toList());
  }

  public List<Ballot> findAllByLotteryId(long lotteryId) {
    return jpaRepository.findAllByLotteryId(lotteryId).stream()
        .map(BallotEntity::toDomain)
        .collect(Collectors.toList());
  }
}
