package com.sachzhong.sachzhonglicense.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 用来监听license的有效性的定时任务
 */
public class LicenseThread {

    public static void startLicenseThread(){
        LicenseRunnable licenseRunnable =new LicenseRunnable();
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(licenseRunnable,0,1, TimeUnit.SECONDS);
    }

}
