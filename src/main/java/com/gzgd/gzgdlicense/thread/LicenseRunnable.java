package com.gzgd.gzgdlicense.thread;

import com.gzgd.gzgdlicense.constants.LicenseConstants;
import com.gzgd.gzgdlicense.service.IGenXml;
import com.gzgd.gzgdlicense.service.impl.GenXmlImpl;

public class LicenseRunnable implements Runnable{

    int count=0;

    @Override
    public void run() {
        IGenXml iGenXml =new GenXmlImpl();
        try {
            if (count==0){
                boolean result = iGenXml.decryptXml(LicenseConstants.LICENSE_FILE_URL);
                System.out.println("对licnse的校验结果："+result);
            }
            count++;
            System.out.println("定时任务执行的次数："+count);
        }catch (Exception e){
            System.out.println("License定时任务错误："+e);
        }

    }
}
