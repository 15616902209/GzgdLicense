package com.sachzhong.sachzhonglicense.service.impl;

import cn.hutool.core.codec.Base64;
import com.sachzhong.sachzhonglicense.constants.LicenseConstants;
import com.sachzhong.sachzhonglicense.encoded.RSACoder;
import com.sachzhong.sachzhonglicense.model.License;
import com.sachzhong.sachzhonglicense.service.IGenXml;
import com.sachzhong.sachzhonglicense.util.LicenseXmlUtil;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: SachZhong
 * @Description:
 * @Date Created in 2021/6/9 14:33
 */
public class GenXmlImpl implements IGenXml {

    @Override
    public void genBaseXml(String fileUrl) throws Exception {
        //初始化xml文件到指定地址
        License license = LicenseXmlUtil.initLicenseData();
        LicenseXmlUtil.writerToXml(license, fileUrl);
    }

    @Override
    public void encryptXml(String fileUrl) throws Exception {
        //读取xml 中的信息，转换成对象
        License license = LicenseXmlUtil.readFormXml(fileUrl);
        //更新ID,将当前时间戳加上uuid的绝对值作为id，基本上能保证唯一
        String id =String.valueOf(System.currentTimeMillis() +Math.abs(UUID.randomUUID().getLeastSignificantBits()));
        license.setId(id);
        //生成密钥对，并生成对应的文件
        Map<String, Object> keyMap = RSACoder.generateKey(LicenseConstants.PUBLIC_KEY_FILE_URL,LicenseConstants.PRIVATE_KEY_FILE_URL);
        //私钥
        byte[] privateKey = RSACoder.getPrivateKey(keyMap);
        //获取要加密的字符串
        byte[] sourceByte = license.getSourceStr().getBytes(StandardCharsets.UTF_8);
        //用私钥加密
        byte[] byteSign = RSACoder.encryptByPrivateKey(sourceByte, privateKey);
        String encrytStr = Base64.encode(byteSign);
        //设置得到对象中去
        license.setEncryptedStr(encrytStr);
        //将对象写回到xml中去
        LicenseXmlUtil.writerToXml(license, fileUrl);
    }

    @Override
    public boolean decryptXml(String fileUrl) throws Exception {
        //读取xml 中的信息，转换成对象
        License license = LicenseXmlUtil.readFormXml(fileUrl);
        byte[] privateKeyFile = RSACoder.loadPublicKey(LicenseConstants.PUBLIC_KEY_FILE_URL);
        byte[] decodeStr2 = RSACoder.decryptByPublicKey(Base64.decode(license.getEncryptedStr()), privateKeyFile);
        System.out.println("用公钥解密后的数据："+new String(decodeStr2));
        String sourceStr = license.getSourceStr();
        return sourceStr.equals(new String(decodeStr2));
    }
}
