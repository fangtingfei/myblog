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
        PageHelper.startPage(pageNum, 6);
        List<Tag> tags=new ArrayList<>();
        List<Type> types=new ArrayList<>();
        List<FirstPageBlog> allFirstPageBlog = blogService.getAllFirstPageBlog();
        List<Type> allType = typeService.findAll().subList(0,5);
        for(Type type:allType){
            types.add(type);
        }

        List<Tag> allTag = tagService.getAllTag().subList(0,6);
        for(Tag tag:allTag){
            tags.add(tag);
        }
        List<RecommendBlog> recommendedBlog = blogService.getRecommendedBlog();
        PageInfo<FirstPageBlog> pageInfo = new PageInfo<>(allFirstPageBlog);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("tags", tags);
        model.addAttribute("types", types);
        model.addAttribute("recommendedBlogs", recommendedBlog);
        return "index";
    }

//    @GetMapping("/search")
//    public String search(Model model,
//                         @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum,
//                         @RequestParam String query) {
//        PageHelper.startPage(pageNum,20);
//        List<FirstPageBlog> searchBlog = blogService.getSearchBlog(query);
//        PageInfo<FirstPageBlog> pageInfo = new PageInfo<>(searchBlog);
//        model.addAttribute("pageInfo", pageInfo);
//        model.addAttribute("query", query);
//        return "search";
//    }


    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Integer id, Model model) {
        DetailedBlog detailedBlog = blogService.getDetailedBlog(id);
        if(detailedBlog==null){
            throw new NotFountException("the blog is not exist！");
        }
        List<Comment> comments = commentService.listCommentByBlogId(id);
        model.addAttribute("comments", comments);
        model.addAttribute("blog", detailedBlog);
        return "blog";
    }
}
