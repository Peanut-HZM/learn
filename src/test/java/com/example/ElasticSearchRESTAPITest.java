package com.example;

import com.example.demo.model.Product;
import com.example.demo.utils.GenerateIdUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.client.core.GetSourceRequest;
import org.elasticsearch.client.core.GetSourceResponse;
import org.elasticsearch.client.core.TermVectorsRequest;
import org.elasticsearch.client.core.TermVectorsResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author HuaZhongmin
 * @Date 2020/12/24
 * @Time 14:25
 * @Week 周四
 **/
public class ElasticSearchRESTAPITest {

    private final static Logger log = LoggerFactory.getLogger(ElasticSearchRESTAPITest.class);

    private final static String HOSTNAME = "127.0.0.1";

    private final static int PORT = 9200;

    private final static int ES_SHARDS = 9;

    private final static int ES_REPLICAS = 4;

    private final static int TIME_OUT = 3000;

    private final static String SCHEMA = "http";

    private final static String DOCUMENT_ID = "100";

    private final static String UPDATE_DOCUMENT_ID = "500";

    private final static String DELETE_DOCUMENT_ID = "50";

    private final static String INDEX_USER_INFO = "user_info";

    private final static String INDEX_MANAGER_INFO = "manager_info";

    private final static String INDEX_CUSTOMER_INFO = "customer_info";

    private final static String INDEX_ADMIN = "administrator_info";

    private final static String INDEX_PRODUCT = "user_info";

    private final static String INDEX_OPERATE_LOG = "user_info";

    private final static String INDEX_MAPPING = "mappings";

    private final static String INDEX_MAPPING_PROPERTIES = "properties";

    private final static String INDEX_SETTINGS = "settings";

    private final static String INDEX_SETTINGS_SHARDS = "number_of_shards";

    private final static String INDEX_SETTINGS_REPLICAS = "number_of_replicas";


    /**
     * 对索引的操作使用：RestHighLevelClient.indices()，参数GetIndexRequest,CreateIndexRequest,DeleteIndexRequest
     * RestHighLevelClient.indices().create(CreateIndexRequest) 新建索引
     * RestHighLevelClient.indices().exists(GetIndexRequest) 获判断索引是否存在
     * RestHighLevelClient.indices().delete(DeleteIndexRequest) 删除索引
     * RestHighLevelClient.indices().get(GetIndexRequest) 获取索引
     * 对文档的操作使用：RestHighLevelClient.
     * esRestClient.index(IndexRequest)
     * esRestClient.exists(GetRequest) 判断数据是否存在
     * esRestClient.get(GetRequest) 通过id获取文档
     * esRestClient.update(UpdateRequest) 通过id更新文档
     * esRestClient.delete(DeleteRequest) 通过id删除文档
     * esRestClient.mget(MultiGetRequest) 通过id集合获取文档集合
     * esRestClient.bulk(BulkRequest) 通过文档id批量操作文档
     */

    public static void main(String[] args) {
        Product product = new Product();
        product.setId(GenerateIdUtils.getUUID());
        product.setName("test" + 90000000);
        product.setCreateTime(new Date());
        product.setUpdateTime(new Date());
        product.setPrice(GenerateIdUtils.getNumberic());
        product.setType(GenerateIdUtils.getType());
        log.info(product.toString());
    }


    public RestHighLevelClient getESRestClient(){
//        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost(HOSTNAME, PORT, SCHEMA)));
        return new RestHighLevelClient(RestClient.builder(new HttpHost(HOSTNAME, PORT, SCHEMA)));
    }



    private HashMap<String,Object> getMap(String key, Object value){
        HashMap<String, Object> map = new HashMap<>();
        map.put(key,value);
        return map;
    }


