package com.sachzhong.sachzhonglicense.model;
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
    private String sign;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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
                ", sign='" + sign + '\'' +
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
        String sourcesStr = (id+name+ip+mac+options+startTime+endTime+count);

        System.out.println("最原始字符:"+sourcesStr);
        char[] chars1 = sourcesStr.toCharArray();
        StringBuffer ascs = new StringBuffer();

        //对源字符串进行混淆，打乱位置
        for (int i = 0; i < chars1.length; i++) {
            if (i%2==0){
                ascs.append((int)chars1[i]);
                //上加整体的字符长度除2，只要整体一变，数据就会不一样
                ascs.append(chars1.length/2);
            }
            if (i<chars1.length/2){
                //变换位置进行混淆
                ascs.append((int)chars1[chars1.length-i-1]);
            }
        }
        System.out.println("打乱的字符:"+ascs.toString());
        //将打乱的字符串再进行一次打乱取值
        chars1 = ascs.toString().toCharArray();
        ascs = new StringBuffer();
        //遍历字符，然后隔5位增加到ascs数组中，增加到50位就不加了，这样既对原有信息做到了加密，又进行了混淆
        System.out.println("长度:"+chars1.length);
        for (int i = 0; i < chars1.length; i++) {
            if (i%5==0){
                ascs.append((int)chars1[i]);
            }
            if (i<=chars1.length/2){
                //增加一些后面的数
                ascs.append((int)chars1[chars1.length-i-1]);
            }
            if (ascs.length()>50){
                break;
            }
        }
        System.out.println("根据xml信息最后得到的字符:"+ascs.toString());
        return  ascs.toString();
    }
}
