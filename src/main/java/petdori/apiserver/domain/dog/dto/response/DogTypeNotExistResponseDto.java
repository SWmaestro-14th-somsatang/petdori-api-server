package petdori.apiserver.domain.dog.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DogTypeNotExistResponseDto {
    @JsonProperty("type_name")
    private String typeName;
}
