package com.gzgd.gzgdlicense.service;

/**
 * @Author: SachZhong
 * @Description: rsa 生成密钥 服务类
 * @Date Created in 2021/6/9 14:29
 */
public interface IGenXml {
    /**
     * 生成基础的xml文件
     */
    public void genBaseXml(String fileUrl) throws Exception;

    /**
     * 对xml的信息进行rsa加密，覆盖原来的xml，并生成序列号，公钥，和私钥
     */
    public void encryptXml(String fileUrl) throws Exception;
}
