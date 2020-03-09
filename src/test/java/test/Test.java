package test;

import cn.ftf.myblog.dao.BlogDao;
import cn.ftf.myblog.entity.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Test {

    @Autowired
     static private BlogDao blogDao;

    public static void main(String[] args) {
        List<BlogQuery> allBlogQuery = blogDao.getAllBlogQuery();
        for(BlogQuery blogQuery:allBlogQuery){
            System.out.println(blogQuery);
        }
    }
}
