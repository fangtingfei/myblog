package cn.ftf.myblog.web.others;

import org.apache.ibatis.annotations.ResultMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OtherController {
    @RequestMapping("/getdevlog")
    public String getDevLog(){
        return "others/devlog";
    }
}
