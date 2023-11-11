package petdori.apiserver.domain.walklog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum WalkLogErrorCode {
    WALK_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 기록을 찾을 수 없습니다");

    private HttpStatus httpStatus;
    private String errorMessage;
}
