package com.ego.search.service;

import com.ego.pojo.TbItem;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.Map;

public interface TbItemService {

    /**
     * 初始化
     * @throws IOException
     * @throws SolrServerException
     */
    void init() throws IOException, SolrServerException;

    /**
     * 分页查询
     * @param query
     * @return
     */
    Map<String,Object> selByQuery(String query,int page,int rows) throws IOException, SolrServerException;

    /**
     * 新增
     * @param
     * @return
     */
    int add(Map<String,Object> map,String desc) throws IOException, SolrServerException;

}



