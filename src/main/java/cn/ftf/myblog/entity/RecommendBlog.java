package cn.ftf.myblog.entity;


public class RecommendBlog {

    private Integer id;
    private String title;
    private boolean recommend=true;
    private Integer views;

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

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

    @Override
    public String toString() {
        return "RecommendBlog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", recommend=" + recommend +
                ", views=" + views +
                '}';
    }
}
