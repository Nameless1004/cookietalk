//package com.sparta.cookietalk.upload;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//
//@SpringBootTest
//class TestServiceTest {
//    @Autowired
//    private TestService service;
//
//    @Autowired
//    private TestRepository testRepository;
//
//    @Test
//    @Rollback(false)
//    public void tdd() throws Exception {
//        // given
//            for(int i = 0; i< 200; ++i){
//                service.test(i).thenAccept((x)->{
//                    System.out.println(x.getUuid());
//                    long count = testRepository.count();
//                    System.out.println("count = " + count);
//                    testRepository.save(x);
//                });
//            }
//
//        Thread.sleep(5000);
//        // when
//        long count = testRepository.count();
//
//        assertEquals(200, count
//        );
//        // then
//    }
//}