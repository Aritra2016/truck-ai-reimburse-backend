package com.aritra.truck_ai_reimburse.Domain;

import com.aritra.truck_ai_reimburse.enums.OcrStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Many expenses belong to one reimbursement
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reimbursement_id", nullable = false)
    private Reimbursement reimbursement;

    @Column(nullable = false)
    private String type;   // FUEL / FOOD / TOLL etc

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;   // INR

    @Column(nullable = false)
    private LocalDateTime expenseDate;

    @Column(nullable = false)
    private boolean approved = false;

    public OcrStatus getOcrStatus() {
        return ocrStatus;
    }

    public void setOcrStatus(OcrStatus ocrStatus) {
        this.ocrStatus = ocrStatus;
    }

    public LocalDateTime getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDateTime expenseDate) {
        this.expenseDate = expenseDate;
    }

    public Reimbursement getReimbursement() {
        return reimbursement;
    }

    public void setReimbursement(Reimbursement reimbursement) {
        this.reimbursement = reimbursement;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OcrStatus ocrStatus;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }



    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
