package com.ryszka.imageRestApi.service.serviceV2.writeService;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class GoogleUploadTaskProcessor {
    private List<GoogleUploadTask> tasks;

    public GoogleUploadTaskProcessor(List<GoogleUploadTask> googleUploadTasks) {
        this.tasks = googleUploadTasks;
    }

    public void processTasks() {
        CountDownLatch latch = new CountDownLatch(tasks.size());
        AtomicInteger atomicInteger = new AtomicInteger(this.tasks.size());
        for (int i = 0; i < this.tasks.size(); i++) {
            final int index = i;
            new Thread(() -> {
                this.tasks.get(index).run();
                latch.countDown();
            }).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
