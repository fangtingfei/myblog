package cn.ftf.myblog.dao;
import cn.ftf.myblog.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagDao {
    int saveTag(Tag tag);
    int deleteTag(Integer id);
    int updateTag(Tag tag);
    Tag getById(Integer id);
    Tag getByName(String name);
    List<Tag> findAll();
    List<Tag> findAll_1();
    int blogNum(Integer id);
    void deleteBlogAndTag(Integer id);
}
