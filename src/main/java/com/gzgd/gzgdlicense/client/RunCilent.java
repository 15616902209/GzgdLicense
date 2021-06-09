package com.gzgd.gzgdlicense.client;

import com.gzgd.gzgdlicense.service.IGenXml;
import com.gzgd.gzgdlicense.service.impl.GenXmlImpl;

/**
 * @Author: SachZhong
 * @Description:
 * @Date Created in 2021/6/9 11:23
 */
public class RunCilent {
    /**
     * xml的文件路径
     */
    public final static String LICENSE_FILE_URL = "resources/license.xml";

    public static void main(String[] args) throws Exception {

        IGenXml iGenXml =new GenXmlImpl();
        //iGenXml.genBaseXml(LICENSE_FILE_URL);
        //对xml文件中的信息进行加密
        //iGenXml.encryptXml(LICENSE_FILE_URL);
        //对xml文件信息进行机密，并校验其信息的正确性
        boolean result = iGenXml.decryptXml(LICENSE_FILE_URL);
        System.out.println("校验结果："+result);
    }
}
