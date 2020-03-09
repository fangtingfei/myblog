package cn.ftf.myblog.dao;


import cn.ftf.myblog.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentDao {

    //根据创建时间倒序来排

    //不使用@Param注解
    //不使用@Param注解时，参数只能有一个，并且是Javabean。在SQL语句里可以引用JavaBean的属性，而且只能引用JavaBean的属性。
    List<Comment> findByBlogIdParentIdNull(@Param("blogId") Integer blogId, @Param("blogParentId") Integer blogParentId);

    //查询父级对象
    Comment findByParentCommentId(Integer parentCommentId);


    //添加一个评论
    int saveComment(Comment comment);
}
