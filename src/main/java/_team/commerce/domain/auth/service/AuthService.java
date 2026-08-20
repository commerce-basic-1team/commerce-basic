package _team.commerce.domain.auth.service;

import _team.commerce.domain.auth.dto.AuthResponse;
import _team.commerce.domain.auth.dto.LoginRequest;
import _team.commerce.domain.auth.dto.SignupRequest;
import _team.commerce.domain.auth.dto.SignupResponse;
import _team.commerce.domain.member.entity.Member;
import _team.commerce.domain.member.repository.MemberRepository;
import _team.commerce.global.exception.CustomException;
import _team.commerce.global.exception.ErrorCode;
import _team.commerce.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        validateEmail(request.email());

        String encodedPassword = passwordEncoder.encode(request.password());

        Member member = Member.create(
                request.email(),
                encodedPassword,
                request.name(),
                request.phone()
        );

        memberRepository.save(member);

        return SignupResponse.from(member);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(
                request.password(),
                member.getPassword()
        )) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }

        String token = jwtProvider.createToken(member.getId());

        AuthResponse.MemberInfo memberInfo =
                new AuthResponse.MemberInfo(
                        member.getId(),
                        member.getName(),
                        member.getEmail(),
                        member.getPhone()
                );

        return new AuthResponse(token, memberInfo);
    }

    private void validateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
    }
}