    @Test
    public void createIndexRequestTest(){
        RestHighLevelClient esRestClient = getESRestClient();

        GetIndexRequest getRequest = new GetIndexRequest(INDEX_MANAGER_INFO);
        try {
            boolean exists = esRestClient.indices().exists(getRequest,RequestOptions.DEFAULT);
            if (exists){
                GetIndexResponse getIndexResponse = esRestClient.indices().get(getRequest, RequestOptions.DEFAULT);
                Map<String, MappingMetadata> mappings = getIndexResponse.getMappings();
                DeleteIndexRequest deleteRequest = new DeleteIndexRequest(INDEX_MANAGER_INFO);
                AcknowledgedResponse delete = esRestClient.indices().delete(deleteRequest, RequestOptions.DEFAULT);
                if (delete.isAcknowledged()){
                    log.info("索引：{}存在，已删除",INDEX_MANAGER_INFO);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        CreateIndexRequest customerInfoCreateIndexRequest = new CreateIndexRequest(INDEX_MANAGER_INFO);
        HashMap<String, HashMap<String, Object>> properties = new HashMap<>();
        properties.put("id" , getMap("type","text"));
        properties.put("name" , getMap("type","text"));
        properties.put("phone" , getMap("type","long"));
        properties.put("age" , getMap("type","integer"));
        properties.put("email" , getMap("type","text"));
        properties.put("assets" , getMap("type","double"));
        properties.put("address" , getMap("type","text"));
        properties.put("isMarried" , getMap("type","boolean"));
        properties.put("isGraduated" , getMap("type","boolean"));
        properties.put("createTime" , getMap("type","date"));
        properties.put("updateTime" , getMap("type","date"));
        properties.put("workExperience" , getMap("type","object"));
        properties.put("workExperience.yearCount" , getMap("type","integer"));
        properties.put("workExperience.companyCount" , getMap("type","integer"));
        properties.put("workExperience.currentCompany" , getMap("type","keyword"));
        properties.put("workExperience.currentSalary" , getMap("type","double"));
        properties.put("introduction" , getMap("type","keyword"));
        //索引设置方式一 JsonBuilder
        try (XContentBuilder jsonBuilder = XContentFactory.jsonBuilder()) {
            jsonBuilder.startObject()
                    .startObject(INDEX_MAPPING).field(INDEX_MAPPING_PROPERTIES,properties).endObject()
                    .startObject(INDEX_SETTINGS).field(INDEX_SETTINGS_SHARDS,ES_SHARDS).field(INDEX_SETTINGS_REPLICAS,ES_REPLICAS).endObject()
                    .endObject();

            customerInfoCreateIndexRequest.source(jsonBuilder);
            esRestClient.indices().create(customerInfoCreateIndexRequest , RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.info("创建索引：{}失败",INDEX_CUSTOMER_INFO);
            e.printStackTrace();
        }

        /*HashMap<String, Object> settingMap = new HashMap<>();
        settingMap.put(INDEX_SETTINGS_SHARDS,ES_SHARDS);
        settingMap.put(INDEX_SETTINGS_REPLICAS,ES_REPLICAS);
        //索引设置方式二
        CreateIndexRequest managerInfoCreateIndex = new CreateIndexRequest(INDEX_MANAGER_INFO);
        managerInfoCreateIndex.mapping(properties);
        managerInfoCreateIndex.settings(settingMap);

        try {
            esRestClient.indices().create(managerInfoCreateIndex,RequestOptions.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
            log.info("创建索引：{}失败",INDEX_MANAGER_INFO);
        }
*/
    }

    @Test
    public void indexRequestTest(){
        RestHighLevelClient restHighLevelClient = getESRestClient();
        long startTime = System.currentTimeMillis();
        IndexRequest indexRequest = new IndexRequest(INDEX_USER_INFO);
//        HashMap<String, Object> hashMap = new HashMap<>();
//        方式一
//        String jsonString = ""
//        indexRequest.source(jsonString , XContentType.JSON)
//        方式二
        /*hashMap.put("id", GenerateIdUtils.getUUID());
//        hashMap.put("id", 1);
        hashMap.put("userName","peanut");
        hashMap.put("sex","女");
        hashMap.put("height",175);
        hashMap.put("weight",137.4);
        hashMap.put("role","manager");
        hashMap.put("isMarried",false);
        hashMap.put("isGraduated",true);
        HashMap<String, Object> workExperience = new HashMap<>();
        workExperience.put("companyName","阿里巴巴");
        workExperience.put("workYear","5年");
        workExperience.put("level","P9");
        workExperience.put("joinDate",System.currentTimeMillis());
        hashMap.put("workExperience",workExperience);
        hashMap.put("school",new String[]{"湖北大学","华中科技大学"});
        indexRequest.source(hashMap);*/
//        方式三
//        indexRequest.source("","","","");
//        方式四
        String[] addressArray = {"广东省深圳市福田区","湖北省武汉市洪山区","北京市朝阳区","上海市青浦区","香港特别行政区","海外"};
        String[] companyArray = {"阿里巴巴吧","平安科技","京东","美团","腾讯科技","微软","金蝶中国有限公司"};
        for (int i = 0; i < 1000; i++) {
            try {
                XContentBuilder jsonBuilder = XContentFactory.jsonBuilder();
                String uuid = GenerateIdUtils.getUUID();
                jsonBuilder.startObject();
                {
                    jsonBuilder.field("id", uuid);
                    jsonBuilder.field("userName","Peter Green");
                    if (i%2 == 0) {
                        jsonBuilder.field("sex", "男");
                    }else {
                        jsonBuilder.field("sex", "女");
                    }
                    jsonBuilder.field("address",addressArray[i % 6]);
                    jsonBuilder.field("height",175);
                    jsonBuilder.field("weight",137.4);
                    jsonBuilder.field("role","super manager");
                    jsonBuilder.field("isMarried",false);
                    jsonBuilder.field("isGraduated",true);
                    jsonBuilder.startObject("workExperience");
                    {
                        jsonBuilder.field("companyName",companyArray[i % 7]);
                        jsonBuilder.field("workYear","5年");
                        jsonBuilder.field("level","P9");
                        jsonBuilder.field("joinDate", startTime);
                    }
                    jsonBuilder.endObject();
                    jsonBuilder.field("school",new String[]{"哈佛大学","湖北大学","武汉大学","华中科技大学","北京大学","清华大学"});
                }
                jsonBuilder.endObject();
                indexRequest.source(jsonBuilder);
            }catch (Exception e){
                e.printStackTrace();
            }
        //        设置路由策略
            indexRequest.routing("routing");
        //        设置请求超时时间
            indexRequest.timeout(TimeValue.timeValueMillis(TIME_OUT));
        //        设置刷新策略
            indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        //        设置数据的版本
        //        indexRequest.version(10);
        //        设置数据的类型
        //        indexRequest.versionType(VersionType.INTERNAL);
        //        设置索引操作类型
            indexRequest.opType(DocWriteRequest.OpType.INDEX);
        //        设置pipeline
        //        indexRequest.setPipeline("pipeline");
        //        发起索引操作请求
            try {
                indexRequest.id(String.valueOf(i));
                IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
                log.info("索引创建成功：{} , 数据id:{}" , indexResponse.getIndex() , indexResponse.getId());
        //            Cancellable indexAsync = restHighLevelClient.indexAsync(indexRequest, RequestOptions.DEFAULT, new ESActionListener());
            }catch (Exception e){
                e.printStackTrace();
                log.info("索引创建失败！！！！");
            }
        }
        //4m44s757ms
        log.info("循环操作1000条数据耗时：{}" , (System.currentTimeMillis() - startTime));

    }

    @Test
    public void getIndexRequestTest(){
        RestHighLevelClient esRestClient = getESRestClient();
        GetRequest getRequest = new GetRequest(INDEX_USER_INFO,"1");
//        getRequest.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE);

//        String[] includes = {"setting.number_of_shards", "isGraduated"};
        String[] excludes = {"setting.number_of_shards", "isGraduated"};
        String[] includes = Strings.EMPTY_ARRAY;
//        String[] excludes = Strings.EMPTY_ARRAY;
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
        getRequest.fetchSourceContext(fetchSourceContext);

        getRequest.routing("routing");

        getRequest.preference("preference");

        getRequest.realtime(false);

        getRequest.refresh(true);

        getRequest.version(1);

        getRequest.versionType(VersionType.EXTERNAL);
//        getRequest.id(String.valueOf(67));
        //通过id查询es中的数据
        try {
            GetResponse getResponse = esRestClient.get(getRequest, RequestOptions.DEFAULT);
            String sourceAsString = getResponse.getSourceAsString();
            log.info("通过id:{}，获取的数据：{}" , getRequest.id() , sourceAsString);
        }catch (Exception e){
            e.printStackTrace();
            log.info("通过id查询数据失败！！！");
        }
        /**
         * {"isMarried":false,"address":"北京市朝阳区","role":"super manager","school":["哈佛大学","湖北大学","武汉大学","华中科技大学","北京大学","清华大学"],"sex":"男","workExperience":{"companyName":"美团","workYear":"5年","level":"P9","joinDate":1608864478490},"weight":137.4,"id":"80F2CCA1C7BB49BFA40B9B67FFB79F80","userName":"Peter Green","height":175}
         */
    }

    @Test
    public void getSourceRequest(){
        RestHighLevelClient esRestClient = getESRestClient();

        GetSourceRequest getSourceRequest = new GetSourceRequest(INDEX_USER_INFO, DOCUMENT_ID);

        try {
            GetSourceResponse getSourceResponse = esRestClient.getSource(getSourceRequest, RequestOptions.DEFAULT);
            Map<String, Object> source = getSourceResponse.getSource();
            log.info("通过id:{}，获取的数据：{}" , getSourceRequest.id() , source);
        }catch (Exception e){
            e.printStackTrace();
            log.info("通过id查询数据失败！！！");
        }

    }

    @Test
    public void existIndexTest(){
        RestHighLevelClient esRestClient = getESRestClient();
        GetRequest getRequest = new GetRequest(INDEX_USER_INFO, DOCUMENT_ID);
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        try {
            boolean exists = esRestClient.exists(getRequest, RequestOptions.DEFAULT);
            if (exists){
                log.info("{}索引下存在id为{}的数据",INDEX_USER_INFO,DOCUMENT_ID);
            }else {
                log.info("{}索引下不存在id为{}的数据",INDEX_USER_INFO,DOCUMENT_ID);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void deleteRequestTest(){
        RestHighLevelClient esRestClient = getESRestClient();
        DeleteRequest deleteRequest = new DeleteRequest(INDEX_USER_INFO, DOCUMENT_ID);
        deleteRequest.timeout(TimeValue.timeValueMillis(TIME_OUT));
        deleteRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);

        try {
            DeleteResponse delete = esRestClient.delete(deleteRequest, RequestOptions.DEFAULT);
            RestStatus status = delete.status();
//            删除操作结果的判断
            if (status.equals(RestStatus.OK)){
                log.info("删除索引{}下id为{}的数据，删除成功！！！",INDEX_USER_INFO , DOCUMENT_ID);
            }else {
                log.info("删除索引{}下id为{}的数据，删除失败！！！",INDEX_USER_INFO , DOCUMENT_ID);
            }
            ReplicationResponse.ShardInfo shardInfo = delete.getShardInfo();
            int total = shardInfo.getTotal();
            int successful = shardInfo.getSuccessful();
//            if (total == successful){
//                log.info("删除索引{}下id为{}的数据，删除失败！！！",INDEX_USER_INFO , DOCUMENT_ID);
//            }
            int failed = shardInfo.getFailed();
            if (failed>0) {
                ReplicationResponse.ShardInfo.Failure[] failures = shardInfo.getFailures();
                for (ReplicationResponse.ShardInfo.Failure failure : failures) {
                    String reason = failure.reason();
                    log.info("失败的原因：{}", reason);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            log.info("删除索引{}下id为{}的数据，删除失败！！！",INDEX_USER_INFO , DOCUMENT_ID);

        }
    }

    @Test
    public void updateRequestTest(){
        RestHighLevelClient esRestClient = getESRestClient();

        ClusterClient cluster = esRestClient.cluster();
        IndicesClient indices = esRestClient.indices();

        UpdateRequest updateRequest = new UpdateRequest(INDEX_USER_INFO, DOCUMENT_ID);
//        UpdateRequest updateRequest = new UpdateRequest(INDEX_USER_INFO, String.valueOf(100));

        HashMap<String, Object> map = new HashMap<>();
        map.put("userName","peanut_hzm");
        map.put("sex","男");

//        String updateJsonString = "{\"userName\":\"peanut_hzm\",\"sex\":\"男\"}";
//        updateRequest.upsert(updateJsonString , XContentType.JSON);

//        updateRequest.upsert(map);
        updateRequest.scriptedUpsert(true);
        updateRequest.doc(map);

        try {
            UpdateResponse updateResponse = esRestClient.update(updateRequest, RequestOptions.DEFAULT);
            RestStatus status = updateResponse.status();
            DocWriteResponse.Result result = updateResponse.getResult();
            if (result.equals(DocWriteResponse.Result.CREATED)){

            }else if (result.equals(DocWriteResponse.Result.UPDATED)){

            }else if (result.equals(DocWriteResponse.Result.DELETED)){

            }else if (result.equals(DocWriteResponse.Result.NOOP)){

            }
            if (status.equals(RestStatus.OK)){
                log.info("更新索引{}下id为{}的数据，更新成功！！！",INDEX_USER_INFO , DOCUMENT_ID);
            }else {
                log.info("更新索引{}下id为{}的数据，更新失败！！！",INDEX_USER_INFO , DOCUMENT_ID);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.info("更新索引{}下id为{}的数据，更新失败！！！",INDEX_USER_INFO , DOCUMENT_ID);
        }
    }

    @Test
    public void termVectorsRequestTest(){
        RestHighLevelClient esRestClient = getESRestClient();
        try (XContentBuilder jsonBuilder = XContentFactory.jsonBuilder()) {
            jsonBuilder.startObject()
                    .field("userName","peanut")
                    .field("sex", "女")
                    .startObject("workExperience")
                    .field("companyName","阿里巴巴")
                    .field("workYear","5年")
                    .field("level","P9")
                    .field("school",new String[]{"哈佛大学","华中科技大学","北京大学","清华大学"})
                    .endObject()
                    .endObject();
            TermVectorsRequest termVectorsRequest = new TermVectorsRequest(INDEX_USER_INFO, jsonBuilder);
            TermVectorsResponse termVectorsResponse = esRestClient.termvectors(termVectorsRequest, RequestOptions.DEFAULT);
            String id = termVectorsRequest.getId();
            String[] fields = termVectorsRequest.getFields();
            List<TermVectorsResponse.TermVector> termVectorsList = termVectorsResponse.getTermVectorsList();
            for (TermVectorsResponse.TermVector termVector : termVectorsList) {
                String fieldName = termVector.getFieldName();
                log.info("当前查询的属性名称：{}" + fieldName);
                int docCount = termVector.getFieldStatistics().getDocCount();
                log.info("包含当前关键字的文档数：{}" , docCount);
                long sumDocFreq = termVector.getFieldStatistics().getSumDocFreq();
                log.info("当前关键字查询的sumDocFreq：{}" , sumDocFreq);
                long sumTotalTermFreq = termVector.getFieldStatistics().getSumTotalTermFreq();
                log.info("当前关键字查询的sumTotalTermFreq：{}" , sumTotalTermFreq);
                List<TermVectorsResponse.TermVector.Term> terms = termVector.getTerms();
                for (TermVectorsResponse.TermVector.Term term : terms) {
                    Integer docFreq = term.getDocFreq();
                    log.info("当前关键字查询的docFreq：{}" , docFreq);
                    int termFreq = term.getTermFreq();
                    log.info("当前关键字查询的termFreq：{}" , termFreq);
                    Float score = term.getScore();
                    log.info("当前关键字查询的score：{}" , score);
                    Long totalTermFreq = term.getTotalTermFreq();
                    log.info("当前关键字查询的totalTermFreq：{}" , totalTermFreq);
                    String result = term.getTerm();
                    log.info("termVectorRequest查询结果：{}" , result);
                    List<TermVectorsResponse.TermVector.Token> tokens = term.getTokens();
                    for (TermVectorsResponse.TermVector.Token token : tokens) {
                        Integer startOffset = token.getStartOffset();
                        log.info("当前关键字查询的startOffset：{}" , startOffset);
                        String payload = token.getPayload();
                        log.info("当前关键字查询的payload：{}" , payload);
                        Integer endOffset = token.getEndOffset();
                        log.info("当前关键字查询的endOffset：{}" , endOffset);
                        Integer position = token.getPosition();
                        log.info("当前关键字查询的position：{}" , position);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void bulkRequestTest() {
        long startTime = System.currentTimeMillis();

        BulkRequest bulkRequest = new BulkRequest();
        String[] nameArray = {"hua zhong min", "peanut", "peter", "zhangsan", "lisi", "tom", "bob", "li xiao lu","王五","赵柳"};
        String[] addressArray = {"广东省深圳市福田区", "湖北省武汉市洪山区", "北京市朝阳区", "上海市青浦区", "香港特别行政区", "海外"};
        String[] companyArray = {"阿里巴巴吧", "平安科技", "京东", "美团", "腾讯科技", "微软", "金蝶中国有限公司","字节跳动"};
        for (int i = 2000; i < 3000; i++) {
            IndexRequest indexRequest = new IndexRequest(INDEX_USER_INFO);
            try {
                XContentBuilder jsonBuilder = XContentFactory.jsonBuilder();
                jsonBuilder.startObject();
                {
                    jsonBuilder.field("id", i);
                    jsonBuilder.field("userName", nameArray[i % nameArray.length]);
                    if (i % 2 == 0) {
                        jsonBuilder.field("sex", "男");
                    } else {
                        jsonBuilder.field("sex", "女");
                    }
                    jsonBuilder.field("address", addressArray[i % addressArray.length]);
                    jsonBuilder.field("height", 175);
                    jsonBuilder.field("weight", 137.4);
                    jsonBuilder.field("role", "super manager");
                    jsonBuilder.field("isMarried", false);
                    jsonBuilder.field("isGraduated", true);
                    jsonBuilder.startObject("workExperience");
                    {
                        jsonBuilder.field("companyName", companyArray[i % companyArray.length]);
                        jsonBuilder.field("workYear", "5年");
                        jsonBuilder.field("level", "P9");
                        jsonBuilder.field("joinDate", System.currentTimeMillis());
                    }
                    jsonBuilder.endObject();
                    jsonBuilder.field("school", new String[]{"哈佛大学", "湖北大学", "武汉大学", "华中科技大学", "北京大学", "清华大学"});
                }
                jsonBuilder.endObject();
                indexRequest.id(String.valueOf(i));
                indexRequest.source(jsonBuilder);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //        设置路由策略
            indexRequest.routing("routing");
            //        设置请求超时时间
            indexRequest.timeout(TimeValue.timeValueMillis(TIME_OUT));
            //        设置刷新策略
//            indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            //        设置数据的版本
            //        indexRequest.version(10);
            //        设置数据的类型
            //        indexRequest.versionType(VersionType.INTERNAL);
            //        设置索引操作类型
            indexRequest.opType(DocWriteRequest.OpType.INDEX);
            bulkRequest.add(indexRequest);
        }
        RestHighLevelClient esRestClient = getESRestClient();

        try {
            UpdateRequest updateRequest = new UpdateRequest(INDEX_USER_INFO, UPDATE_DOCUMENT_ID);
            XContentBuilder jsonBuilder = XContentFactory.jsonBuilder();
            String uuid = GenerateIdUtils.getUUID();
            jsonBuilder.startObject();
            {
                jsonBuilder.field("id", UPDATE_DOCUMENT_ID);
                jsonBuilder.field("userName", "花生米");
                jsonBuilder.field("sex", "男");
                jsonBuilder.field("address", "浙江省杭州市西湖区");
                jsonBuilder.field("height", 175);
                jsonBuilder.field("weight", 137.4);
                jsonBuilder.field("role", "super manager");
                jsonBuilder.field("isMarried", false);
                jsonBuilder.field("isGraduated", true);
                jsonBuilder.startObject("workExperience");
                {
                    jsonBuilder.field("companyName", "字节跳动");
                    jsonBuilder.field("workYear", "5年");
                    jsonBuilder.field("level", "P9");
                    jsonBuilder.field("joinDate", System.currentTimeMillis());
                }
                jsonBuilder.endObject();
                jsonBuilder.field("school", new String[]{"哈佛大学", "湖北大学", "武汉大学", "华中科技大学", "北京大学", "清华大学"});
            }
            jsonBuilder.endObject();
            updateRequest.id(String.valueOf(500));
            updateRequest.doc(jsonBuilder);
            bulkRequest.add(updateRequest);

            DeleteRequest deleteRequest = new DeleteRequest(INDEX_USER_INFO, DELETE_DOCUMENT_ID);
            bulkRequest.add(deleteRequest);

        }catch (Exception e){
            e.printStackTrace();
        }

        //upsert的用法
        /*try {
            UpdateRequest updateRequest = new UpdateRequest(INDEX_USER_INFO, "5000");
            XContentBuilder jsonBuilder = XContentFactory.jsonBuilder();
            String uuid = GenerateIdUtils.getUUID();
            jsonBuilder.startObject();
            {
                jsonBuilder.field("id", uuid);
                jsonBuilder.field("userName", "花生米");
                jsonBuilder.field("sex", "男");
                jsonBuilder.field("address", "浙江省杭州市西湖区");
                jsonBuilder.field("height", 175);
                jsonBuilder.field("weight", 137.4);
                jsonBuilder.field("role", "super manager");
                jsonBuilder.field("isMarried", false);
                jsonBuilder.field("isGraduated", true);
                jsonBuilder.startObject("workExperience");
                {
                    jsonBuilder.field("companyName", "字节跳动");
                    jsonBuilder.field("workYear", "5年");
                    jsonBuilder.field("level", "P9");
                    jsonBuilder.field("joinDate", System.currentTimeMillis());
                }
                jsonBuilder.endObject();
                jsonBuilder.field("school", new String[]{"哈佛大学", "湖北大学", "武汉大学", "华中科技大学", "北京大学", "清华大学"});
            }
            jsonBuilder.endObject();
            updateRequest.upsert(jsonBuilder);
            bulkRequest.add(updateRequest);
        }catch (Exception e){
            e.printStackTrace();
        }*/

        try {
            BulkResponse bulkResponse = esRestClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (bulkResponse.hasFailures()){
                for (BulkItemResponse bulkItemResponse : bulkResponse) {
                    if (bulkItemResponse.isFailed()){
                        BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                        String failureMessage = bulkItemResponse.getFailureMessage();
                        String id = bulkItemResponse.getId();
                        DocWriteRequest.OpType opType = bulkItemResponse.getOpType();
                        log.info("批量操作中失败的数据：{}，操作类型：{}，失败的message：{}", id ,opType.toString(),failureMessage);
                    }
                }
            }else {
                log.info("批量操作数据成功！！！！！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        log.info("批量操作索引耗时：{}" , (System.currentTimeMillis() - startTime));
    }

    @Test
    public void multiGetRequestTest(){
        RestHighLevelClient esRestClient = getESRestClient();
        //通过id批量查询
        MultiGetRequest multiGetRequest = new MultiGetRequest();
        for (int i = 0; i < 30; i++) {
            multiGetRequest.add(new MultiGetRequest.Item(INDEX_USER_INFO , String.valueOf(i)));
        }
        try {
            MultiGetResponse multiGetResponse = esRestClient.mget(multiGetRequest, RequestOptions.DEFAULT);
            MultiGetItemResponse[] responses = multiGetResponse.getResponses();
            for (MultiGetItemResponse getItemResponse : responses) {
                GetResponse response = getItemResponse.getResponse();
                String id = response.getId();
                Map<String, Object> source = response.getSource();
                log.info("批量查询的id：{}，数据：{}",id,source);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void reindexRequestTest(){
        //复制一个索引以及其中的数据，索引不存在就创建，存在则不会变化
        RestHighLevelClient esRestClient = getESRestClient();
        ReindexRequest reindexRequest = new ReindexRequest();
        reindexRequest.setSourceIndices(INDEX_ADMIN);
        reindexRequest.setDestIndex(INDEX_MANAGER_INFO);
        reindexRequest.setDestVersionType(VersionType.INTERNAL);
        reindexRequest.setDestOpType("create");
        try {
            BulkByScrollResponse response = esRestClient.reindex(reindexRequest, RequestOptions.DEFAULT);
            List<BulkItemResponse.Failure> bulkFailures = response.getBulkFailures();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void updateByQueryRequest(){
        RestHighLevelClient esRestClient = getESRestClient();
        UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest(INDEX_USER_INFO,INDEX_MANAGER_INFO);
        updateByQueryRequest.setQuery(new TermQueryBuilder("userName","peter"));
        updateByQueryRequest.setMaxDocs(20);
        try {
            BulkByScrollResponse bulkByScrollResponse = esRestClient.updateByQuery(updateByQueryRequest, RequestOptions.DEFAULT);
            int batches = bulkByScrollResponse.getBatches();
            List<BulkItemResponse.Failure> bulkFailures = bulkByScrollResponse.getBulkFailures();
            long bulkRetries = bulkByScrollResponse.getBulkRetries();
            long created = bulkByScrollResponse.getCreated();
            long deleted = bulkByScrollResponse.getDeleted();
            String reasonCancelled = bulkByScrollResponse.getReasonCancelled();
            List<ScrollableHitSource.SearchFailure> searchFailures = bulkByScrollResponse.getSearchFailures();
            long searchRetries = bulkByScrollResponse.getSearchRetries();
            BulkByScrollTask.Status status = bulkByScrollResponse.getStatus();
            log.info("操作返回参数：{}",created);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
