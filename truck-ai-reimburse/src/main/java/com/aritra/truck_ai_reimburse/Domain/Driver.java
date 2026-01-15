package com.aritra.truck_ai_reimburse.Domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false, unique = true)
    private String driverCode;
    @Column(nullable = false)
    private String driverType; // COMPANY / OWNER_OPERATOR
    private String phone;
    private String email;
    @Column(nullable = false)
    private boolean active = true;

}
