package cn.ftf.myblog.dao;

import cn.ftf.myblog.entity.*;
import cn.ftf.myblog.pojo.Blog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogDao {

    List<Blog> getAllPojoBlog();

    ShowBlog getBlogById(Integer id);

    List<BlogQuery> getAllBlogQuery();

    int saveBlog(Blog blog);

    int deleteBlog(Integer id);

    int updateBlog(ShowBlog showBlog);

    int saveBlogAndTag(BlogAndTag blogAndTag);

    int deleteBlogAndTag(Integer blogId);

    void viewOne(Integer id);

    List<Integer> getTagIds(Integer id);

    Integer getCommentCount(Integer id);

    List<BlogQuery> searchByTitleOrTypeOrRecommend(SearchBlog searchBlog);

    List<FirstPageBlog> getFirstPageBlog();

    List<RecommendBlog> getAllRecommendBlog();

    List<FirstPageBlog> getSearchBlog(String query);

    DetailedBlog getDetailedBlog(Integer id);

    List<FirstPageBlog> getByTypeId(Integer typeId);

    List<FirstPageBlog> getByTagId(Integer tagId);
}
