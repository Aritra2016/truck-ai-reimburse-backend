package com.aritra.truck_ai_reimburse.Domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pay_rules")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ruleName;

    private String ruleType; // PER_MILE, FLAT_RATE, PERCENTAGE

    private Double rateValue;

    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public Double getRateValue() {
        return rateValue;
    }

    public void setRateValue(Double rateValue) {
        this.rateValue = rateValue;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
