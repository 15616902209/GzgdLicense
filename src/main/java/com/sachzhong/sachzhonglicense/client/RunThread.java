package com.sachzhong.sachzhonglicense.client;

import com.sachzhong.sachzhonglicense.thread.LicenseThread;

/**
 * @Author SachZhong
 * @Description
 * @Date 9:43 2021/7/8
 **/
public class RunThread {

    public static void main(String[] args) {
        //开启定时检查license线程
        LicenseThread.startLicenseThread();
    }
}
