package petdori.apiserver.domain.walklog.exception;

public class WalkLogNotFoundException extends WalkLogException {
    public WalkLogNotFoundException() {
        super(WalkLogErrorCode.WALK_LOG_NOT_FOUND);
    }
}
