package _team.commerce.domain.member.controller;

import _team.commerce.domain.member.dto.MemberResponse;
import _team.commerce.domain.member.service.MemberService;
import _team.commerce.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponse>> getMyInfo(
            Authentication authentication
    ) {
        Long memberId = (Long) authentication.getPrincipal();

        MemberResponse response = memberService.getMyInfo(memberId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}