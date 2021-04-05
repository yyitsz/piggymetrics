package com.yyitsz.piggymetrics2.account.repository;

import com.yyitsz.piggymetrics2.account.domain.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

	Account findByName(String name);

}
