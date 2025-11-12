package org.example.backend.survey;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.backend.user.User;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "survey")
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public boolean isActive() {
        return isActive(LocalDateTime.now());
    }

    public boolean isActive(LocalDateTime now) {
        return (startTime == null || !now.isBefore(startTime)) &&
                (endTime == null || !now.isAfter(endTime));
    }
}