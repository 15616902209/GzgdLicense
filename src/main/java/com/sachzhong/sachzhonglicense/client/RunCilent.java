package com.sachzhong.sachzhonglicense.client;

import com.sachzhong.sachzhonglicense.constants.LicenseConstants;
import com.sachzhong.sachzhonglicense.service.IGenXml;
import com.sachzhong.sachzhonglicense.service.impl.GenXmlImpl;
import com.sachzhong.sachzhonglicense.thread.LicenseThread;

import java.util.Scanner;

/**
 * @Author: SachZhong
 * @Description:
 * @Date Created in 2021/6/9 11:23
 */
public class RunCilent {
    public static void main(String[] args) throws Exception {
        IGenXml iGenXml =new GenXmlImpl();
        Scanner scanner =new Scanner(System.in);
        System.out.println("-----------------License加密程序----------------");
        System.out.println("用法：先执行0生成基本的xml模板，然后可以自行更改xml中的信息。" +
                "\n更改后，再启动执行1，对xml信息进行加密，并生成密钥信息和文件。" +
                "\n可以通过执行2校验xml信息的合法性");
        System.out.println("-----------------------------------------------");
        System.out.println("输入：0 生成xml模板");
        System.out.println("输入：1 对象xml信息进行加密");
        System.out.println("输入：2 校验xml信息的正确性");
        System.out.println("其他输入：退出程序\n");
        int input = scanner.nextInt();
        if (input==0){
            //生成基础模板
            iGenXml.genBaseXml(LicenseConstants.LICENSE_FILE_URL);
        }else if (input==1){
            //对xml文件中的信息进行加密
            iGenXml.encryptXml(LicenseConstants.LICENSE_FILE_URL);
        }else if (input==2){
            //对xml文件信息进行解密，并校验其信息的正确性
            boolean result = iGenXml.decryptXml(LicenseConstants.LICENSE_FILE_URL);
            System.out.println("校验结果："+result);
        }else {
            System.out.println("-------------------结束程序----------------");
            System.exit(0);
        }
        //开启定时检查license线程
        //LicenseThread.startLicenseThread();

    }
}
