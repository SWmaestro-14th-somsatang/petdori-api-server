package wooyoungsoo.authserver.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DogRegisterDto {
    private String ownerEmail;
    private String dogName;
    private String dogType;
    private String dogGender;
    private int dogAge;
}
