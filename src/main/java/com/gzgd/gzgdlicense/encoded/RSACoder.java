package com.gzgd.gzgdlicense.encoded;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import com.gzgd.gzgdlicense.constants.LicenseConstants;

/**
 * @Author: SachZhong
 * @Description:
 * @Date Created in 2021/6/9 11:31
 */
public class RSACoder {

    /**
     * 初始化密钥对
     *
     * @return Map 甲方密钥的Map
     */
    public static Map<String, Object> initKey() throws Exception {
        //实例化密钥生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(LicenseConstants.KEY_ALGORITHM);
        //初始化密钥生成器
        keyPairGenerator.initialize(LicenseConstants.KEY_SIZE);

        //生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //甲方公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //System.out.println("系数：" + publicKey.getModulus() + " 加密指数：" + publicKey.getPublicExponent());
        //甲方私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //System.out.println("系数：" + privateKey.getModulus() + " 解密指数：" + privateKey.getPrivateExponent());
        //将密钥存储在map中
        Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put(LicenseConstants.PUBLIC_KEY, publicKey);
        keyMap.put(LicenseConstants.PRIVATE_KEY, privateKey);
        saveKey(keyPair, LicenseConstants.PUBLIC_KEY_FILE_URL, LicenseConstants.PRIVATE_KEY_FILE_URL);
        return keyMap;
    }

    /**
     * 初始化密钥对
     *
     * @return Map 甲方密钥的Map
     */
    public static  Map<String, Object> generateKey(String publicKeyFileUrl, String privateKeyFileUrl) throws Exception {
        //实例化密钥生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(LicenseConstants.KEY_ALGORITHM);
        //初始化密钥生成器
        keyPairGenerator.initialize(LicenseConstants.KEY_SIZE);
        //生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //将密钥存储在map中
        Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put(LicenseConstants.PUBLIC_KEY, keyPair.getPublic());
        keyMap.put(LicenseConstants.PRIVATE_KEY, keyPair.getPrivate());
        saveKey(keyPair, publicKeyFileUrl, privateKeyFileUrl);
        return keyMap;
    }

    /**
     * 生成公钥和私钥
     *
     * @param keyPair
     * @throws Exception
     */
    protected static void saveKey(KeyPair keyPair, String publicKeyFileUrl, String privateKeyFileUrl) throws Exception {
        FileOutputStream osPublicKey = new FileOutputStream(publicKeyFileUrl);
        osPublicKey.write(keyPair.getPublic().getEncoded());
        osPublicKey.flush();
        osPublicKey.close();

        FileOutputStream osPrivateKey = new FileOutputStream(privateKeyFileUrl);
        osPrivateKey.write(keyPair.getPrivate().getEncoded());
        osPrivateKey.close();
    }

    /**
     * 加载公钥
     *
     * @return
     * @throws Exception
     */
    public static byte[] loadPublicKey() throws Exception {
        File publicKeyFile = new File(LicenseConstants.PUBLIC_KEY_FILE_URL);
        byte[] keyArr = FileUtil.readBytes(publicKeyFile);
        return keyArr;
    }

    /**
     * 加载公钥
     *
     * @return
     * @throws Exception
     */
    public static byte[] loadPublicKey(String publicKeyFileUrl) throws Exception {
        File publicKeyFile = new File(publicKeyFileUrl);
        byte[] keyArr = FileUtil.readBytes(publicKeyFile);
        return keyArr;
    }

    /**
     * 加载私钥
     *
     * @return
     * @throws Exception
     */
    protected static byte[] loadPrivateKey() throws Exception {
        File privateKeyFile = new File(LicenseConstants.PRIVATE_KEY_FILE_URL);
        byte[] keyArr = FileUtil.readBytes(privateKeyFile);
        return keyArr;
    }
    /**
     * 加载私钥
     *
     * @return
     * @throws Exception
     */
    protected static byte[] loadPrivateKey(String privateKeyFileUrl) throws Exception {
        File privateKeyFile = new File(privateKeyFileUrl);
        byte[] keyArr = FileUtil.readBytes(privateKeyFile);
        return keyArr;
    }

    /**
     * 私钥加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[] 加密数据
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {

        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(LicenseConstants.KEY_ALGORITHM);
        //生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[] 加密数据
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {

        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(LicenseConstants.KEY_ALGORITHM);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        //产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

        //数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    /**
     * 私钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(LicenseConstants.KEY_ALGORITHM);
        //生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {

        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(LicenseConstants.KEY_ALGORITHM);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        //产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        //数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    /**
     * 取得私钥
     *
     * @param keyMap 密钥map
     * @return byte[] 私钥
     */
    public static byte[] getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(LicenseConstants.PRIVATE_KEY);
        return key.getEncoded();
    }

    /**
     * 取得公钥
     *
     * @param keyMap 密钥map
     * @return byte[] 公钥
     */
    public static byte[] getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(LicenseConstants.PUBLIC_KEY);
        return key.getEncoded();
    }


    /**
     * 测试运行函数
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        //初始化密钥
        //生成密钥对
        Map<String, Object> keyMap = RSACoder.initKey();
        //公钥
        byte[] publicKey = RSACoder.getPublicKey(keyMap);
        //私钥
        byte[] privateKey = RSACoder.getPrivateKey(keyMap);
        System.out.println("公钥：" + Base64.encode(publicKey));
        System.out.println("私钥：" + Base64.encode(privateKey));

        byte[] publicKeyFile = loadPublicKey();
        System.out.println("从文件解析得到公钥：" + Base64.encode(publicKeyFile));
        byte[] privateKeyFile = loadPrivateKey();
        System.out.println("从文件解析得到私钥：" + Base64.encode(privateKeyFile));
        System.out.println();

        TreeMap<String, String> params = new TreeMap<>();
        params.put("systemId", "cpms");
        params.put("expire", "2026-05-30 23:59:59");

        //构造数据
        Iterator<String> iter = params.keySet().iterator();
        StringBuffer sb = new StringBuffer();
        while (iter.hasNext()) {
            String fieldKey = iter.next();
            sb.append("&").append(fieldKey).append("=").append(params.get(fieldKey));
        }
        sb.deleteCharAt(0);
        //用公钥签名 私钥解密
        String needSignStr = sb.toString();
        System.out.println("==原始内容==: " + needSignStr);
        System.out.println();
        byte[] byteSign = RSACoder.encryptByPublicKey(needSignStr.getBytes(), publicKey);
        String encrytStr = Base64.encode(byteSign);
        System.out.println("使用公钥加密得到的数据：" + encrytStr);
        System.out.println();
        byte[] decodeStr = RSACoder.decryptByPrivateKey(Base64.decode(encrytStr), privateKeyFile);
        System.out.println("使用文件的私钥解密后的数据：" + new String(decodeStr));


        //用私钥签名 公钥解密
        byte[] byteSign2 = RSACoder.encryptByPrivateKey(needSignStr.getBytes(), privateKey);
        String encrytStr2 = Base64.encode(byteSign2);
        System.out.println("使用私钥加密得到的数据：" + encrytStr2);
        byte[] decodeStr2 = RSACoder.decryptByPublicKey(Base64.decode(encrytStr2), publicKey);
        System.out.println("使用文件的私钥解密后的数据：" + new String(decodeStr2));

    }
}