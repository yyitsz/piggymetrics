package com.yyitsz.piggymetrics2.statistics.repository;

import com.yyitsz.piggymetrics2.statistics.domain.timeseries.DataPoint;
import com.yyitsz.piggymetrics2.statistics.domain.timeseries.DataPointId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DataPointRepository extends JpaRepository<DataPoint, DataPointId> {

    List<DataPoint> findByIdAccount(String account);
}
