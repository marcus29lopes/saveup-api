package com.saveup.api.transaction.repository;

import com.saveup.api.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
            			SELECT tr
            			FROM Transaction tr
            			WHERE tr.wallet.id = :wallet_id
                        order by tr.createdAt DESC
            """)
    List<Transaction> findAllTransactionsDesc(
            @Param("wallet_id") Long wallet_id
    );

}