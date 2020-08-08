package cn.ftf.myblog.service;


import cn.ftf.myblog.dao.BlogDao;
import cn.ftf.myblog.dao.TagDao;
import cn.ftf.myblog.dao.TypeDao;
import cn.ftf.myblog.entity.*;
import cn.ftf.myblog.pojo.Blog;
import cn.ftf.myblog.pojo.Tag;
import cn.ftf.myblog.pojo.Type;
import cn.ftf.myblog.utils.MarkdownUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private TypeDao typeDao;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private StringRedisTemplate redisTemplate;

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
    public List<RecommendBlog> getMostBlog() {
        List<RecommendBlog> resList=new ArrayList<>();
//        Set<String> mostBlog = redisTemplate.opsForZSet().reverseRange("blog", 0l, 4l);
        Set<ZSetOperations.TypedTuple<String>> mostBlog = redisTemplate.opsForZSet().reverseRangeWithScores("blog", 0l, 6l);
        for(ZSetOperations.TypedTuple<String> blog:mostBlog){
            String str = blog.getValue();
            Double score = blog.getScore();
            int id=Integer.parseInt(str.split("-")[0]);
            String title=str.split("-")[1];
            RecommendBlog recommendBlog=new RecommendBlog();
            recommendBlog.setId(id);
            recommendBlog.setTitle(title);
            recommendBlog.setViews(new Double(score).intValue());
            resList.add(recommendBlog);
        }
        return resList;
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
        if(blog.isRecommend()){
            redisTemplate.opsForList().leftPush("latest_blog", String.valueOf(blog.getId())+"-"+blog.getTitle());
        }
        Type type=typeDao.findById(blog.getTypeId());
        redisTemplate.opsForZSet().incrementScore("type", String.valueOf(type.getId())+"-"+type.getName(), 1);
        for (Integer tagId : tags) {
            Tag tag = tagDao.getById(tagId);
            redisTemplate.opsForZSet().incrementScore("tag", String.valueOf(tagId)+"-"+tag.getName(), 1);
        }
        redisTemplate.opsForZSet().add("blog",String.valueOf(blog.getId())+"-"+blog.getTitle(),0);
    }

    @Override
    public int updateBlog(ShowBlog showBlog) {
        showBlog.setUpdateTime(new Date());
        tagDao.deleteBlogAndTag(showBlog.getId());
        String tagIds = showBlog.getTagIds();
        List<Integer> tags=StringListToList(tagIds);
        BlogAndTag blogAndTag=null;
        for (Integer tagId : tags) {
            blogAndTag = new BlogAndTag(tagId,showBlog.getId());
            blogDao.saveBlogAndTag(blogAndTag);
        }
        //先删掉redis记录
        Type type = typeDao.findByBlogId(showBlog.getId());
        redisTemplate.opsForZSet().incrementScore("type", String.valueOf(type.getId())+"-"+type.getName(), -1);
        List<Tag> tagss=tagDao.findByBlogId(showBlog.getId());
        for(Tag tag:tagss){
            redisTemplate.opsForZSet().incrementScore("tag", String.valueOf(tag.getId())+"-"+tag.getName(), -1);
        }

        //再存上
        Type rtype=typeDao.findById(showBlog.getTypeId());
        redisTemplate.opsForZSet().incrementScore("type", String.valueOf(rtype.getId())+"-"+rtype.getName(), 1);
        for (Integer tagId : tags) {
            Type tag = typeDao.findById(tagId);
            redisTemplate.opsForZSet().incrementScore("tag", String.valueOf(tagId)+"-"+tag.getName(), 1);
        }
        return blogDao.updateBlog(showBlog);
    }

    @Override
    public void deleteBlog(Integer id) {
        blogDao.deleteBlogAndTag(id);
        ShowBlog blogById = blogDao.getBlogById(id);
        blogDao.deleteBlog(id);
        Type type = typeDao.findByBlogId(id);
        System.out.println(String.valueOf(type.getId())+"-"+type.getName());
        redisTemplate.opsForZSet().incrementScore("type", String.valueOf(type.getId())+"-"+type.getName(), -1);
        List<Tag> tags=tagDao.findByBlogId(id);
        for(Tag tag:tags){
            redisTemplate.opsForZSet().incrementScore("tag", String.valueOf(tag.getId())+"-"+tag.getName(), -1);
        }
        redisTemplate.opsForZSet().remove("blog",String.valueOf(blogById.getId())+"-"+blogById.getTitle());
        redisTemplate.opsForList().remove("latest_blog", 0, String.valueOf(blogById.getId())+"-"+blogById.getTitle());
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
//        List<RecommendBlog> allRecommendBlog = blogDao.getAllRecommendBlog();
        List<RecommendBlog> recommendBlogs=new ArrayList<>();
        List<String> latest_blog = redisTemplate.opsForList().range("latest_blog", 0l, 5l);
        for(String str:latest_blog){
            RecommendBlog recommendBlog=new RecommendBlog();
            recommendBlog.setId(Integer.parseInt(str.split("-")[0]));
            recommendBlog.setTitle(str.split("-")[1]);
            recommendBlogs.add(recommendBlog);
        }
        return recommendBlogs;
    }

    @Override
    public List<FirstPageBlog> getSearchBlog(String query) {
        return blogDao.getSearchBlog(query);
    }

    @Override
    public DetailedBlog getDetailedBlog(Integer id) {
        DetailedBlog detailedBlog = blogDao.getDetailedBlog(id);
        Integer type_id = detailedBlog.getType_id();
        detailedBlog.setType(typeDao.findById(type_id).getName());
        detailedBlog.setCommentCount(blogDao.getCommentCount(id));
        if(detailedBlog!=null){
            blogDao.viewOne(id);
            redisTemplate.opsForZSet().incrementScore("blog", String.valueOf(detailedBlog.getId())+"-"+detailedBlog.getTitle(), 1);
            String content = detailedBlog.getContent();
            detailedBlog.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        }
        return detailedBlog;
    }

    @Override
    public String getTagIds(Integer id) {
        StringBuffer buffer=new StringBuffer();
        List<Integer> list=blogDao.getTagIds(id);
        for(Integer tagid:list){
            buffer.append(String.valueOf(tagid) + ",");
        }
        return buffer.toString();
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
