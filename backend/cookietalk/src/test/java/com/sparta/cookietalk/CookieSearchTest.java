package com.sparta.cookietalk;

import com.sparta.cookietalk.category.entity.Category;
import com.sparta.cookietalk.category.entity.CookieCategory;
import com.sparta.cookietalk.category.repository.CategoryRepository;
import com.sparta.cookietalk.category.repository.CookieCategoryRepository;
import com.sparta.cookietalk.common.dto.Response.Page;
import com.sparta.cookietalk.common.dto.Response.Slice;
import com.sparta.cookietalk.common.enums.ProcessStatus;
import com.sparta.cookietalk.common.enums.UploadStatus;
import com.sparta.cookietalk.common.enums.UploadType;
import com.sparta.cookietalk.common.enums.UserRole;
import com.sparta.cookietalk.cookie.dto.CookieResponse.Detail;
import com.sparta.cookietalk.cookie.dto.CookieResponse.List;
import com.sparta.cookietalk.cookie.dto.CookieSearch;
import com.sparta.cookietalk.cookie.entity.Cookie;
import com.sparta.cookietalk.cookie.repository.CookieRepository;
import com.sparta.cookietalk.upload.UploadFile;
import com.sparta.cookietalk.upload.UploadFileRepository;
import com.sparta.cookietalk.user.entity.User;
import com.sparta.cookietalk.user.repository.UserRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CookieSearchTest {

    @Autowired
    private CookieRepository cookieRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CookieCategoryRepository cookieCategoryRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UploadFileRepository uploadFileRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void createCookie() throws Exception {
        // given
        User user = new User("username", "password", "email", "nickname", UserRole.ROLE_USER);
        user = userRepository.saveAndFlush(user);
        Category category = new Category("개발");
        category = categoryRepository.saveAndFlush(category);

        Category category2 = new Category("인문");
        category2 = categoryRepository.saveAndFlush(category2);


        for(int i = 0; i < 100; ++i){
            String t = "title_" + (i + 1);
            UploadFile newUf = new UploadFile(UploadType.VIDEO, UploadStatus.WAITING, "", "");
            UploadFile uploadFile = uploadFileRepository.saveAndFlush(newUf);
            Cookie cookie = new Cookie(user.getChannel(),t, "d", ProcessStatus.SUCCESS, uploadFile, uploadFile, null);
            Cookie save = cookieRepository.save(cookie);
            CookieCategory cc = null;
            if(Math.random() < 0.5){
                cc = new CookieCategory(save, category);
            } else {
                cc = new CookieCategory(save, category2);
            }
            cookieCategoryRepository.saveAndFlush(cc);
        }
    }

    @Test
    public void z() throws Exception {
        // given
        Detail cookieDetails = cookieRepository.getCookieDetails(15L);
        System.out.println("cookieDetails.toString() = " + cookieDetails.toString());

        Slice<List> sliceByCategoryId = cookieRepository.getSliceByCategoryId(30,
            LocalDateTime.parse("2024-10-06T01:29:20.609303"), CookieSearch.builder()
                .categoryId(9L)
                .build());

        System.out.println("sliceByCategoryId.toString() = " + sliceByCategoryId.toString());
        // when

        // then
    }

    @Test
    public void 키워드로찾기() throws Exception {
        // given
        Page<List> listPage = cookieRepository.searchCookieListByKeyword(PageRequest.of(0, 30),
            CookieSearch.builder()
                .keyword("21")
                .build());

        System.out.println("listPage.toString() = " + listPage.toString());
        // when

        // then
    }
}
