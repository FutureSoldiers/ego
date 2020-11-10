package com.ego.search.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.dubbo.service.TbItemDescDubboService;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemCat;
import com.ego.pojo.TbItemDesc;
import com.ego.commons.pojo.TbItemChild;
import com.ego.search.service.TbItemService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TbItemServiceImpl implements TbItemService {

    @Reference
   private TbItemDubboService tbItemDubboServiceImpl;

    @Reference
   private TbItemCatDubboService TbItemCatDubboServiceImpl;

    @Reference
    private TbItemDescDubboService tbItemDescDubboServiceImpl;

    @Resource
    private CloudSolrClient solrClient;
    @Override
    public void init() throws IOException, SolrServerException {
        solrClient.deleteByQuery("*:*");
        solrClient.commit();
        //查询所有正常的商品
        List<TbItem> tbItemList = tbItemDubboServiceImpl.selAllByStatus((byte) 1);
        for (TbItem tbItem : tbItemList) {
            //商品对应的类目信息
            TbItemCat cat = TbItemCatDubboServiceImpl.selById(tbItem.getCid());
            //商品对应的描述信息
            TbItemDesc desc = tbItemDescDubboServiceImpl.selByItemId(tbItem.getId());

            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id",tbItem.getId());
           doc.addField("item_title",tbItem.getTitle());
           doc.addField("item_sell_point",tbItem.getSellPoint());
           doc.addField("item_price",tbItem.getPrice());
           doc.addField("item_image",tbItem.getImage());
           doc.addField("item_category_name",cat.getName());
           doc.addField("item_desc",desc.getItemDesc());
           doc.addField("item_updated",desc.getUpdated());
           solrClient.add(doc);
        }
        solrClient.commit();
    }

    @Override
    public Map<String, Object> selByQuery(String query,int page,int rows) throws IOException, SolrServerException {
        SolrQuery params = new SolrQuery();
        //设置分页条件
        params.setStart(rows*(page-1));
        params.setRows(rows);
        //设置条件
        params.setQuery("item_keywords:\""+query+"\"");
        //设置高亮
        params.setHighlight(true);
        params.addHighlightField("item_title");
        params.setHighlightSimplePre("<span style='color:red'>");
        params.setHighlightSimplePost("</span>");
        //添加排序
        params.setSort("item_updated",SolrQuery.ORDER.desc);
        QueryResponse response = solrClient.query(params);
        List<TbItemChild> childList = new ArrayList<>();
        //未高亮内容
        SolrDocumentList solrList = response.getResults();
        //高亮内容
        Map<String, Map<String, List<String>>> hh = response.getHighlighting();

        for (SolrDocument document : solrList) {
            TbItemChild  child = new TbItemChild();
            child.setId(Long.parseLong(document.getFieldValue("id").toString()));
            List<String> list = hh.get(document.getFieldValue("id")).get("item_title");
            if (list!=null&&list.size()>0){
                child.setTitle(list.get(0));
            }else {
                child.setTitle(document.getFieldValue("item_title").toString());
            }
//            System.out.println(document.getFieldValue("item_price"));
            child.setPrice((Long)document.getFieldValue("item_price"));
            Object image = document.getFieldValue("item_image");
            child.setImages(image==null||image.equals("")?new String[1]:image.toString().split(","));
            child.setSellPoint(document.getFieldValue("item_sell_point").toString());
            childList.add(child);
        }

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("itemList",childList);
        resultMap.put("totalPages",solrList.getNumFound()%rows==0?solrList.getNumFound()/rows:solrList.getNumFound()/rows+1);

        return resultMap;
    }

    @Override
    public int add(Map<String,Object> map,String desc) throws IOException, SolrServerException {
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("id",map.get("id"));
        doc.setField("item_title",map.get("title"));
        doc.setField("item_sell_point",map.get("sellPoint"));
        doc.setField("item_price",map.get("price"));
        doc.setField("item_image",map.get("image"));
        doc.setField("item_category_name",TbItemCatDubboServiceImpl.selById((Integer) map.get("cid")).getName());
        doc.setField("item_desc",desc);
        UpdateResponse response = solrClient.add(doc);
        solrClient.commit();
        if (response.getStatus()==0){
            return 1;
        }

        return 0;
    }
}
