package com.gzgd.gzgdlicense.service.impl;

import cn.hutool.core.codec.Base64;
import com.gzgd.gzgdlicense.encoded.RSACoder;
import com.gzgd.gzgdlicense.model.License;
import com.gzgd.gzgdlicense.service.IGenXml;
import com.gzgd.gzgdlicense.util.LicenseXmlUtil;

import java.util.Map;

/**
 * @Author: SachZhong
 * @Description:
 * @Date Created in 2021/6/9 14:33
 */
public class GenXmlImpl implements IGenXml {

    /**
     * 公钥名称
     */
    public final static String PUBLIC_KEY_NAME = "publicKey";
    /**
     * 私钥名称
     */
    public final static String PRIVATE_KEY_NAME = "privateKey";
    /**
     * 公钥文件生成路径
     */
    public final static String PUBLIC_KEY_FILE_URL = "resources/" + PUBLIC_KEY_NAME;
    /**
     * 私钥文件生成路径
     */
    public final static String PRIVATE_KEY_FILE_URL = "resources/" + PRIVATE_KEY_NAME;

    @Override
    public void genBaseXml(String fileUrl) throws Exception {
        //初始化xml文件到指定地址
        License license =LicenseXmlUtil.initLicenseData();
        LicenseXmlUtil.writerToXml(license,fileUrl);
    }

    @Override
    public void encryptXml(String fileUrl) throws Exception {
        //读取xml 中的信息，转换成对象
        License license = LicenseXmlUtil.readFormXml(fileUrl);
        //生成密钥对，并生成对应的文件
        Map<String, Object> keyMap = RSACoder.generateKey(PUBLIC_KEY_FILE_URL,PRIVATE_KEY_FILE_URL);
        //私钥
        byte[] privateKey = RSACoder.getPrivateKey(keyMap);
        //获取要加密的字符串
        byte[] sourceByte = license.getSourceStr().getBytes();
        System.out.println("sourceByte:" + sourceByte.length);
        //用私钥加密
        byte[] byteSign = RSACoder.encryptByPrivateKey(sourceByte,privateKey);
        String encrytStr = Base64.encode(byteSign);
        System.out.println("使用私钥加密得到的数据：" + encrytStr);
        //设置得到对象中去
        license.setEncryptedStr(encrytStr);
        //将对象写回到xml中去
        LicenseXmlUtil.writerToXml(license,fileUrl);
    }
}
