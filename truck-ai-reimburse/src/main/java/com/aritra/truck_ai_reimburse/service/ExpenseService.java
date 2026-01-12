package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.Domain.Expense;
import com.aritra.truck_ai_reimburse.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
//@RequiredArgsConstructor
@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense submitExpense(Expense expense) {
        expense.setApproved(false);
        return expenseRepository.save(expense);
    }

    public List<Expense> getExpensesByTrip(Long tripId) {
        return expenseRepository.findByTripId(tripId);
    }

    public Expense approveExpense(Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        expense.setApproved(true);
        return expenseRepository.save(expense);
    }
}
