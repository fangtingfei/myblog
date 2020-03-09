package cn.ftf.myblog.service;

import cn.ftf.myblog.entity.QueryPageBean;
import cn.ftf.myblog.pojo.Tag;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface TagService {
    int saveTag(Tag tag);

    int deleteTag(Integer id);

    int updateTag(Tag tag);

    Tag getById(Integer id);

    Tag getByName(String name);

    List<Tag> getAllTag();

    List<Tag> getTagByString(String text);

    PageInfo<Tag> pageQuery(QueryPageBean queryPageBean);

    List<Tag> findAll();

    int blogNum(Integer id);
}
