package com.sachzhong.sachzhonglicense.service.impl;

import cn.hutool.core.codec.Base64;
import com.sachzhong.sachzhonglicense.constants.LicenseConstants;
import com.sachzhong.sachzhonglicense.encoded.RSACoder;
import com.sachzhong.sachzhonglicense.model.License;
import com.sachzhong.sachzhonglicense.service.IGenXml;
import com.sachzhong.sachzhonglicense.util.DateTimeUtil;
import com.sachzhong.sachzhonglicense.util.LicenseXmlUtil;
import java.nio.charset.StandardCharsets;
import java.util.Date;
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
        License license = LicenseXmlUtil.initLicenseXml();
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
        System.out.println("私钥加密后的字符:"+encrytStr);
        //设置得到对象中去
        license.setSign(encrytStr);
        //将对象写回到xml中去
        LicenseXmlUtil.writerToXml(license, fileUrl);
    }

    @Override
    public boolean decryptXml(String fileUrl) throws Exception {
        //读取xml 中的信息，转换成对象
        License license = LicenseXmlUtil.readFormXml(fileUrl);
        byte[] privateKeyFile = RSACoder.loadPublicKey(LicenseConstants.PUBLIC_KEY_FILE_URL);
        byte[] decodeStr2 = RSACoder.decryptByPublicKey(Base64.decode(license.getSign()), privateKeyFile);
        System.out.println("用公钥解密后的数据："+new String(decodeStr2));
        String sourceStr = license.getSourceStr();
        return sourceStr.equals(new String(decodeStr2));
    }

    /**
     * /校验时间合法性
     * @return 校验结果
     */
    @Override
    public boolean checkDateTime(String fileUrl) throws Exception {
        //当license剩余6个月发送短信提示，剩余2到6个月段，剩余天数为10倍数时触发警告，剩余1个月时，剩余天数为3的倍数时发送短信
        //读取xml 中的信息，转换成对象
        License license = LicenseXmlUtil.readFormXml(fileUrl);
        //将xml中的时间转为日期格式
        Date endDateTime = DateTimeUtil.parseDateSecondFormat(license.getEndTime());
        Date nowDate = new Date();

        //如果结束日期小于当前日期，说明过期了
        if (endDateTime.compareTo(nowDate) < 0){
            return false;
        }

        //当license剩余6个月发送短信提示
       if(DateTimeUtil.leftMonthToSix(nowDate,endDateTime)){
                //TODO 发送提示短信

       }else if (DateTimeUtil.leftMonthTwoAndSix(nowDate,endDateTime)){
           //剩余2到6个月段，剩余天数为10倍数时触发警告
           if (DateTimeUtil.leftDayTenTimes(nowDate,endDateTime)){
                //TODO 发送提示短信
           }
       }else if(DateTimeUtil.leftMonthOne(nowDate,endDateTime)){
           //剩余1个月时，剩余天数为3的倍数时发送短信
           if (DateTimeUtil.leftDayThreeTimes(nowDate,endDateTime)){
                //TODO 发送提示短信
           }
       }
        return true;
    }
}
