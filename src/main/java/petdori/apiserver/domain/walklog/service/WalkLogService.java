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
import petdori.apiserver.domain.walklog.repository.DogWalkLogRepository;
import petdori.apiserver.domain.walklog.repository.WalkLogRepository;
import petdori.apiserver.domain.walklog.repository.WalkLogRepository.RecentlyLogDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class WalkLogService {
    private final MemberRepository memberRepository;
    private final WalkLogRepository walkLogRepository;
    private final DogWalkLogRepository dogWalkLogRepository;

    public List<RecentlyLogDto> getWalkLogsForLast30days() {
        // 인증된 사용자이므로 이메일을 가져올 수 있다
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new MemberNotExistException(memberEmail));

        return walkLogRepository
                .findStartedTimeAndWalkedDistanceForLast30Days(member.getId());
    }

    public List<WalkLogSummaryResponseDto> getMonthlyWalkLogs(int year, int month) {
        // 인증된 사용자이므로 이메일을 가져올 수 있다
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new MemberNotExistException(memberEmail));

        return walkLogRepository.findByYearAndMonth(member, year, month);
    }

    public List<WalkLogSummaryResponseDto> getDailyWalkLogs(String walkedDate) {
        // 인증된 사용자이므로 이메일을 가져올 수 있다
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new MemberNotExistException(memberEmail));

        return walkLogRepository.findByMemberAndWalkedDate(member, LocalDate.parse(walkedDate));
    }

    public WalkLogDetailResponseDto getLogDetails(Long logId) {
        // 인증된 사용자이므로 이메일을 가져올 수 있다
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new MemberNotExistException(memberEmail));

        WalkLog walkLog = walkLogRepository.findByMemberAndId(member, logId).get();
        List<DogWalkLog> dogWalkLogs = dogWalkLogRepository.findByWalkLogId(logId);

        List<WalkedDogResponseDto> walkedDogs = new ArrayList<>();
        for (DogWalkLog dogWalkLog : dogWalkLogs) {
            walkedDogs.add(WalkedDogResponseDto.builder()
                    .dogName(dogWalkLog.getDog().getDogName())
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
