package cn.ftf.myblog.service;

import cn.ftf.myblog.dao.TypeDao;
import cn.ftf.myblog.entity.PageResult;
import cn.ftf.myblog.entity.QueryPageBean;
import cn.ftf.myblog.pojo.Type;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeDao typeDao;
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
        System.out.println(currentPage);
        System.out.println(pageSize);
        //基于mybstis框架提供的分页助手来分页
        PageHelper.startPage(currentPage, pageSize);//这条指令的原理是LocalThread本地线程，这条语句一定跟下面的查询指令，中间不能有其他的内容
        List<Type> list=typeDao.findAll();
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
        return typeDao.findAll();
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
