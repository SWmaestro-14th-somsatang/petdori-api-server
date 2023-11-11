package petdori.apiserver.domain.walklog.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
public class WalkLogDetailResponseDto {
    @JsonProperty("walking_route_file_url")
    private String walkingRouteFileUrl;

    @JsonProperty("walking_image_url")
    private String walkingImageUrl;

    @JsonProperty("started_time")
    private LocalDateTime startedTime;

    @JsonProperty("walking_time")
    private LocalTime walkingTime;

    @JsonProperty("walked_distance")
    private BigDecimal walkedDistance;

    @JsonProperty("walked_dogs")
    private List<WalkedDogResponseDto> walkedDogs;
}
