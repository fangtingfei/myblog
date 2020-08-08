package cn.ftf.myblog.service;

import cn.ftf.myblog.entity.QueryPageBean;
import cn.ftf.myblog.pojo.Tag;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface TagService {
    void saveTag(Tag tag);

    void deleteTag(Integer id);

    void updateTag(Tag tag);

    Tag getById(Integer id);

    Tag getByName(String name);

    List<Tag> getAllTag();

    List<Tag> getTagByString(String text);

    PageInfo<Tag> pageQuery(QueryPageBean queryPageBean);

    List<Tag> findAll();

    List<Tag> findAll_1();

    int blogNum(Integer id);
}
