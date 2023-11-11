package petdori.apiserver.domain.walklog.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WalkedDogResponseDto {
    @JsonProperty("dog_name")
    private String dogName;
    @JsonProperty("dog_image_url")
    private String dogImageUrl;
    @JsonProperty("burned_calorie")
    private Long burnedCalorie;
}
