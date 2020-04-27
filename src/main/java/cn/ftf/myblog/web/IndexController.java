package cn.ftf.myblog.web;


import cn.ftf.myblog.NotFountException;
import cn.ftf.myblog.entity.DetailedBlog;
import cn.ftf.myblog.entity.FirstPageBlog;
import cn.ftf.myblog.entity.RecommendBlog;
import cn.ftf.myblog.pojo.Comment;
import cn.ftf.myblog.pojo.Tag;
import cn.ftf.myblog.pojo.Type;
import cn.ftf.myblog.service.BlogService;
import cn.ftf.myblog.service.CommentService;
import cn.ftf.myblog.service.TagService;
import cn.ftf.myblog.service.TypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TagService tagService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    public String index(Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum) {
        PageHelper.startPage(pageNum, 6);  //第几页，一页多少个数据，其紧跟着的第一条查询指令会被分页，原理是ThreadLocal本地线程变量
        List<Tag> tags=new ArrayList<>();
        List<Type> types=new ArrayList<>();
        List<FirstPageBlog> allFirstPageBlog = blogService.getAllFirstPageBlog();
        PageInfo<FirstPageBlog> pageInfo = new PageInfo<>(allFirstPageBlog);
        List<Type> allType = typeService.findAll().subList(0,5);
        for(Type type:allType){
            types.add(type);
        }

        List<Tag> allTag = tagService.getAllTag().subList(0,6);
        for(Tag tag:allTag){
            tags.add(tag);
        }
        List<RecommendBlog> recommendedBlog = blogService.getRecommendedBlog();
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("tags", tags);
        model.addAttribute("types", types);
        model.addAttribute("recommendedBlogs", recommendedBlog);
        return "index";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Integer id, Model model) {
        DetailedBlog detailedBlog = blogService.getDetailedBlog(id);
        if(detailedBlog==null){
            throw new NotFountException("error:该博客不存在！");
        }
        List<Comment> comments = commentService.listCommentByBlogId(id);
        model.addAttribute("comments", comments);
        model.addAttribute("blog", detailedBlog);
        return "blog";
    }
}
