package com.aritra.truck_ai_reimburse.Domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String entityName;
    private String entityId;
    private String action; // CREATE, UPDATE, DELETE, PAYOUT
    @Column(columnDefinition = "TEXT")
    private String details;

    private String performedBy;
    private LocalDateTime performedAt;


}
