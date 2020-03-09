package cn.ftf.myblog.web.admin;

import cn.ftf.myblog.entity.BlogQuery;
import cn.ftf.myblog.entity.SearchBlog;
import cn.ftf.myblog.entity.ShowBlog;
import cn.ftf.myblog.pojo.Blog;
import cn.ftf.myblog.pojo.Tag;
import cn.ftf.myblog.pojo.Type;
import cn.ftf.myblog.pojo.User;
import cn.ftf.myblog.service.BlogService;
import cn.ftf.myblog.service.TagService;
import cn.ftf.myblog.service.TypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    public Integer StringToInteger(String str){
        return Integer.parseInt(str);
    }

    public void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.findAll());
        model.addAttribute("tags", tagService.findAll());
    }

    //显示
    @GetMapping("/blogs")
    public String list(Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum) {
        PageHelper.startPage(pageNum, 10);
        List<BlogQuery> allBlog = blogService.getAllBlog();
        PageInfo<BlogQuery> pageInfo = new PageInfo<>(allBlog);
        model.addAttribute("pageInfo", pageInfo);
        setTypeAndTag(model);
        return "admin/blogs";
    }

    //删除
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "Operates successfully!");
        return "redirect:/admin/blogs";
    }

    //去新增页面
    @GetMapping("/blogs/input")
    public String toAdd(Model model) {
        setTypeAndTag(model);
        return "admin/blogs-input";
    }

    //新增
    @PostMapping("/blogs")
    public String add(Blog blog, RedirectAttributes attributes, HttpSession session) {
        User user=null;
        if(session.getAttribute("user")!=null){
            user =(User) session.getAttribute("user");
            System.out.println(user);
        }else {
            System.out.println("Error:No user message！");
        }
        blog.setUserId(user.getId());
        //设置blog的type
        blog.setTypeId(StringToInteger(blog.getStrType()));
        blogService.saveBlog(blog);
        attributes.addFlashAttribute("message", "Operates successfully!");
        return "redirect:/admin/blogs";
    }

    @PostMapping("/blogs/search")
    public String search(SearchBlog searchBlog, Model model,
                         @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum) {
        //将recommend转换一下
        blogService.transformRecommend(searchBlog);
        //动态sql可以解决
        List<BlogQuery> blogBySearch = blogService.getBlogBySearch(searchBlog);
        PageHelper.startPage(pageNum, 3);
        PageInfo<BlogQuery> pageInfo = new PageInfo<>(blogBySearch);
        model.addAttribute("pageInfo", pageInfo);
        setTypeAndTag(model);
        model.addAttribute("message", "Operate successed!");
        return "admin/blogs";
    }

    //将数据回返编辑页面
    @GetMapping("/blogs/{id}/input")
    public String toUpdate(@PathVariable Integer id,Model model) {
        ShowBlog blogById = blogService.getBlogById(id);
        System.out.println("-----------------------------");
        System.out.println(blogById);
        List<Type> allType = typeService.findAll();
        List<Tag> allTag = tagService.findAll();
        model.addAttribute("blog", blogById);
        model.addAttribute("types", allType);
        model.addAttribute("tags", allTag);
        return "admin/blogs-update";
    }

    @PostMapping("/blogs/update")
    public String editPost(ShowBlog showBlog,RedirectAttributes attributes) {
        blogService.updateBlog(showBlog);
        attributes.addFlashAttribute("message","Operates successfully!");
        return "redirect:/admin/blogs";
    }
}
