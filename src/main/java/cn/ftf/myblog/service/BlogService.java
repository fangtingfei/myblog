package cn.ftf.myblog.service;


import cn.ftf.myblog.entity.*;
import cn.ftf.myblog.pojo.Blog;

import java.util.List;

public interface BlogService {
    List<Blog> getAllPojoBlog();

    ShowBlog getBlogById(Integer id);

    List<BlogQuery> getAllBlog();

    void saveBlog(Blog blog);

    int updateBlog(ShowBlog showBlog);

    int deleteBlog(Integer id);

    List<BlogQuery> getBlogBySearch(SearchBlog searchBlog);

    //修改recommend,因为recommend从前台接收只能接收字符串，但数据库中的Integer类型，所以转一下
    void transformRecommend(SearchBlog searchBlog);

    List<FirstPageBlog> getAllFirstPageBlog();

    List<RecommendBlog> getRecommendedBlog();

    List<FirstPageBlog> getSearchBlog(String query);

    DetailedBlog getDetailedBlog(Integer id);

    //根据TypeId获取博客列表，在分类页进行的操作
    List<FirstPageBlog> getByTypeId(Integer typeId);

    List<FirstPageBlog> getByTagId(Integer tagId);
}
