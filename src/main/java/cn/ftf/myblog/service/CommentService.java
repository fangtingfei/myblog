package cn.ftf.myblog.service;


import cn.ftf.myblog.pojo.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> listCommentByBlogId(Integer blogId);

    int saveComment(Comment comment);
}
