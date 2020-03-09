package cn.ftf.myblog.service;

import cn.ftf.myblog.dao.TagDao;
import cn.ftf.myblog.entity.QueryPageBean;
import cn.ftf.myblog.pojo.Tag;
import cn.ftf.myblog.pojo.Type;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagDao tagDao;

    @Override
    public int saveTag(Tag tag) {
        return tagDao.saveTag(tag);
    }

    @Override
    public int deleteTag(Integer id) {
        return tagDao.deleteTag(id);
    }

    @Override
    public int updateTag(Tag tag) {
        return tagDao.updateTag(tag);
    }

    @Override
    public Tag getById(Integer id) {
        return tagDao.getById(id);
    }

    @Override
    public Tag getByName(String name) {
        return tagDao.getByName(name);
    }

    @Override
    public List<Tag> getAllTag() {
        return tagDao.findAll();
    }

    @Override
    public List<Tag> getTagByString(String text) {
        List<Tag> tags = new ArrayList<>();
        List<Integer> longs = convertToList(text);
        for (Integer aLong : longs) {
            tags.add(tagDao.getById(aLong));
        }
        return tags;

    }

    @Override
    public PageInfo<Tag> pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        //基于mybstis框架提供的分页助手来分页
        PageHelper.startPage(currentPage,pageSize);  //这条指令的原理是LocalThread本地线程，这条语句一定跟下面的查询指令，中间不能有其他的内容
        List<Tag> lists = tagDao.findAll();
        PageInfo<Tag> pageInfo=new PageInfo(lists);
        for(Tag list:lists){
            list.toString();
        }
        return pageInfo;
    }

    @Override
    public List<Tag> findAll() {
        return tagDao.findAll();
    }

    @Override
    public int blogNum(Integer id) {
        return tagDao.blogNum(id);
    }

    private List<Integer> convertToList(String ids) {
        List<Integer> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i=0; i < idarray.length;i++) {
                list.add(new Integer(idarray[i]));
            }
        }
        return list;
    }
}
