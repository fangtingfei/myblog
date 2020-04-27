package cn.ftf.myblog.service;


import cn.ftf.myblog.dao.BlogDao;
import cn.ftf.myblog.dao.CommentDao;
import cn.ftf.myblog.pojo.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private BlogDao blogDao;

    @Override
    public List<Comment> listCommentByBlogId(Integer blogId) {
        List<Comment> comments = commentDao.findByBlogIdParentIdNull(blogId, -1); //装所有一级评论的集合
        if(comments!=null&&comments.size()>0){
            for(Comment comment:comments){
                List<Comment> subComments=new ArrayList<>();  //装所有子评论的集合
                comment.setReplyComments(listAllSubComment(comment,subComments));
            }
        }
        return comments;
    }

    private List<Comment> listAllSubComment(Comment comment,List<Comment> subComments){
        List<Comment> oneSubComments=commentDao.findByParentCommentId(comment.getId());
        if(oneSubComments!=null&&oneSubComments.size()>0) {
            for (Comment comment1 : oneSubComments) {
                comment1.setParentComment(comment);
                subComments.add(comment1);
                listAllSubComment(comment1, subComments);
            }
        }
        return subComments;
    }

    @Override
    //接收回复的表单
    public int saveComment(Comment comment) {
        System.out.println("comment:" + comment);
        comment.setCreateTime(new Date());
        //comment.setBlog(blogDao.getDetailedBlog(comment.getBlogId()));
        return commentDao.saveComment(comment);
    }
}
