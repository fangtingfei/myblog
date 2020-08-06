package cn.ftf.myblog.service;

import cn.ftf.myblog.dao.TypeDao;
import cn.ftf.myblog.entity.QueryPageBean;
import cn.ftf.myblog.pojo.Type;
import cn.ftf.myblog.utils.RedisUtil;
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
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeDao typeDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void addType(String typeName) {
        typeDao.addType(typeName);
    }

    @Override
    public Type findById(Integer id) {
        Type id1 = typeDao.findById(id);
        return id1;
    }

    @Override
    public PageInfo pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        //基于mybstis框架提供的分页助手来分页
        PageHelper.startPage(currentPage, pageSize);//这条指令的原理是LocalThread本地线程，这条语句一定跟下面的查询指令，中间不能有其他的内容
        List<Type> list=typeDao.findAll_1();
        PageInfo<Type> pageInfo=new PageInfo<>(list);
        return pageInfo;
    }


    @Override
    public Type updateType(Integer id, Type type) {
        return null;
    }

    @Override
    public void delete(Integer id) {
        typeDao.delete(id);
    }

    @Override
    public List<Type> findAll() {
        Set<ZSetOperations.TypedTuple<String>> types = stringRedisTemplate.opsForZSet().reverseRangeWithScores("type", 0, 100);
        List<Type> allTypes=new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> type:types) {
            String value = type.getValue();
            String[] splitValue=value.split("-");
            Double score = type.getScore();
            Type oneType=new Type(Integer.parseInt(splitValue[0]),splitValue[1]);
            oneType.setBlogNum(new Double(score).intValue());
            allTypes.add(oneType);
        }

        return allTypes;
//        return typeDao.findAll();
    }

    @Override
    public List<Type> findAll_1() {
        return typeDao.findAll_1();
    }

    @Override
    public int updateType(Type type) {
        return typeDao.updateType(type);
    }

    @Override
    public int blogNum(Integer id) {
        return typeDao.blogNum(id);
    }

}
