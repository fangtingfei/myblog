package cn.ftf.myblog.lucene;

import cn.ftf.myblog.pojo.Blog;
import cn.ftf.myblog.service.BlogService;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*
索引库维护
 */
@Controller()
public class IndexManager {
    @Autowired
    private BlogService blogService;
    @RequestMapping("lucene")
    public String createIndex() throws IOException {
        //采集数据
        List<Blog> allBlog = blogService.getAllPojoBlog();
        List<Document> docList=new ArrayList<>();
        //创建文档对象
        for(Blog blog:allBlog){
            Document document=new Document();
            //创建域对象并放到文档对象中
            document.add(new StringField("id",Integer.toString(blog.getId()), Field.Store.YES));

            document.add(new StoredField("creatTime",blog.getCreateTime().toString()));

            document.add(new StoredField("firstImg",blog.getFirstPicture()));

            document.add(new StoredField("views",blog.getViews()));

            document.add(new TextField("title",blog.getTitle(), Field.Store.YES));

            document.add(new TextField("content",blog.getContent(), Field.Store.NO));

            document.add(new TextField("description",blog.getDescription(), Field.Store.YES));

            //将文档放入文档集合中
            docList.add(document);
        }
        //创建分词器
        Analyzer analyzer=new IKAnalyzer();
        //创建索引库目录对象
        Directory dir= FSDirectory.open(Paths.get("C:/app/myblog/lucene/dir"));
        //创建IndexWriterConfig对象
        IndexWriterConfig config=new IndexWriterConfig(analyzer);
        //创建IndexWrite输出流对象，指定输出位置和创建配置
        IndexWriter indexWriter=new IndexWriter(dir,config);
        //写入文档到索引库
        for(Document document:docList){
            indexWriter.addDocument(document);
        }
        //释放流
        indexWriter.close();
        return "about";
    }
}
