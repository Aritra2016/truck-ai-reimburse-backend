package com.aritra.truck_ai_reimburse.controller;

import com.aritra.truck_ai_reimburse.Domain.Expense;
import com.aritra.truck_ai_reimburse.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/create")
    public Expense submitExpense(@RequestBody Expense expense) {
        return expenseService.submitExpense(expense);
    }

    @GetMapping("/trip/{tripId}")
    public List<Expense> getExpensesByTrip(@PathVariable Long tripId) {
        return expenseService.getExpensesByTrip(tripId);
    }

    @PutMapping("/{expenseId}/approve")
    public Expense approveExpense(@PathVariable Long expenseId) {
        return expenseService.approveExpense(expenseId);
    }

}
