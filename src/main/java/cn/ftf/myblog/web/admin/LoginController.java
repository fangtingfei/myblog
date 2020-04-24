package cn.ftf.myblog.web.admin;

import cn.ftf.myblog.dao.UserDao;
import cn.ftf.myblog.pojo.User;
import cn.ftf.myblog.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserDao userDao;

    @RequestMapping("/")
    public String toLogin(){
        return "admin/login";
    }
    @RequestMapping("/login")
    public String login(@RequestParam String username, HttpSession httpSession,String password, RedirectAttributes redirectAttributes, HttpServletResponse response){
        User user = userDao.findByUsername(username);
        if(user!=null&&user.getPassword().equals(MD5Utils.code(password))){
//            Cookie cookie=new Cookie("myblogCookie", MD5Utils.code(user.getPassword()));
//            cookie.setMaxAge(60*60*24*15);
//            response.addCookie(cookie);
            httpSession.setAttribute("user",user);
            httpSession.setMaxInactiveInterval(60*60*24*15);
            Cookie c=new Cookie("JSESSIONID",httpSession.getId());
            c.setMaxAge(60*60*24*15);
            response.addCookie(c);
            return "admin/index";
        }else {
            return "admin/login";
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpSession httpSession){
        System.out.println("Un-login user : "+httpSession.getAttribute("user"));
        httpSession.removeAttribute("user");
        return "admin/login";
    }
}
