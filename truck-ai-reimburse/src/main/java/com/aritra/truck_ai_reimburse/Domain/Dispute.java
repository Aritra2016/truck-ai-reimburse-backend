package com.aritra.truck_ai_reimburse.Domain;


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

    private String disputeType; // PAY, EXPENSE, DETENTION

    @Column(columnDefinition = "TEXT")
    private String reason;

    private String status; // OPEN, IN_PROGRESS, RESOLVED

    @ManyToOne(fetch = FetchType.LAZY)
    private Driver driver;

    private LocalDateTime createdAt;
}
