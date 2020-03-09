package cn.ftf.myblog.entity;

/**
 * 把博客和标签关系存到数据库中使用的类
 */


public class BlogAndTag {

    private int tagId;

    private int blogId;

    public BlogAndTag(int tagId, int blogId) {
        this.tagId = tagId;
        this.blogId = blogId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    @Override
    public String toString() {
        return "BlogAndTag{" +
                "tagId=" + tagId +
                ", blogId=" + blogId +
                '}';
    }
}
