package petdori.apiserver.domain.walklog.exception;

import lombok.Getter;

@Getter
public class WalkLogException extends RuntimeException {
    private final WalkLogErrorCode walkLogErrorCode;

    public WalkLogException(WalkLogErrorCode walkLogErrorCode) {
        super(walkLogErrorCode.getErrorMessage());
        this.walkLogErrorCode = walkLogErrorCode;
    }
}
