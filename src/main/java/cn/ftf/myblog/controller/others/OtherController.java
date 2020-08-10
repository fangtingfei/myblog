package cn.ftf.myblog.controller.others;

import cn.ftf.myblog.pojo.Diary;
import cn.ftf.myblog.pojo.User;
import cn.ftf.myblog.service.DiaryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Date;
import java.util.List;

@Controller
public class OtherController {

    @Autowired
    private DiaryService diaryService;

    @Autowired
    private RestTemplate restTemplate;

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
        int index = originalFilename.lastIndexOf("\\") + 1;
        if (index != 0) {
            originalFilename = originalFilename.substring(index);
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(file.getInputStream());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("D:\\test\\" + originalFilename));
            byte[] buffer = new byte[1024]; //数组大小可根据文件大小设置
            int len = -1;
            while ((len = bis.read(buffer)) != -1) { //读到文件末尾则返回-1
                bos.write(buffer, 0, len);
            }
            bos.flush();
            bis.close();
            bos.close();

            File swapfile=new File("D:\\test\\"+originalFilename);
            HttpHeaders headers = new HttpHeaders();
            //设置提交方式都是表单提交
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            //设置表单信息
            MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            params.add("file", new FileSystemResource(swapfile));
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, headers);
            String fileURL = restTemplate.postForObject(
                    "http://39.101.191.209:18084/upload/file", requestEntity, String.class);
            model.addAttribute("firImg",fileURL);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("firImg","服务异常：IOException!");
        } catch (RestClientException e) {
            e.printStackTrace();
            model.addAttribute("firImg","服务异常：RestClientException!");
        }
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
    @GetMapping("/music")
    public String about() {
        return "music";
    }
}
