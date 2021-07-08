package com.sachzhong.sachzhonglicense.constants;

import java.util.UUID;
/**
 * @Author: SachZhong
 * @Description: 常量实体类
 * @Date Created in 2021/6/9 19:31
 */
public class LicenseConstants {
    /**
     * 设置license的名称
     */
    public static String LICENSE_NAME = "License";
    /**
     * 设置密钥失效的天数，单位为天。例如要想密钥一年后失效就设置，365
     */
    public static int INVALID_DAY = 365;
    /**
     * 非对称密钥算法
     */
    public static final String KEY_ALGORITHM = "RSA";
    /**
     * 密钥长度，DH算法的默认密钥长度是1024
     * 密钥长度必须是64的倍数，在512到65536位之间
     */
    public static final int KEY_SIZE = 4092;
    /**
     * 公钥 的键
     */
    public static final String PUBLIC_KEY = String.valueOf(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    /**
     * 私钥 的键
     */
    public static final String PRIVATE_KEY = String.valueOf(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
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
    /**
     * xml的文件路径
     */
    public final static String LICENSE_FILE_URL = "resources/license.xml";
}
