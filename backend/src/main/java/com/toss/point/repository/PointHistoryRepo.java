package com.toss.point.repository;

import com.toss.point.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepo extends JpaRepository<PointHistory, Long> {

}
