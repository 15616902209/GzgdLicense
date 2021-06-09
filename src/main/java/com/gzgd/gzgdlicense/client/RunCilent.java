package com.gzgd.gzgdlicense.client;

import com.gzgd.gzgdlicense.constants.LicenseConstants;
import com.gzgd.gzgdlicense.service.IGenXml;
import com.gzgd.gzgdlicense.service.impl.GenXmlImpl;
import com.gzgd.gzgdlicense.thread.LicenseThread;

/**
 * @Author: SachZhong
 * @Description:
 * @Date Created in 2021/6/9 11:23
 */
public class RunCilent {
    public static void main(String[] args) throws Exception {

        //testLicense();

        //开启定时检查license线程
        LicenseThread.startLicenseThread();

    }

    public static void testLicense() throws Exception{
        IGenXml iGenXml =new GenXmlImpl();
        //iGenXml.genBaseXml(LICENSE_FILE_URL);
        //对xml文件中的信息进行加密
        //iGenXml.encryptXml(LICENSE_FILE_URL);
        //对xml文件信息进行机密，并校验其信息的正确性
        boolean result = iGenXml.decryptXml(LicenseConstants.LICENSE_FILE_URL);
        System.out.println("校验结果："+result);
    }

}
