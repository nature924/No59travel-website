package com.entity.model;

import com.entity.LvyoudongtaiEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;


/**
 * 旅游动态
 * 接收传参的实体类
 *（实际开发中配合移动端接口开发手动去掉些没用的字段， 后端一般用entity就够用了）
 * 取自ModelAndView 的model名称
 */
public class LvyoudongtaiModel implements Serializable {
    private static final long serialVersionUID = 1L;




    /**
     * 主键
     */
    private Integer id;


    /**
     * 动态标题
     */
    private String lvyoudongtaiName;


    /**
     * 动态类型
     */
    private Integer lvyoudongtaiTypes;


    /**
     * 动态图片
     */
    private String lvyoudongtaiPhoto;


    /**
     * 用户
     */
    private Integer yonghuId;


    /**
     * 动态详情
     */
    private String lvyoudongtaiContent;


    /**
     * 发布时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    private Date insertTime;


    /**
     * 创建时间 show1 show2 photoShow
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
	 * 获取：动态标题
	 */
    public String getLvyoudongtaiName() {
        return lvyoudongtaiName;
    }


    /**
	 * 设置：动态标题
	 */
    public void setLvyoudongtaiName(String lvyoudongtaiName) {
        this.lvyoudongtaiName = lvyoudongtaiName;
    }
    /**
	 * 获取：动态类型
	 */
    public Integer getLvyoudongtaiTypes() {
        return lvyoudongtaiTypes;
    }


    /**
	 * 设置：动态类型
	 */
    public void setLvyoudongtaiTypes(Integer lvyoudongtaiTypes) {
        this.lvyoudongtaiTypes = lvyoudongtaiTypes;
    }
    /**
	 * 获取：动态图片
	 */
    public String getLvyoudongtaiPhoto() {
        return lvyoudongtaiPhoto;
    }


    /**
	 * 设置：动态图片
	 */
    public void setLvyoudongtaiPhoto(String lvyoudongtaiPhoto) {
        this.lvyoudongtaiPhoto = lvyoudongtaiPhoto;
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
	 * 获取：动态详情
	 */
    public String getLvyoudongtaiContent() {
        return lvyoudongtaiContent;
    }


    /**
	 * 设置：动态详情
	 */
    public void setLvyoudongtaiContent(String lvyoudongtaiContent) {
        this.lvyoudongtaiContent = lvyoudongtaiContent;
    }
    /**
	 * 获取：发布时间
	 */
    public Date getInsertTime() {
        return insertTime;
    }


    /**
	 * 设置：发布时间
	 */
    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
    /**
	 * 获取：创建时间 show1 show2 photoShow
	 */
    public Date getCreateTime() {
        return createTime;
    }


    /**
	 * 设置：创建时间 show1 show2 photoShow
	 */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    }
