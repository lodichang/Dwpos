package com.dw.task;

import com.dw.Main;
import javafx.application.Platform;

import java.text.SimpleDateFormat;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by lodi on 2017/12/13.
 */
public class TimeTask {


    public static void changTime()
    {
        final Long[] currentTime = {System.currentTimeMillis()};
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    currentTime[0] = currentTime[0] + 1000L;
                    Main.timeLabel.setText(simpleDateFormat.format(currentTime[0]));
                });
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);

    }



}
