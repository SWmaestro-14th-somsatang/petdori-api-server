package petdori.apiserver.domain.dog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import petdori.apiserver.domain.dog.dto.request.DogRegisterRequestDto;
import petdori.apiserver.domain.dog.dto.response.DogDetailResponseDto;
import petdori.apiserver.domain.dog.dto.response.MyDogResponseDto;
import petdori.apiserver.domain.dog.entity.Dog;
import petdori.apiserver.domain.dog.entity.DogGender;
import petdori.apiserver.domain.dog.entity.DogType;
import petdori.apiserver.domain.auth.entity.member.Member;
import petdori.apiserver.domain.dog.exception.DogNotExistException;
import petdori.apiserver.domain.dog.exception.DogOwnerNotMatchedException;
import petdori.apiserver.domain.dog.exception.DogTypeNotExistException;
import petdori.apiserver.domain.auth.exception.member.MemberNotExistException;
import petdori.apiserver.domain.dog.repository.DogRepository;
import petdori.apiserver.domain.dog.repository.DogTypeRepository;
import petdori.apiserver.domain.auth.repository.MemberRepository;
import petdori.apiserver.global.common.MemberExtractor;
import petdori.apiserver.global.common.S3Uploader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class DogService {
    private final MemberExtractor memberExtractor;
    private final DogTypeRepository dogTypeRepository;
    private final DogRepository dogRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public void registerDog(MultipartFile dogImage, DogRegisterRequestDto dogRegisterRequestDto) {
        Member owner = memberExtractor.getAuthenticatedMember();
        DogType dogType = dogTypeRepository.findByTypeName(dogRegisterRequestDto.getDogType())
                .orElseThrow(() -> new DogTypeNotExistException(dogRegisterRequestDto.getDogType()));
        // 클라이언트가 반려견 이미지를 첨부하지 않았을 경우에 대한 처리
        String dogImageUrl = dogImage == null || dogImage.isEmpty() ? null : s3Uploader.uploadDogImage(dogImage);

        Dog dog = Dog.from(owner, dogType, dogImageUrl, dogRegisterRequestDto);
        dogRepository.save(dog);
    }

    @Transactional(readOnly = true)
    public List<String> getAllDogTypeNames() {
        List<String> dogTypeNames = new ArrayList<>();
        List<DogType> dogTypes = dogTypeRepository.findAll();

        for (DogType dogType : dogTypes) {
            dogTypeNames.add(dogType.getTypeName());
        }

        return dogTypeNames;
    }

    @Transactional(readOnly = true)
    public List<DogDetailResponseDto> getMyAllDogs() {
        List<DogDetailResponseDto> myDogs = new ArrayList<>();
        Member owner = memberExtractor.getAuthenticatedMember();

        for (Dog dog : owner.getDogs()) {
            myDogs.add(DogDetailResponseDto.builder()
                    .dogId(dog.getId())
                    .dogName(dog.getDogName())
                    .dogTypeName(dog.getDogType().getTypeName())
                    .dogGender(dog.getDogGender().name())
                    .isNeutered(dog.isNeutered())
                    .dogWeight(dog.getDogWeight())
                    .dogBirth(dog.getDogBirth())
                    .dogImageUrl(dog.getDogImageUrl())
                    .build());
        }

        return myDogs;
    }


    @Transactional
    public void deleteDog(Long dogId) {
        Dog dog = getMyDog(dogId);
        dogRepository.delete(dog);
    }

    private Dog getMyDog(Long dogId) {
        Dog dog = dogRepository.findById(dogId)
                .orElseThrow(DogNotExistException::new);
        validateDogOwner(dog);
        return dog;
    }

    private void validateDogOwner(Dog dog) {
        // 현재 인증된 사용자와 반려견의 주인이 일치하는지 확인
        Member ownerFromEmail = memberExtractor.getAuthenticatedMember();
        Member ownerFromDog = dog.getOwner();

        if (!ownerFromEmail.equals(ownerFromDog)) {
            throw new DogOwnerNotMatchedException();
        }
    }

    @Transactional
    public void updateDog(Long dogId, MultipartFile dogImage, DogRegisterRequestDto dogUpdateRequestDto) {
        Dog dog = getMyDog(dogId);

        if (dogImage != null && !dogImage.isEmpty()) {
            String dogImageUrl = s3Uploader.uploadDogImage(dogImage);
            dog.setDogImageUrl(dogImageUrl);
        }
        if (dogUpdateRequestDto.getDogName() != null) {
            dog.setDogName(dogUpdateRequestDto.getDogName());
        }
        if (dogUpdateRequestDto.getDogType() != null) {
            DogType dogType = dogTypeRepository.findByTypeName(dogUpdateRequestDto.getDogType())
                    .orElseThrow(() -> new DogTypeNotExistException(dogUpdateRequestDto.getDogType()));
            dog.setDogType(dogType);
        }
        if (dogUpdateRequestDto.getDogGender() != null) {
            dog.setDogGender(DogGender.getDogGenderByGenderName( dogUpdateRequestDto.getDogGender()));
        }
        if (dogUpdateRequestDto.getIsNeutered() != null) {
            dog.setNeutered(dogUpdateRequestDto.getIsNeutered());
        }
        if (dogUpdateRequestDto.getDogWeight() != null) {
            dog.setDogWeight(dogUpdateRequestDto.getDogWeight());
        }
        if (dogUpdateRequestDto.getDogBirth() != null) {
            dog.setDogBirth(LocalDate.parse(dogUpdateRequestDto.getDogBirth()));
        }

        dogRepository.save(dog);
    }
}
