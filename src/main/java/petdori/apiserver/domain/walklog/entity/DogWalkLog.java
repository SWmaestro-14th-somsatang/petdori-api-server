package petdori.apiserver.domain.walklog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import petdori.apiserver.domain.dog.entity.Dog;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE dog_walk_log SET deleted_date = NOW() WHERE id = ?")
@Where(clause = "deleted_date IS NULL")
@Builder
@Getter
@Setter
@Entity
public class DogWalkLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walk_log_id", nullable = false)
    private WalkLog walkLog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_id", nullable = false)
    private Dog dog;

    @Column(nullable = false)
    private Long burnedCalorie;
}
