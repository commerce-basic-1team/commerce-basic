package _team.commerce.domain.member.dto;

import _team.commerce.domain.member.entity.Member;

public record MemberResponse(
        Long id,
        String name,
        String email,
        String phone
) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPhone()
        );
    }
}