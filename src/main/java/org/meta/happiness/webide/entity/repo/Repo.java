package org.meta.happiness.webide.entity.repo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.meta.happiness.webide.entity.user.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Repo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    private User creator;

    @CreationTimestamp
    private LocalDateTime createAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @Enumerated(EnumType.STRING)
    private Language programmingLanguage;

    private String name;

    private String efsAccessPoint;
}
