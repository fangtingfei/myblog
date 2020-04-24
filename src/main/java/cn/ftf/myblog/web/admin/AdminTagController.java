package cn.ftf.myblog.web.admin;

import cn.ftf.myblog.entity.QueryPageBean;
import cn.ftf.myblog.pojo.Tag;
import cn.ftf.myblog.service.TagService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminTagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String list(Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum) {
        QueryPageBean queryPageBean=new QueryPageBean(pageNum,6,null);
        PageInfo pageInfo = tagService.pageQuery(queryPageBean);
        model.addAttribute("pageInfo", pageInfo);
        return "admin/tags";
    }

    //去新增页面
    @GetMapping("/tags/input")
    public String toAdd() {
        return "admin/tags-input";
    }

    @PostMapping("/tags/add")
    public String add(Tag tag, RedirectAttributes attributes) {
        System.out.println("前端传过来的表单" + tag);
        Tag tag1 = tagService.getByName(tag.getName());
        if (tag1 != null) {
            //不为空说明数据库已有
            attributes.addFlashAttribute("message", "已有这个标签，不能添加重复");
            return "redirect:/admin/tags/input";
        }
        tagService.saveTag(tag);
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Integer id, Model model) {
        model.addAttribute("tag", tagService.getById(id));
        return "admin/tags-update";
    }

    @PostMapping("/tags/update")
    public String editPost(Tag tag) {
        tagService.updateTag(tag);
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Integer id) {
        tagService.deleteTag(id);
        return "redirect:/admin/tags";
    }
}
