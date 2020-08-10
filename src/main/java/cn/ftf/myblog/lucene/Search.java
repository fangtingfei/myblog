package cn.ftf.myblog.lucene;

import cn.ftf.myblog.entity.FirstPageBlog;
import cn.ftf.myblog.entity.SearchPageInfo;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class Search {
    @RequestMapping("/search")
    public String indexSearch(@RequestParam String queryString,Model model) throws Exception{
        //建立分词器
        Analyzer analyzer=new IKAnalyzer();
        //创建查询对象，参数1，默认查询域，参数2，分词器
        QueryParser queryParser=new QueryParser("content",analyzer);
        //设置搜索关键词
        Query query = queryParser.parse(queryString);//参数中有指定域，则从指定域中差，没有就从上面的默认查询域中查
        System.out.println(query);
        //创建Director目录对象，指定索引库坐在位置
        Directory dir= FSDirectory.open(Paths.get("/app/myblog/lucene/dir"));
        //创建输入流对象
        IndexReader indexReader= DirectoryReader.open(dir);
        //创建搜索对象
        IndexSearcher indexSearcher=new IndexSearcher(indexReader);
        //搜索并返回，参数1查询关键词对象，参数二，返回数据条数
        TopDocs topDocs = indexSearcher.search(query, 50);
        TotalHits totalHits= topDocs.totalHits;//一共多少条数据
        SearchPageInfo searchPageInfo = new SearchPageInfo();searchPageInfo.setTotal((int)totalHits.value);
        System.out.println("total aount:->"+totalHits);
        //获取结果集
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        //遍历结果集
        List<FirstPageBlog> list=new ArrayList<>();
        if(scoreDocs!=null){
            for(ScoreDoc scoreDoc:scoreDocs){
                //获得lucene中文档唯一标识，文档id
                int docID = scoreDoc.doc;
                Document doc1 = indexSearcher.doc(docID);
                FirstPageBlog blog=new FirstPageBlog();
                //在文档对象中通过域名读取域内的数据
                blog.setId(Integer.parseInt(doc1.get("id")));
                blog.setFirstPicture(doc1.get("firstImg"));
                blog.setCreateTimeforSearch(doc1.get("creatTime"));
                blog.setViews(Integer.parseInt(doc1.get("views")));
                blog.setTitle(doc1.get("title"));
                blog.setDescription(doc1.get("description"));
                System.out.println(blog);
                list.add(blog);
            }
        }
        indexReader.close();

        model.addAttribute("SearchpageInfo",searchPageInfo);
        model.addAttribute("SearchResList",list);

        return "search";
    }
}
