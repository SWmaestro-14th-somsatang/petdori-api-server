package petdori.apiserver.domain.walklog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import petdori.apiserver.domain.walklog.entity.DogWalkLog;

import java.util.List;

public interface DogWalkLogRepository extends JpaRepository<DogWalkLog, Long> {
    List<DogWalkLog> findByWalkLogId(Long walkLogId);
}
