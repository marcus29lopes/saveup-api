package com.saveup.api.transaction.repository;

import com.saveup.api.transaction.dto.ReportCategoriesResponse;
import com.saveup.api.transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
            SELECT tr
            FROM Transaction tr
            WHERE tr.wallet.id = :walletId
            """)
    Page<Transaction> findAllTransactionsDesc(
            @Param("walletId") Long walletId,
            Pageable pageable
    );

    @Query("""
    SELECT new com.saveup.api.transaction.dto.ReportCategoriesResponse(
        c.name,
        SUM(tr.amount)
    )
    FROM Transaction tr
    JOIN tr.categories c
    WHERE tr.wallet.id = :walletId
      AND tr.type = :type
      AND tr.createdAt >= :dtStart
      AND tr.createdAt < :dtEnd
    GROUP BY c.id, c.name
    ORDER BY SUM(tr.amount) DESC
    """)
    List<ReportCategoriesResponse> findSummaryTransactionsByType(
            @Param("walletId") Long walletId,
            @Param("type") Transaction.TransactionType type,
            @Param("dtStart") Instant dtStart,
            @Param("dtEnd") Instant dtEnd
    );
}