package cn.ftf.myblog.web;


import cn.ftf.myblog.pojo.Comment;
import cn.ftf.myblog.pojo.User;
import cn.ftf.myblog.service.BlogService;
import cn.ftf.myblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;


    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session, Model model) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String ip = requestAttributes.getRequest().getRemoteAddr();
        Integer blogId = comment.getBlogId();
//        if(comment.getContent()==""){
//            List<Comment> comments = commentService.listCommentByBlogId(blogId);
//            model.addAttribute("comments", comments);
//            return "blog :: commentList";
//        }
        comment.setIp(ip);
        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setNickname("[admin] "+user.getNickName());
            comment.setAdminComment(true);
        } else {
            //设置头像
            comment.setAvatar(avatar);
        }

        if (comment.getParentComment().getId() != null) {
            comment.setParentCommentId(comment.getParentComment().getId());
        }
        commentService.saveComment(comment);
        List<Comment> comments = commentService.listCommentByBlogId(blogId);
        model.addAttribute("comments", comments);
        return "blog :: commentList";
    }
}
