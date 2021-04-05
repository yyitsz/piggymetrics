package com.yyitsz.piggymetrics2.statistics.repository;

import com.yyitsz.piggymetrics2.statistics.domain.timeseries.DataPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataPointRepository extends JpaRepository<DataPoint, Long> {

    List<DataPoint> findByAccountName(String account);

}
