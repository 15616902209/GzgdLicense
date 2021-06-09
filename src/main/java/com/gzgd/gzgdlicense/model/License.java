package com.gzgd.gzgdlicense.model;

import cn.hutool.crypto.digest.MD5;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @Author: SachZhong
 * @Description:
 * @Date Created in 2021/6/9 12:02
 */
public class License {
    /**
     *  唯一标识
     */
    private String id;
    /**
     * 名称，可以存储 设备名称，系统名称，等名称
     */
    private String name;
    /**
     * 设备系统的生效IP地址
     */
    private String ip;
    /**
     * 设备系统的mac地址
     */
    private String mac;
    /**
     * 被加密的字符 提供给外部，存储在xml，或者文本中
     */
    private String encryptedStr;
    /**
     * 生效时间
     */
    private String startTime;
    /**
     * 失效时间
     */
    private String endTime;
    /**
     * 统计 做些业务的统计
     */
    private String count;
    /**
     * 存储操作信息，可用json存储
     */
    private String options;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getEncryptedStr() {
        return encryptedStr;
    }

    public void setEncryptedStr(String encryptedStr) {
        this.encryptedStr = encryptedStr;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "License{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", mac='" + mac + '\'' +
                ", encryptedStr='" + encryptedStr + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", count='" + count + '\'' +
                ", options='" + options + '\'' +
                '}';
    }

    /**
     * 返回要被加密的字符
     * @return
     */
    public String getSourceStr(){
        //id是唯一的，name来区分系统，ip用来验证地址 用来验证准确性
        String sourcesStr = (id+name+ip+options);
        //字符串先转成ascii码
        StringBuffer ascs = new StringBuffer();
        char[] chars1 = sourcesStr.toCharArray();
        //遍历字符，然后隔两位增加到ascs数组中，然后大于50位就不加了，这样既对原有信息做到了加密，又进行了混淆
        for (int i = 0; i < chars1.length; i++) {
            int asc = (int)chars1[i];
            if (i%2==0){
                ascs.append(asc);
            }
            if (ascs.length()>50){
                break;
            }
        }
        return  ascs.toString();
    }
}
