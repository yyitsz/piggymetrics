package com.yyitsz.piggymetrics2.statistics.repository;

import com.yyitsz.piggymetrics2.statistics.domain.timeseries.DataPoint;
import com.yyitsz.piggymetrics2.statistics.domain.timeseries.DataPointId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class DataPointRepositoryImpl
        implements DataPointRepository {
    private ConcurrentHashMap<DataPointId, DataPoint> store = new ConcurrentHashMap<>();

    @Override
    public <S extends DataPoint> S save(S entity) {
        store.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public <S extends DataPoint> Iterable<S> saveAll(Iterable<S> entities) {
        for (DataPoint ac : entities) {
            save(ac);
        }
        return entities;
    }

    @Override
    public Optional<DataPoint> findById(DataPointId s) {
        DataPoint ac = store.get(s);
        return Optional.ofNullable(ac);
    }

    @Override
    public boolean existsById(DataPointId s) {
        return store.containsKey(s);
    }

    @Override
    public Iterable<DataPoint> findAll() {
        return store.values();
    }

    @Override
    public Iterable<DataPoint> findAllById(Iterable<DataPointId> strings) {
        List<DataPoint> acList = new ArrayList<>();
        for (DataPointId id : strings) {
            DataPoint ac = store.get(id);
            if (ac != null) {
                acList.add(ac);
            }
        }
        return acList;
    }

    @Override
    public long count() {
        return store.size();
    }

    @Override
    public void deleteById(DataPointId s) {
        store.remove(s);
    }

    @Override
    public void delete(DataPoint entity) {
        store.remove(entity.getId());
    }

    @Override
    public void deleteAll(Iterable<? extends DataPoint> entities) {
        for (DataPoint ac : entities) {
            delete(ac);
        }
    }

    @Override
    public void deleteAll() {
        store.clear();
    }

    @Override
    public List<DataPoint> findByIdAccount(String account) {
        List<DataPoint> list = new ArrayList<>();
        for(DataPoint dataPoint : store.values()) {
            if(dataPoint.getId().getAccount().equals(account)) {
                list.add(dataPoint);
            }
        }
        return list;
    }
}
