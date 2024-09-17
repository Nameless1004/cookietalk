package com.sparta.cookietalk.upload;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;

public class AsyncTest {

    @Test
    public void 예외처리_전파() {
        assertThrows(RuntimeException.class, () -> {
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(() -> {
                    throw new RuntimeException("e");
                })
                .handle((s, ex) -> {
                    if (ex != null) {
                        System.out.println(ex.getMessage() + "발생");
                        throw new RuntimeException();
                    }
                    return null;
                })
                .thenRun(() -> System.out.println("비동기 완료"));
            voidCompletableFuture.join();
        });
    }

    @Test
    public void 예외처리_성공() {
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(() -> {
                throw new RuntimeException("e");
            })
            .handle((s, ex) -> {
                if (ex != null) {
                    System.out.println(ex.getMessage() + "발생");
                }
                return null;
            })
            .thenRun(() -> System.out.println("비동기 완료"));

        voidCompletableFuture.join();
    }
}
