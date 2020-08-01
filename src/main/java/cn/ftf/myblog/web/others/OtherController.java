package cn.ftf.myblog.web.others;

import cn.ftf.myblog.entity.BlogQuery;
import cn.ftf.myblog.pojo.Diary;
import cn.ftf.myblog.pojo.User;
import cn.ftf.myblog.service.DiaryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class OtherController {

    @Autowired
    private DiaryService diaryService;

    @GetMapping("/getDevlog")
    public String getDevLog(){
        return "devlog";
    }


    @GetMapping("/admin/file/upload")
    public String puload(){
        return "admin/upload";
    }


    @GetMapping("/getDiary")
    public String getAllDiary(Model model,@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum){
        PageHelper.startPage(pageNum, 15);
        List<Diary> allDiary = diaryService.getAllDiary();
        PageInfo<Diary> pageInfo = new PageInfo<>(allDiary);
        model.addAttribute("pageInfo", pageInfo);
        return "diary";
    }
    @GetMapping("/admin/addDiary")
    public String addDiary(){
        return "admin/diary-input";
    }
    @PostMapping("/admin/addDiary")
    public String addDiary(Diary diary, HttpSession session){
        diary.setAuthor(((User)session.getAttribute("user")).getNickName());
        diary.setCreate_time(new Date());
        diaryService.addDiary(diary);
        return "redirect:/getDiary";
    }
    @PostMapping("/admin/file/firImgupload")
    public String upload(@RequestParam("file") MultipartFile file,Model model) throws IOException {
        System.out.println(file.getSize());
        String originalFilename = file.getOriginalFilename();
        System.out.println(originalFilename);
        model.addAttribute("firImg","文件系统计划部署为微服务，正在整合搭建，敬请期待！");
//        model.addAttribute("firImg","");
        return "admin/blogs-input::firImg";
    }

    @GetMapping("/admin/updateDiary/{id}")
    public String updateDiary(@PathVariable Integer id,Model model){
        Diary diary=diaryService.findById(id);
        model.addAttribute("diary",diary);
        return "admin/diary-update";
    }
    @PostMapping("/admin/updateDiary/{id}")
    public String updateDiary(@PathVariable Integer id,Diary diary){
        diary.setId(id);
        diaryService.updateDiary(diary);
        return "redirect:/getDiary";
    }
}
