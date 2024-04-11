package Capstone.VoQal.global.auth.service;

import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.domain.member.repository.MemberRepository;
import Capstone.VoQal.global.auth.dto.OAuth2AttributeDTO;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.enums.Provider;
import Capstone.VoQal.global.enums.Role;
import Capstone.VoQal.global.error.exception.BusinessException;
import Capstone.VoQal.global.error.exception.CustomAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        // 기본 OAuth2UserService 객체 생성
//        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
//
//        // OAuth2UserService를 사용하여 OAuth2User 정보를 가져온다.
//        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
//
//        // 클라이언트 등록 ID(google, naver, kakao)와 사용자 이름 속성을 가져온다.
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//        String userNameAttributeName = userRequest.getClientRegistration()
//                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
//
//
//        // OAuth2UserService를 사용하여 가져온 OAuth2User 정보로 OAuth2Attribute 객체를 만든다.
//        OAuth2Attribute oAuth2Attribute =
//                OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
//
//        // OAuth2Attribute의 속성값들을 Map으로 반환 받는다.
//        Map<String, Object> memberAttribute = oAuth2Attribute.convertToMap();
//
//        // 사용자 email(또는 id) 정보를 가져온다.
//        String email = (String) memberAttribute.get("email");
//        // 이메일로 가입된 회원인지 조회한다.
//        Optional<Member> findMember = memberRepository.findByEmail(email);
//
//        if (findMember.isEmpty()) {
//            // 회원이 존재하지 않을경우, memberAttribute의 exist 값을 false로 넣어준다.
//            memberAttribute.put("exist", false);
//            // 회원의 권한(회원이 존재하지 않으므로 기본권한인 ROLE_USER를 넣어준다), 회원속성, 속성이름을 이용해 DefaultOAuth2User 객체를 생성해 반환한다.
//            return new DefaultOAuth2User(
//                    Collections.singleton(new SimpleGrantedAuthority("ROLE_STUDENT")),
//                    memberAttribute, "email");
//        }
//
//        // 회원이 존재할경우, memberAttribute의 exist 값을 true로 넣어준다.
//        memberAttribute.put("exist", true);
//        // 회원의 권한과, 회원속성, 속성이름을 이용해 DefaultOAuth2User 객체를 생성해 반환한다.
//        return new DefaultOAuth2User(
//                Collections.singleton(new SimpleGrantedAuthority("ROLE_".concat(String.valueOf(findMember.get().getRole())))),
//                memberAttribute, "email");
//
//    }
@Override
public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = service.loadUser(userRequest);

    Map<String, Object> originAttributes = oAuth2User.getAttributes();

    String registrationId = userRequest.getClientRegistration().getRegistrationId();

    OAuth2AttributeDTO oAuth2Attribute = OAuth2AttributeDTO.of(registrationId, originAttributes);

    return processOAuth2Login(oAuth2Attribute);
}

    private boolean checkSameEmailDifferentProvider(Member member, OAuth2AttributeDTO oAuth2Attribute) {
        return member.getEmail().equals(oAuth2Attribute.getEmail()) && member.getProvider().name().equals(oAuth2Attribute.getProvider());
    }

    public OAuth2User processOAuth2Login(OAuth2AttributeDTO oAuth2Attribute) {
        Map<String, Object> memberAttribute = oAuth2Attribute.convertToMap();

        Optional<Member> findMember = memberRepository.findByEmail(oAuth2Attribute.getEmail());
        Member member;
        if (findMember.isEmpty()) {
            //todo
            // 로그인 폼까지는 띄웠고 로그인 시도 했을 때 없는 회원인 경우에 가입하는 로직 짜야함
            member = Member.createGuest(Provider.fromValue(oAuth2Attribute.getProvider()), oAuth2Attribute.getProviderId(), oAuth2Attribute.getEmail());
            memberRepository.save(member);
        } else {
            member = findMember.get();
            if (checkSameEmailDifferentProvider(member, oAuth2Attribute)) {
                throw new CustomAuthenticationException("이미 존재하는 회원입니다, " + member.getProvider().getKoreanName() + "로 다시 로그인해주세요");
            }
        }

        Optional<Member> findMemberId = memberRepository.findByMemberId(member.getId());

        memberAttribute.put("registered", findMemberId.isPresent());

        memberAttribute.put("id", member.getId());

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())), memberAttribute, "id");
    }

}