package cn.ftf.myblog.service;


import cn.ftf.myblog.dao.BlogDao;
import cn.ftf.myblog.entity.*;
import cn.ftf.myblog.pojo.Blog;
import cn.ftf.myblog.utils.MarkdownUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogDao blogDao;

    public List<Integer> StringListToList(String str1){
        List<Integer> arrList=new ArrayList<>();
        String[] strArr = str1.split(",");
        for(String str:strArr){
            arrList.add(Integer.parseInt(str));
        }
        return arrList;
    }

    @Override
    public ShowBlog getBlogById(Integer id) {
        return blogDao.getBlogById(id);
    }

    @Override
    public List<BlogQuery> getAllBlog() {
        List<BlogQuery> allBlogQuery = blogDao.getAllBlogQuery();
        return allBlogQuery;
    }

    @Override
    public void saveBlog(Blog blog) {
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setViews(0);
        blogDao.saveBlog(blog);
        //将标签的数据存到t_blogs_tag表中
        String tagIds = blog.getTagIds();
        List<Integer> tags=StringListToList(tagIds);
        BlogAndTag blogAndTag=null;
        for (Integer tagId : tags) {
            blogAndTag = new BlogAndTag(tagId,blog.getId());
            blogDao.saveBlogAndTag(blogAndTag);
        }
    }

    @Override
    public int updateBlog(ShowBlog showBlog) {
        showBlog.setUpdateTime(new Date());
        return blogDao.updateBlog(showBlog);
    }

    @Override
    public int deleteBlog(Integer id) {
        blogDao.deleteBlogAndTag(id);
        blogDao.deleteBlog(id);
        return 1;
    }

    @Override
    public List<BlogQuery> getBlogBySearch(SearchBlog searchBlog) {
        return blogDao.searchByTitleOrTypeOrRecommend(searchBlog);
    }

    @Override
    public void transformRecommend(SearchBlog searchBlog) {
        if (!"".equals(searchBlog.getRecommend()) && null != searchBlog.getRecommend()) {
            searchBlog.setRecommend2(1);
        }
    }

    @Override
    public List<FirstPageBlog> getAllFirstPageBlog() {
        return blogDao.getFirstPageBlog();
    }


    @Override
    public List<RecommendBlog> getRecommendedBlog() {
        List<RecommendBlog> allRecommendBlog = blogDao.getAllRecommendBlog();
        return allRecommendBlog;
    }

    @Override
    public List<FirstPageBlog> getSearchBlog(String query) {
        return blogDao.getSearchBlog(query);
    }

    @Override
    public DetailedBlog getDetailedBlog(Integer id) {
        DetailedBlog detailedBlog = blogDao.getDetailedBlog(id);
        if(detailedBlog!=null){
            blogDao.viewOne(id);
            String content = detailedBlog.getContent();
            detailedBlog.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        }
        return detailedBlog;
    }

    @Override
    public List<FirstPageBlog> getByTypeId(Integer typeId) {

        return blogDao.getByTypeId(typeId);
    }

    @Override
    public List<FirstPageBlog> getByTagId(Integer tagId) {
        return blogDao.getByTagId(tagId);
    }

    @Override
    public List<Blog> getAllPojoBlog() {
        List<Blog> allPojoBlog=blogDao.getAllPojoBlog();
        return allPojoBlog;
    }

}
