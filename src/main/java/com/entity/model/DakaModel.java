package com.entity.model;

import com.entity.DakaEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;


/**
 * 疫情数据
 * 接收传参的实体类
 *（实际开发中配合移动端接口开发手动去掉些没用的字段， 后端一般用entity就够用了）
 * 取自ModelAndView 的model名称
 */
public class DakaModel implements Serializable {
    private static final long serialVersionUID = 1L;




    /**
     * 主键
     */
    private Integer id;


    /**
     * 用户
     */
    private Integer yonghuId;


    /**
     * 健康码打卡
     */
    private String dakaName;


    /**
     * 健康码照片
     */
    private String dakaFile;


    /**
     * 就诊情况
     */
    private String dakaJiuzhen;


    /**
     * 疫情状态
     */
    private Integer dakaTypes;


    /**
     * 体温
     */
    private Double dakaWendu;


    /**
     * 备注
     */
    private String dakaText;


    /**
     * 逻辑删除
     */
    private Integer dakaDelete;


    /**
     * 录入时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    private Date insertTime;


    /**
     * 创建时间  show3 listShow
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    private Date createTime;


    /**
	 * 获取：主键
	 */
    public Integer getId() {
        return id;
    }


    /**
	 * 设置：主键
	 */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
	 * 获取：用户
	 */
    public Integer getYonghuId() {
        return yonghuId;
    }


    /**
	 * 设置：用户
	 */
    public void setYonghuId(Integer yonghuId) {
        this.yonghuId = yonghuId;
    }
    /**
	 * 获取：健康码打卡
	 */
    public String getDakaName() {
        return dakaName;
    }


    /**
	 * 设置：健康码打卡
	 */
    public void setDakaName(String dakaName) {
        this.dakaName = dakaName;
    }
    /**
	 * 获取：健康码照片
	 */
    public String getDakaFile() {
        return dakaFile;
    }


    /**
	 * 设置：健康码照片
	 */
    public void setDakaFile(String dakaFile) {
        this.dakaFile = dakaFile;
    }
    /**
	 * 获取：就诊情况
	 */
    public String getDakaJiuzhen() {
        return dakaJiuzhen;
    }


    /**
	 * 设置：就诊情况
	 */
    public void setDakaJiuzhen(String dakaJiuzhen) {
        this.dakaJiuzhen = dakaJiuzhen;
    }
    /**
	 * 获取：疫情状态
	 */
    public Integer getDakaTypes() {
        return dakaTypes;
    }


    /**
	 * 设置：疫情状态
	 */
    public void setDakaTypes(Integer dakaTypes) {
        this.dakaTypes = dakaTypes;
    }
    /**
	 * 获取：体温
	 */
    public Double getDakaWendu() {
        return dakaWendu;
    }


    /**
	 * 设置：体温
	 */
    public void setDakaWendu(Double dakaWendu) {
        this.dakaWendu = dakaWendu;
    }
    /**
	 * 获取：备注
	 */
    public String getDakaText() {
        return dakaText;
    }


    /**
	 * 设置：备注
	 */
    public void setDakaText(String dakaText) {
        this.dakaText = dakaText;
    }
    /**
	 * 获取：逻辑删除
	 */
    public Integer getDakaDelete() {
        return dakaDelete;
    }


    /**
	 * 设置：逻辑删除
	 */
    public void setDakaDelete(Integer dakaDelete) {
        this.dakaDelete = dakaDelete;
    }
    /**
	 * 获取：录入时间
	 */
    public Date getInsertTime() {
        return insertTime;
    }


    /**
	 * 设置：录入时间
	 */
    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
    /**
	 * 获取：创建时间  show3 listShow
	 */
    public Date getCreateTime() {
        return createTime;
    }


    /**
	 * 设置：创建时间  show3 listShow
	 */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    }
