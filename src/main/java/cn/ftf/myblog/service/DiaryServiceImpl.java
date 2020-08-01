package cn.ftf.myblog.service;

import cn.ftf.myblog.dao.DiaryDao;
import cn.ftf.myblog.pojo.Diary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiaryServiceImpl implements DiaryService {
    @Autowired
    private DiaryDao diaryDao;

    @Override
    public List<Diary> getAllDiary() {
        return diaryDao.getAllDiary();
    }

    @Override
    public void addDiary(Diary diary) {
        diaryDao.addDiary(diary);
    }

    @Override
    public void updateDiary(Diary diary) {
        diaryDao.updateDiary(diary);
    }

    @Override
    public Diary findById(Integer id) {
        return diaryDao.findById(id);
    }
}
