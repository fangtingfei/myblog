package cn.ftf.myblog.dao;
import cn.ftf.myblog.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagDao {
    void saveTag(Tag tag);
    void deleteTag(Integer id);
    void updateTag(Tag tag);
    Tag getById(Integer id);
    Tag getByName(String name);
    List<Tag> findAll();
    List<Tag> findAll_1();
    int blogNum(Integer id);
    List<Tag> findByBlogId(Integer blogId);
    void deleteBlogAndTag(Integer id);
}
