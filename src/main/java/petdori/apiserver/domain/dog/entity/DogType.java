package petdori.apiserver.domain.dog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import petdori.apiserver.global.common.BaseTimeEntity;

@SQLDelete(sql = "UPDATE dog_type SET deleted_date = NOW() WHERE id = ?")
@Getter
@Entity
public class DogType extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 45, unique = true)
    private String typeName;
}
