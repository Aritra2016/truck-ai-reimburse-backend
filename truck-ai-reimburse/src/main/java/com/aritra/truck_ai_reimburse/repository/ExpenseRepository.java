package com.aritra.truck_ai_reimburse.repository;

import com.aritra.truck_ai_reimburse.Domain.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {

    // 1️⃣ All expenses under a reimbursement (MOST USED)
    List<Expense> findByReimbursement_Id(Long reimbursementId);

    // 2️⃣ Approved / unapproved expenses (admin / audit)
    List<Expense> findByApproved(boolean approved);

    // 3️⃣ TOTAL amount for reimbursement (FIXED)
    @Query("""
           SELECT COALESCE(SUM(e.amount), 0)
           FROM Expense e
           WHERE e.reimbursement.id = :reimbursementId
           """)
    BigDecimal sumAmountByReimbursementId(
            @Param("reimbursementId") Long reimbursementId
    );
}
