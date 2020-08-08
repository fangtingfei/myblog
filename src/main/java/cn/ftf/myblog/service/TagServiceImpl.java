package cn.ftf.myblog.service;

import cn.ftf.myblog.dao.TagDao;
import cn.ftf.myblog.entity.QueryPageBean;
import cn.ftf.myblog.pojo.Tag;
import cn.ftf.myblog.pojo.Type;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagDao tagDao;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void saveTag(Tag tag) {
        tagDao.saveTag(tag);
        redisTemplate.opsForZSet().add("tag",String.valueOf(tag.getId())+"-"+tag.getName(),0);
    }

    @Override
    public void deleteTag(Integer id) {
        Tag tag = tagDao.getById(id);
        tagDao.deleteTag(id);
        redisTemplate.opsForZSet().remove("tag",String.valueOf(tag.getId())+"-"+tag.getName());
    }

    @Override
    public void updateTag(Tag tag) {
        Tag ordTag = tagDao.getById(tag.getId());
        String key=String.valueOf(ordTag.getId())+"-"+ordTag.getName();
        //先查
        Double score = stringRedisTemplate.opsForZSet().score("tag", key);
        //再删
        stringRedisTemplate.opsForZSet().remove("tag",key);
        //再添
        stringRedisTemplate.opsForZSet().add("tag",String.valueOf(tag.getId())+"-"+tag.getName(),score);
        tagDao.updateTag(tag);
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
        Set<ZSetOperations.TypedTuple<String>> tags = stringRedisTemplate.opsForZSet().reverseRangeWithScores("tag", 0, 100);
        List<Tag> allTags=new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> tag:tags) {
            String value = tag.getValue();
            String[] splitValue=value.split("-");
            Double score = tag.getScore();
            Tag ontTag=new Tag(Integer.parseInt(splitValue[0]),splitValue[1]);
            ontTag.setBlogNum(new Double(score).intValue());
            allTags.add(ontTag);
        }
        return allTags;
//        return tagDao.findAll();
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
        List<Tag> lists = tagDao.findAll_1();
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
    public List<Tag> findAll_1() {
        return tagDao.findAll_1();
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
