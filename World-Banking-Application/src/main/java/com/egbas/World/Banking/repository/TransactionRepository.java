package com.egbas.World.Banking.repository;

import com.egbas.World.Banking.domain.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository <Transaction, String>{

}
