package com.gzgd.gzgdlicense.util;

import cn.hutool.core.codec.Base64;
import com.gzgd.gzgdlicense.encoded.RSACoder;
import com.gzgd.gzgdlicense.model.License;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * @Author: SachZhong
 * @Description:
 * @Date Created in 2021/6/9 9:52
 */
public class LicenseXmlUtil {
    /**
     * xml的文件路径
     */
    public final static String LICENSE_FILE_URL = "resources/license.xml";

    /**
     * 测试运行函数
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        writerToXml(initLicenseData(), LICENSE_FILE_URL);
        readFormXml(LICENSE_FILE_URL);
    }

    /**
     * 初始化机密对象
     *
     * @return
     * @throws Exception
     */
    public static License initLicenseData() throws Exception {
        License license = new License();
        //将当前时间戳加上uuid的绝对值作为id，基本上能保证唯一
        String id = String.valueOf(System.currentTimeMillis() + Math.abs(UUID.randomUUID().getLeastSignificantBits()));
        license.setId(id);
        license.setName("sachzhong");

        //获取机器的IP
        InetAddress address = InetAddress.getLocalHost();
        license.setIp(address.getHostAddress());

        //生成密钥对
        Map<String, Object> keyMap = RSACoder.initKey();
        //公钥
        byte[] publicKey = RSACoder.getPublicKey(keyMap);
        //私钥
        byte[] privateKey = RSACoder.getPrivateKey(keyMap);

        //获取要加密的字符串
        byte[] sourceByte = license.getSourceStr().getBytes(StandardCharsets.UTF_8);
        System.out.println("sourceByte:" + sourceByte.length);

        //用私钥签名 公钥解密
        byte[] byteSign = RSACoder.encryptByPrivateKey(sourceByte, privateKey);
        String encrytStr = Base64.encode(byteSign);
        System.out.println("使用私钥加密得到的数据：" + encrytStr);
        license.setEncryptedStr(encrytStr);

        byte[] decodeStr = RSACoder.decryptByPublicKey(Base64.decode(encrytStr), publicKey);
        System.out.println("使用文件的私钥解密后的数据：" + new String(decodeStr));

        return license;
    }

    /**
     * 从xml文件在读取信息到对象
     *
     * @param fileUrl
     * @return
     * @throws Exception
     */
    public static License readFormXml(String fileUrl) throws Exception {
        File file = new File(fileUrl);
        SAXReader reader = new SAXReader();
        // 读取文件作为文档
        Document doc = reader.read(file);
        // 获取文档的根元素
        Element root = doc.getRootElement();
        // 根据跟元素找到全部的子节点
        Iterator<Element> iter = root.elementIterator();

        License license = new License();
        //获取实体类的所有属性，返回Field数组
        Field[] field = license.getClass().getDeclaredFields();

        while (iter.hasNext()) {
            Element name = iter.next();
            //System.out.println(name.getName() + ":" + name.getText());
            //遍历所有属性
            for (Field item : field) {
                //获取属性的名字
                String fieldName = item.getName();
                //通过反射将xml文件中的属性设置到对象中去
                if (name.getName().equals(fieldName)) {
                    //将属性的首字符大写，方便构造get，set方法
                    String tempName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    //System.out.println("方法名：" + tempName);
                    Method m = license.getClass().getDeclaredMethod("set" + tempName, String.class);
                    m.setAccessible(true);
                    //调用set方法设置属性值
                    m.invoke(license, name.getText());
                }
            }
        }
        System.out.println("从xml文件中读取到的信息：" + license.toString());
        return license;
    }

    /**
     * 写入对象到xml文件中去
     *
     * @param license
     * @param fileUrl
     * @throws Exception
     */
    public static void writerToXml(License license, String fileUrl) throws Exception {
        System.out.println("要写入到xml中的信息：" + license.toString());
        Document document = DocumentHelper.createDocument();
        // 创建元素并设置关系
        Element config = document.addElement("config");
        //获取实体类的所有属性，返回Field数组
        Field[] field = license.getClass().getDeclaredFields();
        //遍历所有属性
        for (Field item : field) {
            //获取属性的名字
            String name = item.getName();
            Element elementName = config.addElement(name);
            //将属性的首字符大写，方便构造get，set方法
            String tempName = name.substring(0, 1).toUpperCase() + name.substring(1);
            //类里的值
            String value;
            Method m = license.getClass().getMethod("get" + tempName);
            //调用getter方法获取属性值
            value = String.valueOf(m.invoke(license));
            if (value != null) {
                elementName.setText(value);
            }
        }
        // 创建格式化输出器
        OutputFormat of = OutputFormat.createPrettyPrint();
        of.setEncoding("utf-8");
        // 输出到文件
        File fileOut = new File(fileUrl);
        XMLWriter writer;
        writer = new XMLWriter(new FileOutputStream(fileOut.getAbsolutePath()), of);
        // 写出
        writer.write(document);
        writer.flush();
        writer.close();
    }

}
