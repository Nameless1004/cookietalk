package com.sparta.cookietalk.cookie.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import com.sparta.cookietalk.amazon.AmazonS3Uploader;
import com.sparta.cookietalk.channel.entity.Channel;
import com.sparta.cookietalk.channel.repository.ChannelRepository;
import com.sparta.cookietalk.common.enums.ProcessStatus;
import com.sparta.cookietalk.common.enums.UserRole;
import com.sparta.cookietalk.common.exceptions.InvalidRequestException;
import com.sparta.cookietalk.cookie.dto.CookieRequest;
import com.sparta.cookietalk.cookie.dto.CookieResponse;
import com.sparta.cookietalk.cookie.dto.CookieResponse.Create;
import com.sparta.cookietalk.cookie.entity.Cookie;
import com.sparta.cookietalk.cookie.entity.UserRecentCookie;
import com.sparta.cookietalk.cookie.repository.CookieRepository;
import com.sparta.cookietalk.cookie.repository.UserRecentCookieRepository;
import com.sparta.cookietalk.security.AuthUser;
import com.sparta.cookietalk.series.repository.SeriesRepository;
import com.sparta.cookietalk.upload.UploadFileRepository;
import com.sparta.cookietalk.user.entity.User;
import com.sparta.cookietalk.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.RedisListCommands.Direction;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class CookieServiceTest {

    @Mock private CookieRepository cookieRepository;
    @Mock private ChannelRepository channelRepository;
    @Mock private CookieCreateFacade cookieCreateFacade;
    @Mock private RedisTemplate<String, Object> redisTemplate;
    @Mock private UserRecentCookieRepository userRecentCookieRepository;
    @Mock private UserRepository userRepository;
    @Mock private AmazonS3Uploader amazonS3Uploader;
    @Mock private UploadFileRepository uploadFileRepository;
    @Mock SeriesRepository seriesRepository;

    @InjectMocks
    private CookieService cookieService;

    @Nested
    class 쿠키_생성 {

        @Test
        public void 쿠키_생성_시_유저의_채널이_없으면_InvalidRequest_익셉션이_발생한다() throws Exception {
            // given
            CookieRequest.Create req = new CookieRequest.Create("title", "desc", 1L, 1L);
            CookieResponse.Create res = new Create(1L);
            given(cookieCreateFacade.createCookie(any(), any(), any(), any(), any())).willReturn(res);
            AuthUser authUser = new AuthUser(1L, "test@email.com", UserRole.ROLE_USER);
            // when
            Create cookie = cookieService.createCookie(authUser, req, null, null, null);

            // then
            assertThat(cookie).isNotNull();
            assertThat(cookie.cookieId()).isEqualTo(1L);
        }
    }

    @Nested
    class 쿠키_상세_조회 {

        @Test
        public void 쿠키_상세_조회_시_쿠키가_존재하지않으면_InvalidRequestException이_발생한다() throws Exception {
            // given
            AuthUser authUser = new AuthUser(1L, "test@email.com", UserRole.ROLE_USER);
            long cookieId = 1L;
            given(cookieRepository.findById(any())).willReturn(Optional.empty());

            // when then
            assertThatThrownBy(()-> cookieService.getCookie(authUser, cookieId )).isInstanceOf(InvalidRequestException.class).hasMessage("존재하지 않는 쿠키입니다.");
        }

        @Test
        public void 쿠키_상세_조회_시_유저가_존재하지않으면_NPE가_발생한다() throws Exception {
            // given
            AuthUser authUser = new AuthUser(1L, "test@email.com", UserRole.ROLE_USER);
            long cookieId = 1L;
            Channel channel =new Channel();
            Cookie cookie = new Cookie(channel, "title", "desc", ProcessStatus.SUCCESS, null, null, null);
            given(cookieRepository.findById(any())).willReturn(Optional.of(cookie));
            given(userRepository.findByIdOrElseThrow(any())).willReturn(null);

            // when then
            assertThatThrownBy(()-> cookieService.getCookie(authUser, cookieId )).isInstanceOf(NullPointerException.class);
        }

        public static class Default implements ListOperations<String, Object> {

            @Override
            public List<Object> range(String key, long start, long end) {
                return List.of();
            }

            @Override
            public void trim(String key, long start, long end) {

            }

            @Override
            public Long size(String key) {
                return 0L;
            }

            @Override
            public Long leftPush(String key, Object value) {
                return 0L;
            }

            @Override
            public Long leftPushAll(String key, Object... values) {
                return 0L;
            }

            @Override
            public Long leftPushAll(String key, Collection<Object> values) {
                return 0L;
            }

            @Override
            public Long leftPushIfPresent(String key, Object value) {
                return 0L;
            }

            @Override
            public Long leftPush(String key, Object pivot, Object value) {
                return 0L;
            }

            @Override
            public Long rightPush(String key, Object value) {
                return 0L;
            }

            @Override
            public Long rightPushAll(String key, Object... values) {
                return 0L;
            }

            @Override
            public Long rightPushAll(String key, Collection<Object> values) {
                return 0L;
            }

            @Override
            public Long rightPushIfPresent(String key, Object value) {
                return 0L;
            }

            @Override
            public Long rightPush(String key, Object pivot, Object value) {
                return 0L;
            }

            @Override
            public Object move(String sourceKey, Direction from, String destinationKey,
                Direction to) {
                return null;
            }

            @Override
            public Object move(String sourceKey, Direction from, String destinationKey,
                Direction to, long timeout, TimeUnit unit) {
                return null;
            }

            @Override
            public void set(String key, long index, Object value) {

            }

            @Override
            public Long remove(String key, long count, Object value) {
                return 0L;
            }

            @Override
            public Object index(String key, long index) {
                return null;
            }

            @Override
            public Long indexOf(String key, Object value) {
                return 0L;
            }

            @Override
            public Long lastIndexOf(String key, Object value) {
                return 0L;
            }

            @Override
            public Object leftPop(String key) {
                return null;
            }

            @Override
            public List<Object> leftPop(String key, long count) {
                return List.of();
            }

            @Override
            public Object leftPop(String key, long timeout, TimeUnit unit) {
                return null;
            }

            @Override
            public Object rightPop(String key) {
                return null;
            }

            @Override
            public List<Object> rightPop(String key, long count) {
                return List.of();
            }

            @Override
            public Object rightPop(String key, long timeout, TimeUnit unit) {
                return null;
            }

            @Override
            public Object rightPopAndLeftPush(String sourceKey, String destinationKey) {
                return null;
            }

            @Override
            public Object rightPopAndLeftPush(String sourceKey, String destinationKey, long timeout,
                TimeUnit unit) {
                return null;
            }

            @Override
            public RedisOperations<String, Object> getOperations() {
                return null;
            }
        }
        @Test
        public void 쿠키_상세_조회_시_조회수_1증가() throws Exception {
            // given
            AuthUser authUser = new AuthUser(1L, "test@email.com", UserRole.ROLE_USER);
            long cookieId = 1L;
            Channel channel =new Channel();
            User user = new User("name", "password", authUser.getEmail(), "nickname", UserRole.ROLE_USER);
            ReflectionTestUtils.setField(user, "id", authUser.getUserId());
            Cookie cookie = new Cookie(channel, "title", "desc", ProcessStatus.SUCCESS, null, null, null);

            ListOperations<String, Object> a = new Default();
            given(cookieRepository.findById(any())).willReturn(Optional.of(cookie));
            given(userRepository.findByIdOrElseThrow(any())).willReturn(user);
            given(cookieRepository.save(any())).willReturn(cookie);
            given(redisTemplate.opsForList()).willReturn(a);

            UserRecentCookie uc = new UserRecentCookie(user, cookie);

            given(userRecentCookieRepository.findByCookieId(any())).willReturn(Optional.empty());
            // when
            cookieService.getCookie(authUser, cookieId);

            //then
            assertThat(cookie.getCookieViews()).isEqualTo(1);
        }

        @Test
        public void 쿠키_상세_조회_시_쿠키가_있다면_시청한_시간_변경() throws Exception {
            // given
            AuthUser authUser = new AuthUser(1L, "test@email.com", UserRole.ROLE_USER);
            long cookieId = 1L;
            Channel channel =new Channel();
            User user = new User("name", "password", authUser.getEmail(), "nickname", UserRole.ROLE_USER);
            ReflectionTestUtils.setField(user, "id", authUser.getUserId());
            Cookie cookie = new Cookie(channel, "title", "desc", ProcessStatus.SUCCESS, null, null, null);

            ListOperations<String, Object> a = new Default();
            given(cookieRepository.findById(any())).willReturn(Optional.of(cookie));
            given(userRepository.findByIdOrElseThrow(any())).willReturn(user);
            given(cookieRepository.save(any())).willReturn(cookie);
            given(redisTemplate.opsForList()).willReturn(a);

            UserRecentCookie uc = new UserRecentCookie(user, cookie);
            ReflectionTestUtils.setField(uc, "viewAt", LocalDateTime.now().minusHours(1));
            String now = uc.getViewAt().toString();
            given(userRecentCookieRepository.findByCookieId(any())).willReturn(Optional.of(uc));

            // when
            cookieService.getCookie(authUser, cookieId);

            //then
            assertThat(cookie.getCookieViews()).isEqualTo(1);
            assertThat(uc.getViewAt().toString()).isNotEqualTo(now);
        }

        @Nested
        class 쿠키_유저아이디로_목록_조회 {
            
        }

    }
}