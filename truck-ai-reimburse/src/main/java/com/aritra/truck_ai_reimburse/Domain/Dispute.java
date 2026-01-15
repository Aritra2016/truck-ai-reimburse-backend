package com.aritra.truck_ai_reimburse.Domain;


import com.aritra.truck_ai_reimburse.enums.DisputeStatus;
import com.aritra.truck_ai_reimburse.enums.DisputeType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "disputes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dispute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DisputeType disputeType; // PAY, EXPENSE, DETENTION

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DisputeStatus status; // OPEN, IN_PROGRESS, RESOLVED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    private LocalDateTime createdAt;

}
