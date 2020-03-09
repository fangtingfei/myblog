package cn.ftf.myblog.entity;

import cn.ftf.myblog.pojo.Type;

import java.util.Date;

/**
 * 博客列表页显示数据所使用的类
 */

public class BlogQuery {
    private Integer id;
    private String title;
    private Date createTime;
    private Integer recommend;
    private Long typeId;
    private Type type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getRecommend() {
        return recommend;
    }

    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BlogQuery{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", updateTime=" + createTime +
                ", recommend=" + recommend +
                ", typeId=" + typeId +
                ", type=" + type +
                '}';
    }
}
