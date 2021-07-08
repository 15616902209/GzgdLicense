package com.sachzhong.sachzhonglicense.thread;

import com.sachzhong.sachzhonglicense.constants.LicenseConstants;
import com.sachzhong.sachzhonglicense.service.IGenXml;
import com.sachzhong.sachzhonglicense.service.impl.GenXmlImpl;

import javax.xml.crypto.Data;

/**
 * @Author: SachZhong
 * @Description: 线程实现类
 * @Date Created in 2021/6/10 10:16
 */
public class LicenseRunnable implements Runnable{

    int count=0;
    boolean oneOpen = true;

    @Override
    public void run() {
        IGenXml iGenXml =new GenXmlImpl();
        try {
            boolean result = true;
            if (oneOpen){
                result = iGenXml.decryptXml(LicenseConstants.LICENSE_FILE_URL);
                System.out.println("对licnse的校验结果："+result);
                oneOpen = false;
                //如果校验失败，就结束程序
                if (!result){
                    System.exit(0);
                }
            }
            //校验时间合法性
            result =  iGenXml.checkDateTime(LicenseConstants.LICENSE_FILE_URL);
            System.out.println("对时间的校验结果："+result);
            count++;
            System.out.println("定时任务执行的次数："+count);
        }catch (Exception e){
            System.out.println("License定时任务错误："+e);
        }

    }
}
