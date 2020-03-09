package cn.ftf.myblog.web.admin;

import cn.ftf.myblog.entity.PageResult;
import cn.ftf.myblog.entity.QueryPageBean;
import cn.ftf.myblog.pojo.Type;
import cn.ftf.myblog.service.TypeService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminTypeController {

    @Autowired
    private TypeService typeService;

    //列表页
    @RequestMapping("/types")
    public String list(Model model,@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum) {
        QueryPageBean queryPageBean=new QueryPageBean(pageNum,6,null);
        PageInfo pageInfo = typeService.pageQuery(queryPageBean);
        model.addAttribute("pageInfo", pageInfo);
        return "admin/types";
    }

    //去新增页面
    @RequestMapping("/types/input")
    public String toAdd() {
        return "admin/types-input";
    }


    //Add
    @RequestMapping("/types/add")
    public String Add(Type type, RedirectAttributes attributes, BindingResult result) {
        //添加操作
        typeService.addType(type.getName());
        return "redirect:/admin/types";
    }

    //删除
    @RequestMapping("/types/{id}/delete")
    public String delete(@PathVariable Integer id, Model model) {
        typeService.delete(id);
        model.addAttribute("message","删除成功！");
        return "redirect:/admin/types";
    }

    //进行修改
    @RequestMapping("/types/update")
    public String editPost(Type type) {
        typeService.updateType(type);
        return "redirect:/admin/types";
    }
}

