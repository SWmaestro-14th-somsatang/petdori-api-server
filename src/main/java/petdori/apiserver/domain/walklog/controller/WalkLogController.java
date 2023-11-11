package petdori.apiserver.domain.walklog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import petdori.apiserver.domain.walklog.dto.response.WalkLogDetailResponseDto;
import petdori.apiserver.domain.walklog.dto.response.WalkLogSummaryResponseDto;
import petdori.apiserver.domain.walklog.repository.WalkLogRepository.RecentlyLogDto;
import petdori.apiserver.domain.walklog.service.WalkLogService;
import petdori.apiserver.global.common.BaseResponse;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/walk-log")
public class WalkLogController {
    private final WalkLogService walkLogService;

    @GetMapping("/recently-logs")
    public BaseResponse<List<RecentlyLogDto>> getRecentlyWalkLogs() {
        List<RecentlyLogDto> recentlyWalkLogs = walkLogService
                .getWalkLogsForLast30days();
        return BaseResponse.createSuccessResponse(recentlyWalkLogs);
    }

    @GetMapping("/monthly-logs")
    public BaseResponse<List<WalkLogSummaryResponseDto>> getMonthlyWalkLogs(@RequestParam(value = "year") int year,
                                                                        @RequestParam(value = "month") int month) {
        List<WalkLogSummaryResponseDto> monthlyWalkLogs = walkLogService.getMonthlyWalkLogs(year, month);
        return BaseResponse.createSuccessResponse(monthlyWalkLogs);
    }

    @GetMapping("/daily-logs")
    public BaseResponse<List<WalkLogSummaryResponseDto>> getDailyWalkLogs(@RequestParam(value = "date") String walkedDate) {
        List<WalkLogSummaryResponseDto> monthlyWalkLogs = walkLogService.getDailyWalkLogs(walkedDate);
        return BaseResponse.createSuccessResponse(monthlyWalkLogs);
    }

    @GetMapping("/{logId}")
    public BaseResponse<WalkLogDetailResponseDto> getWalkLogDetail(@PathVariable Long logId) {
        WalkLogDetailResponseDto walkLogDetail = walkLogService.getLogDetails(logId);
        return BaseResponse.createSuccessResponse(walkLogDetail);
    }
}
