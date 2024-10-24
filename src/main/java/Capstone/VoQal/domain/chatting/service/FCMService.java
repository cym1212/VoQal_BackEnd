package Capstone.VoQal.domain.chatting.service;

import Capstone.VoQal.domain.chatting.dto.FCMTokenSaveRequestDTO;
import Capstone.VoQal.domain.chatting.dto.NotificationRequestDTO;
import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.domain.member.repository.Member.MemberRepository;
import Capstone.VoQal.domain.member.service.MemberService;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.error.exception.BusinessException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FCMService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Transactional
    public void saveFcmToken(FCMTokenSaveRequestDTO fcmTokenSaveRequestDTO) {
        Optional<Member> existingToken = memberRepository.findByIdAndFcmToken(Long.valueOf(fcmTokenSaveRequestDTO.getUserId()), fcmTokenSaveRequestDTO.getFcmToken());
        if (existingToken.isPresent()){
            memberRepository.updateFcmToken(Long.valueOf(fcmTokenSaveRequestDTO.getUserId()), fcmTokenSaveRequestDTO.getFcmToken());
        } else{
            Member member = memberService.getMemberById(Long.valueOf(fcmTokenSaveRequestDTO.getUserId()));
            member.saveFcmToken(fcmTokenSaveRequestDTO.getFcmToken());
            memberRepository.save(member);

        }
    }

    @Transactional
    public void sendNotification(NotificationRequestDTO notificationRequestDTO) {
        Member member = memberService.getMemberById(Long.valueOf(notificationRequestDTO.getUserId()));

        Message message = Message.builder()
                .setToken(member.getFcmToken())
                .putData("title", notificationRequestDTO.getTitle())
                .putData("message", notificationRequestDTO.getMessage())
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            throw new BusinessException(ErrorCode.FAIL_TO_SEND_PUSH_NOTIFICATION);
        }
    }


}
