package com.aritra.truck_ai_reimburse.service;

import com.aritra.truck_ai_reimburse.Domain.Destination;
import com.aritra.truck_ai_reimburse.Domain.Reimbursement;
import com.aritra.truck_ai_reimburse.enums.DestinationStatus;
import com.aritra.truck_ai_reimburse.enums.ReimbursementStatus;
import com.aritra.truck_ai_reimburse.exception.BusinessException;
import com.aritra.truck_ai_reimburse.repository.ExpenseRepository;
import com.aritra.truck_ai_reimburse.repository.ReimbursementRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ReimbursementService {

    private final ReimbursementRepository repository;
    private final DestinationService destinationService;
    private final ExpenseRepository expenseRepository;

    public ReimbursementService(
            ReimbursementRepository repository,
            DestinationService destinationService,
            ExpenseRepository expenseRepository
    ) {
        this.repository = repository;
        this.destinationService = destinationService;
        this.expenseRepository = expenseRepository;
    }

    /**
     * Called automatically when POD is VERIFIED
     */
    public Reimbursement openReimbursement(Long tripId, Long destinationId) {

        Destination destination = destinationService.getCurrentDestination(tripId);

        if (!destination.getId().equals(destinationId)
                || destination.getStatus() != DestinationStatus.VERIFIED) {
            throw new BusinessException("Reimbursement allowed only after POD verification");
        }

        return repository.findByTripIdAndDestinationId(tripId, destinationId)
                .orElseGet(() -> {
                    Reimbursement r = new Reimbursement();
                    r.setTripId(tripId);
                    r.setDestinationId(destinationId);
                    r.setStatus(ReimbursementStatus.DRAFT);
                    r.setTotalAmount(BigDecimal.ZERO);
                    r.setCreatedAt(LocalDateTime.now());
                    return repository.save(r);
                });
    }

    /**
     * Recalculate total after every expense OCR
     */
    public void recalculateTotal(Long reimbursementId) {

        Reimbursement r = getById(reimbursementId);

        BigDecimal total = expenseRepository
                .sumAmountByReimbursementId(reimbursementId);

        r.setTotalAmount(total != null ? total : BigDecimal.ZERO);
        repository.save(r);
    }

    /**
     * Driver confirms reimbursement
     */
    public Reimbursement confirm(Long reimbursementId) {

        Reimbursement r = getById(reimbursementId);

        if (r.getStatus() != ReimbursementStatus.DRAFT) {
            throw new BusinessException("Reimbursement already confirmed");
        }

        if (r.getTotalAmount() == null
                || r.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("No expenses found for reimbursement");
        }

        r.setStatus(ReimbursementStatus.READY_FOR_SETTLEMENT);
        r.setConfirmedAt(LocalDateTime.now());

        return repository.save(r);
    }

    public Reimbursement getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new BusinessException("Reimbursement not found")
                );
    }

}
