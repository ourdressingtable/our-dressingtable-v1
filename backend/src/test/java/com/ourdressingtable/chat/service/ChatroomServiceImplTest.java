package com.ourdressingtable.chat.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ourdressingtable.auth.dto.CustomUserDetails;
import com.ourdressingtable.chat.domain.Chat;
import com.ourdressingtable.chat.domain.Chatroom;
import com.ourdressingtable.chat.domain.repository.ChatRepository;
import com.ourdressingtable.chat.domain.repository.ChatroomRepository;
import com.ourdressingtable.chat.dto.ChatroomResponse;
import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.security.WithCustomUser;
import com.ourdressingtable.common.util.TestDataFactory;
import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.Role;
import com.ourdressingtable.member.domain.Status;
import com.ourdressingtable.member.service.MemberService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("채팅방 테스트")
public class ChatroomServiceImplTest {

    @InjectMocks
    private ChatroomServiceImpl chatroomService;

    @Mock
    private ChatroomRepository chatroomRepository;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private SetOperations<String, String> setOperations;

    @BeforeEach
    void setUp() {
        // 사용자 정보 생성
        CustomUserDetails userDetails = CustomUserDetails.builder()
                .memberId(1L)
                .email("test@example.com")
                .password("password")
                .role(Role.ROLE_BASIC)
                .status(Status.ACTIVE)
                .build();

        // Authentication 객체 생성 및 SecurityContext에 주입
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);

        when(redisTemplate.opsForSet()).thenReturn(setOperations);
    }

    @Nested
    @DisplayName("1:1 채팅방 생성 테스트")
    class createOneToOneChatroomTest {

        @DisplayName("1:1 채팅방 생성 성공")
        @Test
        public void createdOneToOneChatroom_returnSuccess() {
            Member member = TestDataFactory.testMember(1L);
            Long targetId = 2L;
            Member targetMember = TestDataFactory.testMember(targetId);

            when(chatRepository.findOneToOneChatroom(member.getId(), targetId)).thenReturn(Optional.empty());

            Chatroom room = TestDataFactory.testChatroom(1L);
            given(chatroomRepository.save(any(Chatroom.class))).willAnswer( invocation -> {
                    Chatroom saved = invocation.getArgument(0);
                    ReflectionTestUtils.setField(saved, "id", 1L);
                    return saved;

            });

            given(memberService.getMemberEntityById(member.getId())).willReturn(member);
            given(memberService.getMemberEntityById(targetId)).willReturn(targetMember);

            Chat memberChat = TestDataFactory.testChat(1L,room,member);
            Chat targetChat = TestDataFactory.testChat(2L,room,targetMember);
            when(chatRepository.save(any(Chat.class))).thenReturn(
                    memberChat,targetChat
            );

            ChatroomResponse response = chatroomService.createOrGetOneToOneChatroom(targetId);

            assertEquals(1L, response.getId());
            verify(chatRepository,times(2)).save(any(Chat.class));
        }

        @DisplayName("1:1 채팅방 생성 실패 - 자신과 채팅 불가능")
        @Test
        public void createdOneToOneChatroom_returnError() {
            Member member = TestDataFactory.testMember(1L);

           assertThatThrownBy(() -> chatroomService.createOrGetOneToOneChatroom(1L))
                   .isInstanceOf(OurDressingTableException.class)
                   .hasMessageContaining(ErrorCode.NO_CHAT_WITH_MYSELF.getMessage());
        }


    }



}
