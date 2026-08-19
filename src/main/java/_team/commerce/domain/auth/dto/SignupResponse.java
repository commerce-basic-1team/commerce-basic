package _team.commerce.domain.auth.dto;

import _team.commerce.domain.auth.entity.Member;

public record SignupResponse(

        Long id,
        String email,
        String name,
        String phone
) {

    public static SignupResponse from(Member member) {
        return new SignupResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getPhone()
        );
    }
}
