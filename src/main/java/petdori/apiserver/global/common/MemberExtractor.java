package petdori.apiserver.global.common;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import petdori.apiserver.domain.auth.entity.member.Member;
import petdori.apiserver.domain.auth.exception.member.MemberNotExistException;
import petdori.apiserver.domain.auth.repository.MemberRepository;

@RequiredArgsConstructor
@Component
public class MemberExtractor {
    private final MemberRepository memberRepository;

    public Member getAuthenticatedMember() {
        // 인증된 사용자이므로 이메일을 가져올 수 있다
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new MemberNotExistException(memberEmail));

        return member;
    }
}
