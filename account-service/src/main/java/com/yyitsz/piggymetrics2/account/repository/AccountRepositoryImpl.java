package com.yyitsz.piggymetrics2.account.repository;

import com.yyitsz.piggymetrics2.account.domain.Account;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccountRepositoryImpl
        implements AccountRepository {
    private ConcurrentHashMap<String, Account> store = new ConcurrentHashMap<>();

    @Override
    public Account findByName(String name) {
        return store.get(name);
    }

    @Override
    public <S extends Account> S save(S entity) {
        store.put(entity.getName(), entity);
        return entity;
    }

    @Override
    public <S extends Account> Iterable<S> saveAll(Iterable<S> entities) {
        for (Account ac : entities) {
            save(ac);
        }
        return entities;
    }

    @Override
    public Optional<Account> findById(String s) {
        Account ac = store.get(s);
        return Optional.ofNullable(ac);
    }

    @Override
    public boolean existsById(String s) {
        return store.containsKey(s);
    }

    @Override
    public Iterable<Account> findAll() {
        return store.values();
    }

    @Override
    public Iterable<Account> findAllById(Iterable<String> strings) {
        List<Account> acList = new ArrayList<>();
        for (String id : strings) {
            Account ac = store.get(id);
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
    public void deleteById(String s) {
        store.remove(s);
    }

    @Override
    public void delete(Account entity) {
        store.remove(entity.getName());
    }

    @Override
    public void deleteAll(Iterable<? extends Account> entities) {
        for (Account ac : entities) {
            delete(ac);
        }
    }

    @Override
    public void deleteAll() {
        store.clear();
    }
}
