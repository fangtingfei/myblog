package cn.ftf.myblog.dao;


import cn.ftf.myblog.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentDao {
    //不使用@Param注解
    //不使用@Param注解时，参数只能有一个，并且是Javabean。在SQL语句里可以引用JavaBean的属性，而且只能引用JavaBean的属性。
    //查询父级评论
    List<Comment> findByParentCommentId(Integer parentCommentId);

    //通过父评论查询其子级评论
    List<Comment> findByBlogIdParentIdNull(@Param("blogId") Integer blogId, @Param("blogParentId") Integer blogParentId);

    //添加一个评论
    int saveComment(Comment comment);
}
