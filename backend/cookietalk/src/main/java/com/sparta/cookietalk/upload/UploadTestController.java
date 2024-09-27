//package com.sparta.cookietalk.upload;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.MediaType;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//@Slf4j
//@CrossOrigin(origins = "*")  // 모든 도메인에서의 요청 허용
//@Controller
//public class UploadTestController {
//    @GetMapping("/test")
//    public String test(){
//        return "test";
//    }
//
//    @ResponseBody
//    @PostMapping("/api/trigger")
//    public String triggerNotification() {
//        return "Notification triggered";
//    }
//
//    @ResponseBody
//    @GetMapping(value = "/api/notification")
//    public SseEmitter sendNotification() {
//        SseEmitter emitter = new SseEmitter(0L); // 타임아웃 없이 설정
//
//        new Thread(() -> {
//            try {
//                // 1초마다 메시지를 10번 전송
//                for (int i = 10; i > 0; i--) {
//                    String message = "Time Left: " + i + " seconds";
//                    // 메시지 전송 및 데이터 포맷 확인
//                    emitter.send(SseEmitter.event()
//                        .name("notification")
//                        .data(message, MediaType.APPLICATION_JSON)
//                        .comment("Client connection notification")
//                    );  // 메시지 전송
//
//                    // 1초 대기
//                    Thread.sleep(1000);
//                }
//
//                // 10초 후에 최종 메시지 전송
//                emitter.send(SseEmitter.event()
//                    .name("notification")
//                    .data("Alarm: 10 seconds passed!")
//                );
//
//                // 연결 완료
//                emitter.complete();
//            } catch (IOException | InterruptedException e) {
//                emitter.completeWithError(e);
//            }
//        }).start();
//
//        return emitter;
//    }
//
//    @Async // Spring의 비동기 처리 기능 사용
//    public void handleEmitter(SseEmitter emitter) {
//        try {
//            // 1초마다 메시지를 10번 전송
//            for (int i = 10; i > 0; i--) {
//                String message = "Time Left: " + i + " seconds";
//                emitter.send(SseEmitter.event().name("notification").data(message));  // 올바른 메시지 전송
//                Thread.sleep(1000); // 1초 대기
//            }
//
//            // 10초 후에 최종 알림 전송
//            String finalMessage = "Alarm: 10 seconds passed!";
//            emitter.send(SseEmitter.event().name("notification").data(finalMessage));
//
//            // 연결 완료
//            emitter.complete();
//        } catch (IOException | InterruptedException e) {
//            // 오류 처리
//            emitter.completeWithError(e);
//        }
//    }
//}
