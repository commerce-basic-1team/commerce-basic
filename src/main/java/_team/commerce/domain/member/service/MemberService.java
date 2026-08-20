package _team.commerce.domain.member.service;

import _team.commerce.domain.member.dto.MemberResponse;
import _team.commerce.domain.member.entity.Member;
import _team.commerce.domain.member.repository.MemberRepository;
import _team.commerce.global.exception.CustomException;
import _team.commerce.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse getMyInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return MemberResponse.from(member);
    }
}