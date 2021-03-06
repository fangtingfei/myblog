package cn.ftf.myblog.controller;

import cn.ftf.myblog.entity.FirstPageBlog;
import cn.ftf.myblog.pojo.Tag;
import cn.ftf.myblog.service.BlogService;
import cn.ftf.myblog.service.TagService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class
TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String tag(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,
                      @PathVariable Integer id, Model model) {
        List<Tag> allTag = tagService.getAllTag();
        //-1表示从首页导航点进来的
        if (id == -1) {
            id = allTag.get(0).getId();
        }
        model.addAttribute("tags", allTag);
        List<FirstPageBlog> blogs = blogService.getByTagId(id);
        PageHelper.startPage(pageNum, 1);
        PageInfo<FirstPageBlog> pageInfo = new PageInfo<>(blogs);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("activeTagId", id);
        return "tags";
    }
}
