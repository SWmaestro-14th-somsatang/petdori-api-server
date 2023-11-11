package petdori.apiserver.domain.walklog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import petdori.apiserver.domain.auth.entity.member.Member;
import petdori.apiserver.domain.auth.exception.member.MemberNotExistException;
import petdori.apiserver.domain.auth.repository.MemberRepository;
import petdori.apiserver.domain.walklog.dto.response.WalkLogDetailResponseDto;
import petdori.apiserver.domain.walklog.dto.response.WalkLogSummaryResponseDto;
import petdori.apiserver.domain.walklog.dto.response.WalkedDogResponseDto;
import petdori.apiserver.domain.walklog.entity.DogWalkLog;
import petdori.apiserver.domain.walklog.entity.WalkLog;
import petdori.apiserver.domain.walklog.exception.WalkLogNotFoundException;
import petdori.apiserver.domain.walklog.repository.DogWalkLogRepository;
import petdori.apiserver.domain.walklog.repository.WalkLogRepository;
import petdori.apiserver.domain.walklog.repository.WalkLogRepository.RecentlyLogDto;
import petdori.apiserver.global.common.MemberExtractor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class WalkLogService {
    private final MemberExtractor memberExtractor;
    private final WalkLogRepository walkLogRepository;
    private final DogWalkLogRepository dogWalkLogRepository;

    public List<RecentlyLogDto> getWalkLogsForLast30days() {
        Member member = memberExtractor.getAuthenticatedMember();
        return walkLogRepository
                .findStartedTimeAndWalkedDistanceForLast30Days(member.getId());
    }

    public List<WalkLogSummaryResponseDto> getMonthlyWalkLogs(int year, int month) {
        Member member = memberExtractor.getAuthenticatedMember();
        return walkLogRepository.findByYearAndMonth(member, year, month);
    }

    public List<WalkLogSummaryResponseDto> getDailyWalkLogs(String walkedDate) {
        Member member = memberExtractor.getAuthenticatedMember();
        return walkLogRepository.findByMemberAndWalkedDate(member, LocalDate.parse(walkedDate));
    }

    public WalkLogDetailResponseDto getLogDetails(Long logId) {
        Member member = memberExtractor.getAuthenticatedMember();

        WalkLog walkLog = walkLogRepository
                .findByMemberAndId(member, logId).orElseThrow(
                        () -> new WalkLogNotFoundException());
        List<DogWalkLog> dogWalkLogs = dogWalkLogRepository.findByWalkLogId(logId);

        List<WalkedDogResponseDto> walkedDogs = new ArrayList<>();
        for (DogWalkLog dogWalkLog : dogWalkLogs) {
            walkedDogs.add(WalkedDogResponseDto.builder()
                    .dogName(dogWalkLog.getDog().getDogName())
                    .dogImageUrl(dogWalkLog.getDog().getDogImageUrl())
                    .burnedCalorie(dogWalkLog.getBurnedCalorie())
                    .build());
        }

        return WalkLogDetailResponseDto.builder()
                .walkingRouteFileUrl(walkLog.getWalkingRouteFileUrl())
                .walkingImageUrl(walkLog.getWalkingImageUrl())
                .startedTime(walkLog.getStartedTime())
                .walkingTime(walkLog.getWalkingTime())
                .walkedDistance(walkLog.getWalkedDistance())
                .walkedDogs(walkedDogs)
                .build();
    }
}
