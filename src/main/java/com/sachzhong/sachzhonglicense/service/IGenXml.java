package com.sachzhong.sachzhonglicense.service;

/**
 * @Author: SachZhong
 * @Description: rsa 生成密钥 服务类
 * @Date Created in 2021/6/9 14:29
 */
public interface IGenXml {
    /**
     * 生成基础的xml文件
     * @param fileUrl xml文件地址
     * @throws Exception
     */
    void genBaseXml(String fileUrl) throws Exception;
    /**
     * 对xml的信息进行rsa加密，覆盖原来的xml，并生成序列号，公钥，和私钥
     * @param fileUrl  xml文件地址
     * @throws Exception
     */
    void encryptXml(String fileUrl) throws Exception;
    /**
     * 对xml进行解密，读取公钥文件，并匹配序列号，
     * @param fileUrl  xml文件地址
     * @return 匹配结果
     * @throws Exception
     */
    boolean decryptXml(String fileUrl) throws Exception;
    /**
     * xml 地址
     * @param fileUrl
     * @return 结果
     * @throws Exception
     */
    boolean checkDateTime(String fileUrl) throws Exception;
}
