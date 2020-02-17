package com.blackcat.solr;

import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p> 描述 ：solr增删改查demo控制器
 *
 * @author : blackcat
 * @date : 2020/2/17 17:33
 */
@Slf4j
@RestController
@RequestMapping("solr")
public class SolrController {

    @Autowired
    private SolrClient client;

    /**
     * 新增或者修改索引，当id存在时为更新，如果id不存在则为新增
     * @return
     */
    @RequestMapping("/addOrUpdate")
    public String addOrUpdate() {
        //不重复的UUID，当前现有数据从id=10开始
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        try {
            SolrInputDocument doc = new SolrInputDocument();
            doc.setField("id", uuid);
            doc.setField("name", "测试123");
            doc.setField("isDeleted", 0);
            //如果spring.data.solr.host里面配置到articles，那么这里就不需要传articles
            client.add("articles", doc);
            client.commit("articles");
            log.info("新增/更新成功！");
            return "新增/更新成功！";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据id删除索引
     *
     * @param id
     * @return
     */
    @RequestMapping("delete")
    public String delete(String id)  {
        try {
            client.deleteById("articles",id);
            client.commit("articles");
            log.info("删除id=[{}]成功！",id);
            return "删除成功！";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除所有的索引
     *
     * @return
     */
    @RequestMapping("deleteAll")
    public String deleteAll(){
        try {
            client.deleteByQuery("articles","*:*");
            client.commit("articles");
            log.info("删除所有索引成功！");
            return "删除所有索引成功！";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据id查询索引
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("getById")
    public String getById(String id) throws Exception {
        SolrDocument document = client.getById("articles", id);
        log.info("根据id=[{}]查询索引成功！", id);
        log.info("查询结果为：{}", document.toString());
        return document.toString();
    }

    /**
     * 综合查询：可以按照条件，排序，分页，高亮显示，部分域信息查询
     *
     * @return
     */
    @RequestMapping("search")
    public String search(String q){
        try {
            SolrQuery params = new SolrQuery();
            //查询条件，q的对应值
            params.set("q", q);
            //过滤条件，可以增加多个多虑条件
            //params.set("fq", "updateTime:[2019-04-19 08:10:59 TO 2019-04-19 09:10:59]");
            //排序，不传默认排序
            params.addSort("id", SolrQuery.ORDER.asc);
            //分页参数
            params.setStart(0);
            params.setRows(20);
            //默认域
            params.set("df", "address");
            //只查询指定域，指定显示字段
            params.set("fl", "id,name");
            //高亮
            //打开开关
            params.setHighlight(true);
            //指定高亮域
            params.addHighlightField("name");
            //高亮css设置前缀
            params.setHighlightSimplePre("<span style='background-color:yellow;'>");
            //高亮css设置后缀
            params.setHighlightSimplePost("</span>");
            //高亮设置每个分片的最大长度
            params.setHighlightFragsize(100000);

            QueryResponse queryResponse = client.query("articles", params);
            SolrDocumentList results = queryResponse.getResults();
            long numFound = results.getNumFound();
            log.info("查询结果数量为：{}条",numFound);
            log.info("查询结果为：{}",results.toString());

            //获取高亮显示的结果, 高亮显示的结果和查询结果是分开放的
            Map<String, Map<String, List<String>>> highlight = queryResponse.getHighlighting();
            log.info("查询结果高亮显示为：{}",highlight.toString());
//            for (SolrDocument result : results) {
//                Map<String, List<String>> map = highlight.get(result.get("id"));
//                List<String> list = map.get("address");
//                log.info(list.get(0));
//            }
            return results.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
