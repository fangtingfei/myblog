package cn.ftf.myblog.entity;

public class SearchPageInfo {
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "SearchPageInfo{" +
                "total=" + total +
                '}';
    }
}
