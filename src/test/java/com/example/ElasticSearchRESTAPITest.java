package com.example;

import com.example.demo.model.Product;
import com.example.demo.utils.CommonUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
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
import org.elasticsearch.action.search.*;
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
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequest;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.InternalAggregation;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
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

    volatile boolean flag = false;

    private final static String HOSTNAME = "127.0.0.1";

    private final static int PORT = 10201;

    private final static int ES_SHARDS = 5;

    private final static int ES_REPLICAS = 4;

    private final static int TIME_OUT = 3000;

    private final static int PAGE_SIZE_MAX = 1000;

    private final static int PAGE_NUM_START = 1;

    private final static String SCHEMA = "http";

    private final static String DOCUMENT_ID = "100";

    private final static String UPDATE_DOCUMENT_ID = "500";

    private final static String DELETE_DOCUMENT_ID = "50";

    private final static String INDEX_USER_INFO = "user_info";

    private final static String INDEX_MANAGER_INFO = "manager_info";

    private final static String INDEX_MANAGER = "manager";

    private final static String INDEX_CUSTOMER_INFO = "customer_info";

    private final static String INDEX_CUSTOMER = "customer";

    private final static String INDEX_ADMIN = "administrator_info";

    private final static String INDEX_PRODUCT = "product";

    private final static String INDEX_OPERATE_LOG = "operate_log";

    private final static String INDEX_MAPPING = "mappings";

    private final static String INDEX_MAPPING_PROPERTIES = "properties";

    private final static String INDEX_SETTINGS = "settings";

    private final static String INDEX_SETTINGS_SHARDS = "number_of_shards";

    private final static String INDEX_SETTINGS_REPLICAS = "number_of_replicas";

    private final static String ANALYZER = "analyzer";

    private final static String ANALYZER_IK_MAX = "ik_max_word";

    private final static String ANALYZER_IK_SMART = "ik_max_word";

    private final static RequestOptions requestOptions = RequestOptions.DEFAULT;

    private String[] nameArray = new String[]{"张毅", "李二", "赵三", "王五", "陆佰", "peanut", "John Smith", "Thomas Kone Ken", "ZhengHe Yang", "cheng dou dou",
            "韩麒麟", "peanut hua", "Jhon Richard Tang", " Jhon Thomas", "Peter Jhon Smith", "Thomas Tom Jhon", "Peter Thomas", "Richard Thomas"};

    private String[] addressArray = new String[]{"广东省深圳市福田区景田路", "广东省深圳市南山区高新南十二路", "湖北省武汉市武昌区友谊大道",
            "湖北省武汉市洪山区光谷大道", "北京市朝阳区北京路", "香港特别行政区九龙街", "新疆乌鲁木齐市水磨沟区", "浙江省杭州市西湖区玄武大道", "海南省海口市观海区北京路"};

    private String[] schoolArray = new String[]{"北京大学", "清华大学", "湖北大学", "武汉大学", "华中科技大学", "Cambridge University",
            "Harvard University", "Boston University", "华中师范大学", "湖南大学"};

    private String[] companyArray = new String[]{"阿里巴巴", "腾讯科技", "字节跳动", "平安科技", "金蝶中国", "网易", "拼多多",
            "Google", "Microsoft", "华为有限责任公司", "美团", "百度科技"};

    private String[] roleArray = new String[]{"normal user", "VIP user", "SVIP user", "visitor", "first level manager", "second level manager", "third level manager",
            "forth level manager", "fifth level manager", "first level admin", "second level admin", "third level admin"};

    private String[] sexArray = new String[]{"男", "女", "保密"};

    private int[] workYearArray = new int[]{0, 1, 2, 3, 4, 5, 10};

    private double[] salaryYearArray = new double[]{15.0, 15.5, 16.5, 16.0, 16.8, 19.5, 19.8, 20.1, 20.5, 21.0, 25.0, 23.8};

    private long[] phoneArray = new long[]{13377652763L, 15722764893L, 19928646533L, 13098477736L, 17765367265L, 15927638923L, 13977652973L, 19976382784L,
            15922673888L, 16637826528L, 17763926328L, 13698376328L, 13397378923L, 13378297283L, 15528903784L, 16397287356L, 13688922873L, 13377668888L};

    private String[] placeArray = new String[]{"深圳", "北京", "上海", "广州", "杭州", "武汉", "南京", "成都", "长沙", "西安", "重庆", "厦门"};

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
        product.setId(CommonUtils.getUUID());
        product.setName("test" + 90000000);
        product.setCreateTime(new Date());
        product.setUpdateTime(new Date());
        product.setPrice(CommonUtils.getNumberic());
        product.setType(CommonUtils.getType());

        try {
            File file = new File("E:\\Program Files\\elasticsearch\\elasticsearch-7.10.1-node-3\\plugins\\elasticsearch-analysis-ik-7.10.1\\config\\IKAnalyzer.cfg.xml");
            FileInputStream fileInputStream = new FileInputStream(file);
            int read = fileInputStream.read(new byte[2048]);
            FileReader fileReader = new FileReader(file);
            int read1 = fileReader.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info(product.toString());
    }


    public RestHighLevelClient getESRestClient() {
//        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost(HOSTNAME, PORT, SCHEMA)));
        return new RestHighLevelClient(RestClient.builder(new HttpHost(HOSTNAME, PORT, SCHEMA)));
    }


    private HashMap<String, Object> getMap(String key, Object value) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(key, value);
        if ("name".equals(key) || "address".equals(key) || "school".equals(key)) {
            map.put(ANALYZER, ANALYZER_IK_SMART);
        }
        return map;
    }


    @Test
    public void createIndexRequestTest() {
        RestHighLevelClient esRestClient = getESRestClient();
        String indexName = INDEX_CUSTOMER;
        GetIndexRequest getRequest = new GetIndexRequest(indexName);
        try {
            boolean exists = esRestClient.indices().exists(getRequest, requestOptions);
            if (exists) {
                GetIndexResponse getIndexResponse = esRestClient.indices().get(getRequest, requestOptions);
                Map<String, MappingMetadata> mappings = getIndexResponse.getMappings();
                DeleteIndexRequest deleteRequest = new DeleteIndexRequest(INDEX_MANAGER_INFO);
                AcknowledgedResponse delete = esRestClient.indices().delete(deleteRequest, requestOptions);
                if (delete.isAcknowledged()) {
                    log.info("索引：{}存在，已删除", indexName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        CreateIndexRequest customerInfoCreateIndexRequest = new CreateIndexRequest(indexName);
        HashMap<String, HashMap<String, Object>> properties = new HashMap<>();
        properties.put("id", getMap("type", "text"));
        properties.put("name", getMap("type", "text"));
        properties.put("phone", getMap("type", "long"));
        properties.put("age", getMap("type", "integer"));
        properties.put("email", getMap("type", "text"));
        properties.put("assets", getMap("type", "double"));
        properties.put("address", getMap("type", "text"));
        properties.put("isMarried", getMap("type", "boolean"));
        properties.put("isGraduated", getMap("type", "boolean"));
        properties.put("createTime", getMap("type", "date"));
        properties.put("updateTime", getMap("type", "date"));
        properties.put("workExperience", getMap("type", "object"));
        properties.put("workExperience.yearCount", getMap("type", "integer"));
        properties.put("workExperience.companyCount", getMap("type", "integer"));
        properties.put("workExperience.currentCompany", getMap("type", "keyword"));
        properties.put("workExperience.currentSalary", getMap("type", "double"));
        properties.put("introduction", getMap("type", "keyword"));
        //索引设置方式一 JsonBuilder
        try (XContentBuilder jsonBuilder = XContentFactory.jsonBuilder()) {
            jsonBuilder.startObject()
                    .startObject(INDEX_MAPPING).field(INDEX_MAPPING_PROPERTIES, properties).endObject()
                    .startObject(INDEX_SETTINGS).field(INDEX_SETTINGS_SHARDS, ES_SHARDS).field(INDEX_SETTINGS_REPLICAS, ES_REPLICAS).endObject()
                    .endObject();

            customerInfoCreateIndexRequest.source(jsonBuilder);
            esRestClient.indices().create(customerInfoCreateIndexRequest, requestOptions);
        } catch (IOException e) {
            log.info("创建索引：{}失败", indexName);
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
            esRestClient.indices().create(managerInfoCreateIndex,requestOptions);
        }catch (Exception e){
            e.printStackTrace();
            log.info("创建索引：{}失败",INDEX_MANAGER_INFO);
        }
*/
    }

    @Test
    public void indexRequestTest() {
        RestHighLevelClient restHighLevelClient = getESRestClient();
        long startTime = System.currentTimeMillis();
        IndexRequest indexRequest = new IndexRequest(INDEX_CUSTOMER_INFO);
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
        for (int i = 0; i < 10000000; i++) {
            try {
                XContentBuilder jsonBuilder = XContentFactory.jsonBuilder();
                String uuid = CommonUtils.getUUID();
                jsonBuilder.startObject();
                {
                    jsonBuilder.field("id", uuid);
                    jsonBuilder.field("name", nameArray[i % nameArray.length] + CommonUtils.getType());
                    jsonBuilder.field("sex", sexArray[i % sexArray.length]);
                    jsonBuilder.field("address", addressArray[i % 6]);
                    jsonBuilder.field("height", CommonUtils.getNumberic().doubleValue());
                    jsonBuilder.field("weight", CommonUtils.getNumberic().doubleValue());
                    jsonBuilder.field("role", roleArray[i % roleArray.length]);

                    jsonBuilder.field("createTime", CommonUtils.getRandomTime(-CommonUtils.getRandomDigital(1000000000L)));
                    jsonBuilder.field("updateTime", CommonUtils.getRandomTime(CommonUtils.getRandomDigital(1000000000L)));
                    jsonBuilder.field("isMarried", Integer.parseInt(CommonUtils.getType()) > 5);
                    jsonBuilder.field("isGraduated", Integer.parseInt(CommonUtils.getType()) > 5);
                    jsonBuilder.startObject("workExperience");
                    {
                        jsonBuilder.field("companyName", companyArray[i % 7]);
                        jsonBuilder.field("workYear", workYearArray[i % workYearArray.length]);
                        jsonBuilder.field("level", Integer.parseInt(CommonUtils.getType()));
                        jsonBuilder.field("joinDate", CommonUtils.getRandomTime(CommonUtils.getRandomDigital(1000000000L)));
                    }
                    jsonBuilder.endObject();
                    jsonBuilder.field("school", schoolArray[i % schoolArray.length]);
                }
                jsonBuilder.endObject();
                indexRequest.source(jsonBuilder);
            } catch (Exception e) {
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
                IndexResponse indexResponse = restHighLevelClient.index(indexRequest, requestOptions);
                log.info("索引创建成功：{} , 数据id:{}", indexResponse.getIndex(), indexResponse.getId());
                //            Cancellable indexAsync = restHighLevelClient.indexAsync(indexRequest, requestOptions, new ESActionListener());
            } catch (Exception e) {
                e.printStackTrace();
                log.info("索引创建失败！！！！");
            }
        }
        //4m44s757ms
        log.info("循环操作1000条数据耗时：{}", (System.currentTimeMillis() - startTime));

    }

    @Test
    public void getIndexRequestTest() {
        RestHighLevelClient esRestClient = getESRestClient();
        GetRequest getRequest = new GetRequest(INDEX_USER_INFO, "1");
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
            GetResponse getResponse = esRestClient.get(getRequest, requestOptions);
            String sourceAsString = getResponse.getSourceAsString();
            log.info("通过id:{}，获取的数据：{}", getRequest.id(), sourceAsString);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("通过id查询数据失败！！！");
        }
        /**
         * {"isMarried":false,"address":"北京市朝阳区","role":"super manager","school":["哈佛大学","湖北大学","武汉大学","华中科技大学","北京大学","清华大学"],"sex":"男","workExperience":{"companyName":"美团","workYear":"5年","level":"P9","joinDate":1608864478490},"weight":137.4,"id":"80F2CCA1C7BB49BFA40B9B67FFB79F80","userName":"Peter Green","height":175}
         */
    }

    @Test
    public void getSourceRequest() {
        RestHighLevelClient esRestClient = getESRestClient();

        GetSourceRequest getSourceRequest = new GetSourceRequest(INDEX_USER_INFO, DOCUMENT_ID);

        try {
            GetSourceResponse getSourceResponse = esRestClient.getSource(getSourceRequest, requestOptions);
            Map<String, Object> source = getSourceResponse.getSource();
            log.info("通过id:{}，获取的数据：{}", getSourceRequest.id(), source);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("通过id查询数据失败！！！");
        }

    }

    @Test
    public void existIndexTest() {
        RestHighLevelClient esRestClient = getESRestClient();
        GetRequest getRequest = new GetRequest(INDEX_USER_INFO, DOCUMENT_ID);
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        try {
            boolean exists = esRestClient.exists(getRequest, requestOptions);
            if (exists) {
                log.info("{}索引下存在id为{}的数据", INDEX_USER_INFO, DOCUMENT_ID);
            } else {
                log.info("{}索引下不存在id为{}的数据", INDEX_USER_INFO, DOCUMENT_ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteRequestTest() {
        RestHighLevelClient esRestClient = getESRestClient();
        DeleteRequest deleteRequest = new DeleteRequest(INDEX_USER_INFO, DOCUMENT_ID);
        deleteRequest.timeout(TimeValue.timeValueMillis(TIME_OUT));
        deleteRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);

        try {
            DeleteResponse delete = esRestClient.delete(deleteRequest, requestOptions);
            RestStatus status = delete.status();
//            删除操作结果的判断
            if (status.equals(RestStatus.OK)) {
                log.info("删除索引{}下id为{}的数据，删除成功！！！", INDEX_USER_INFO, DOCUMENT_ID);
            } else {
                log.info("删除索引{}下id为{}的数据，删除失败！！！", INDEX_USER_INFO, DOCUMENT_ID);
            }
            ReplicationResponse.ShardInfo shardInfo = delete.getShardInfo();
            int total = shardInfo.getTotal();
            int successful = shardInfo.getSuccessful();
//            if (total == successful){
//                log.info("删除索引{}下id为{}的数据，删除失败！！！",INDEX_USER_INFO , DOCUMENT_ID);
//            }
            int failed = shardInfo.getFailed();
            if (failed > 0) {
                ReplicationResponse.ShardInfo.Failure[] failures = shardInfo.getFailures();
                for (ReplicationResponse.ShardInfo.Failure failure : failures) {
                    String reason = failure.reason();
                    log.info("失败的原因：{}", reason);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("删除索引{}下id为{}的数据，删除失败！！！", INDEX_USER_INFO, DOCUMENT_ID);

        }
    }

    @Test
    public void updateRequestTest() {
        RestHighLevelClient esRestClient = getESRestClient();

        ClusterClient cluster = esRestClient.cluster();
        IndicesClient indices = esRestClient.indices();

        UpdateRequest updateRequest = new UpdateRequest(INDEX_USER_INFO, DOCUMENT_ID);
//        UpdateRequest updateRequest = new UpdateRequest(INDEX_USER_INFO, String.valueOf(100));

        HashMap<String, Object> map = new HashMap<>();
        map.put("userName", "peanut_hzm");
        map.put("sex", "男");

//        String updateJsonString = "{\"userName\":\"peanut_hzm\",\"sex\":\"男\"}";
//        updateRequest.upsert(updateJsonString , XContentType.JSON);

//        updateRequest.upsert(map);
        updateRequest.scriptedUpsert(true);
        updateRequest.doc(map);

        try {
            UpdateResponse updateResponse = esRestClient.update(updateRequest, requestOptions);
            RestStatus status = updateResponse.status();
            DocWriteResponse.Result result = updateResponse.getResult();
            if (result.equals(DocWriteResponse.Result.CREATED)) {

            } else if (result.equals(DocWriteResponse.Result.UPDATED)) {

            } else if (result.equals(DocWriteResponse.Result.DELETED)) {

            } else if (result.equals(DocWriteResponse.Result.NOOP)) {

            }
            if (status.equals(RestStatus.OK)) {
                log.info("更新索引{}下id为{}的数据，更新成功！！！", INDEX_USER_INFO, DOCUMENT_ID);
            } else {
                log.info("更新索引{}下id为{}的数据，更新失败！！！", INDEX_USER_INFO, DOCUMENT_ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("更新索引{}下id为{}的数据，更新失败！！！", INDEX_USER_INFO, DOCUMENT_ID);
        }
    }

    @Test
    public void termVectorsRequestTest() {
        RestHighLevelClient esRestClient = getESRestClient();
        try (XContentBuilder jsonBuilder = XContentFactory.jsonBuilder()) {
            jsonBuilder.startObject()
                    .field("userName", "peanut")
                    .field("sex", "女")
                    .startObject("workExperience")
                    .field("companyName", "阿里巴巴")
                    .field("workYear", "5年")
                    .field("level", "P9")
                    .field("school", new String[]{"哈佛大学", "华中科技大学", "北京大学", "清华大学"})
                    .endObject()
                    .endObject();
            TermVectorsRequest termVectorsRequest = new TermVectorsRequest(INDEX_USER_INFO, jsonBuilder);
            TermVectorsResponse termVectorsResponse = esRestClient.termvectors(termVectorsRequest, requestOptions);
            String id = termVectorsRequest.getId();
            String[] fields = termVectorsRequest.getFields();
            List<TermVectorsResponse.TermVector> termVectorsList = termVectorsResponse.getTermVectorsList();
            for (TermVectorsResponse.TermVector termVector : termVectorsList) {
                String fieldName = termVector.getFieldName();
                log.info("当前查询的属性名称：{}" + fieldName);
                int docCount = termVector.getFieldStatistics().getDocCount();
                log.info("包含当前关键字的文档数：{}", docCount);
                long sumDocFreq = termVector.getFieldStatistics().getSumDocFreq();
                log.info("当前关键字查询的sumDocFreq：{}", sumDocFreq);
                long sumTotalTermFreq = termVector.getFieldStatistics().getSumTotalTermFreq();
                log.info("当前关键字查询的sumTotalTermFreq：{}", sumTotalTermFreq);
                List<TermVectorsResponse.TermVector.Term> terms = termVector.getTerms();
                for (TermVectorsResponse.TermVector.Term term : terms) {
                    Integer docFreq = term.getDocFreq();
                    log.info("当前关键字查询的docFreq：{}", docFreq);
                    int termFreq = term.getTermFreq();
                    log.info("当前关键字查询的termFreq：{}", termFreq);
                    Float score = term.getScore();
                    log.info("当前关键字查询的score：{}", score);
                    Long totalTermFreq = term.getTotalTermFreq();
                    log.info("当前关键字查询的totalTermFreq：{}", totalTermFreq);
                    String result = term.getTerm();
                    log.info("termVectorRequest查询结果：{}", result);
                    List<TermVectorsResponse.TermVector.Token> tokens = term.getTokens();
                    for (TermVectorsResponse.TermVector.Token token : tokens) {
                        Integer startOffset = token.getStartOffset();
                        log.info("当前关键字查询的startOffset：{}", startOffset);
                        String payload = token.getPayload();
                        log.info("当前关键字查询的payload：{}", payload);
                        Integer endOffset = token.getEndOffset();
                        log.info("当前关键字查询的endOffset：{}", endOffset);
                        Integer position = token.getPosition();
                        log.info("当前关键字查询的position：{}", position);
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

        for (int i = 0; i < 1000; i++) {
            BulkRequest bulkRequest = new BulkRequest();
            for (int j = i * 100000; j < (i + 1) * 100000; j++) {
                IndexRequest indexRequest = new IndexRequest(INDEX_CUSTOMER);
                try {
                    XContentBuilder jsonBuilder = XContentFactory.jsonBuilder();
                    jsonBuilder.startObject();
                    {
                        jsonBuilder.field("id", j);
                        jsonBuilder.field("name", nameArray[i % nameArray.length] + CommonUtils.getType());
                        jsonBuilder.field("sex", sexArray[i % sexArray.length]);
                        jsonBuilder.field("address", addressArray[i % 6]);
                        jsonBuilder.field("height", CommonUtils.getNumberic().doubleValue());
                        jsonBuilder.field("weight", CommonUtils.getNumberic().doubleValue());
                        jsonBuilder.field("role", roleArray[i % roleArray.length]);

                        jsonBuilder.field("createTime", CommonUtils.getRandomTime(-CommonUtils.getRandomDigital(1000000000L)));
                        jsonBuilder.field("updateTime", CommonUtils.getRandomTime(CommonUtils.getRandomDigital(1000000000L)));
                        jsonBuilder.field("isMarried", Integer.parseInt(CommonUtils.getType()) > 5);
                        jsonBuilder.field("isGraduated", Integer.parseInt(CommonUtils.getType()) > 5);
                        jsonBuilder.startObject("workExperience");
                        {
                            jsonBuilder.field("companyName", companyArray[i % 7]);
                            jsonBuilder.field("workYear", workYearArray[i % workYearArray.length]);
                            jsonBuilder.field("level", Integer.parseInt(CommonUtils.getType()));
                            jsonBuilder.field("joinDate", CommonUtils.getRandomTime(CommonUtils.getRandomDigital(1000000000L)));
                        }
                        jsonBuilder.endObject();
                        jsonBuilder.field("school", schoolArray[i % schoolArray.length]);
                    }
                    jsonBuilder.endObject();
                    indexRequest.id(String.valueOf(j));
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

        /*try {
            UpdateRequest updateRequest = new UpdateRequest(INDEX_USER_INFO, UPDATE_DOCUMENT_ID);
            XContentBuilder jsonBuilder = XContentFactory.jsonBuilder();
            String uuid = CommonUtils.getUUID();
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
        }*/

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
                RestHighLevelClient esRestClient = getESRestClient();

                bulkRequest.timeout(TimeValue.timeValueHours(1));
                bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
                BulkResponse bulkResponse = esRestClient.bulk(bulkRequest, requestOptions);
                if (bulkResponse.hasFailures()) {
                    for (BulkItemResponse bulkItemResponse : bulkResponse) {
                        if (bulkItemResponse.isFailed()) {
                            BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                            String failureMessage = bulkItemResponse.getFailureMessage();
                            String id = bulkItemResponse.getId();
                            DocWriteRequest.OpType opType = bulkItemResponse.getOpType();
                            log.info("批量操作中失败的数据：{}，操作类型：{}，失败的message：{}", id, opType.toString(), failureMessage);
                        }
                    }
                } else {
                    log.info("批量操作数据成功！！！！！");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info("批量操作索引耗时：{}", (System.currentTimeMillis() - startTime));
        }
    }

    @Test
    public void multiGetRequestTest() {
        RestHighLevelClient esRestClient = getESRestClient();
        //通过id批量查询
        MultiGetRequest multiGetRequest = new MultiGetRequest();
        for (int i = 0; i < 30; i++) {
            multiGetRequest.add(new MultiGetRequest.Item(INDEX_USER_INFO, String.valueOf(i)));
        }
        try {
            MultiGetResponse multiGetResponse = esRestClient.mget(multiGetRequest, requestOptions);
            MultiGetItemResponse[] responses = multiGetResponse.getResponses();
            for (MultiGetItemResponse getItemResponse : responses) {
                GetResponse response = getItemResponse.getResponse();
                String id = response.getId();
                Map<String, Object> source = response.getSource();
                log.info("批量查询的id：{}，数据：{}", id, source);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void reindexRequestTest() {
        //复制一个索引以及其中的数据，索引不存在就创建，存在则把所有数据复制到新的索引中
        RestHighLevelClient esRestClient = getESRestClient();
        ReindexRequest reindexRequest = new ReindexRequest();
        reindexRequest.setSourceIndices(INDEX_USER_INFO);
        reindexRequest.setDestIndex(INDEX_MANAGER_INFO);
        reindexRequest.setRefresh(true);

//        reindexRequest.setDestVersionType(VersionType.INTERNAL);
//        reindexRequest.setDestOpType("create");
        try {
            ActionListener<BulkByScrollResponse> actionListener = new ActionListener<BulkByScrollResponse>() {
                @Override
                public void onResponse(BulkByScrollResponse bulkByScrollResponse) {
                    List<BulkItemResponse.Failure> bulkFailures = bulkByScrollResponse.getBulkFailures();
                    long deleted = bulkByScrollResponse.getDeleted();
                    TimeValue took = bulkByScrollResponse.getTook();
                    long total = bulkByScrollResponse.getTotal();
                    flag = true;
                    log.info("reindex数据{}成功！！！！", total);
                }

                @Override
                public void onFailure(Exception e) {
                    log.info("reindex操作数据失败，捕获异常：{}", e.getMessage());
                }
            };
            Cancellable cancellable = esRestClient.reindexAsync(reindexRequest, requestOptions, actionListener);

            while (true) {
                if (flag) {
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void updateByQueryRequest() {
        RestHighLevelClient esRestClient = getESRestClient();
        UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest(INDEX_USER_INFO, INDEX_MANAGER_INFO);
        updateByQueryRequest.setQuery(new TermQueryBuilder("userName", "peter"));
        updateByQueryRequest.setMaxDocs(20);
        try {
            BulkByScrollResponse bulkByScrollResponse = esRestClient.updateByQuery(updateByQueryRequest, requestOptions);
            int batches = bulkByScrollResponse.getBatches();
            List<BulkItemResponse.Failure> bulkFailures = bulkByScrollResponse.getBulkFailures();
            long bulkRetries = bulkByScrollResponse.getBulkRetries();
            long created = bulkByScrollResponse.getCreated();
            long deleted = bulkByScrollResponse.getDeleted();
            String reasonCancelled = bulkByScrollResponse.getReasonCancelled();
            List<ScrollableHitSource.SearchFailure> searchFailures = bulkByScrollResponse.getSearchFailures();
            long searchRetries = bulkByScrollResponse.getSearchRetries();
            BulkByScrollTask.Status status = bulkByScrollResponse.getStatus();
            log.info("操作返回参数：{}", created);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void searchRequestTest() {

        //获取ES查询的客户端
        RestHighLevelClient esRestClient = getESRestClient();
        //构建查询请求
        SearchRequest searchRequest = new SearchRequest(INDEX_USER_INFO);

        //构建查询请求条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //查询所有数据
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        //设置关键字查询条件 未使用中文分词器时，中文被默认按照每个汉字拆分作为关键字，做匹配查询
//        searchSourceBuilder.query(QueryBuilders.termQuery("userName","Jhon"));

        //默认分词之后根据关键字模糊查询，中文字段不分词也可模糊查询
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("address", "景田");
        matchQueryBuilder.fuzziness(Fuzziness.AUTO);
        matchQueryBuilder.prefixLength(3);
        matchQueryBuilder.maxExpansions(10);
        searchSourceBuilder.query(matchQueryBuilder);

        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field userNameField = new HighlightBuilder.Field("userName");
        userNameField.highlighterType("unified");
        highlightBuilder.field(userNameField);
        HighlightBuilder.Field addressField = new HighlightBuilder.Field("address");
        highlightBuilder.field(addressField);
        searchSourceBuilder.highlighter(highlightBuilder);

//        String[] excludeFields = new String[]{"isMarried","role"};

//        String[] includeFields = new String[]{"userName","address","company"};

//        searchSourceBuilder.fetchSource(Strings.EMPTY_ARRAY , excludeFields);
        //设置排序条件
        searchSourceBuilder.sort("height", SortOrder.DESC);

        //聚合查询
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("workExperience.level").field("workExperience.level");

        searchSourceBuilder.aggregation(aggregationBuilder);

        //设置分页参数
        searchSourceBuilder.from(3);
        //最大只能返回一万条数据，不做设置时默认返回一万条
        searchSourceBuilder.size(PAGE_SIZE_MAX);

        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = esRestClient.search(searchRequest, requestOptions);
            SearchHits hits = searchResponse.getHits();
            long value = hits.getTotalHits().value;
            log.info("查询到的数据有{}条", value);
            int count = 0;
            for (SearchHit hit : hits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                log.info(sourceAsMap.toString());
                count++;
            }
            log.info("分页查询的结果数据条数：{}", count);
            Aggregations aggregations = searchResponse.getAggregations();
            Map<String, Aggregation> asMap = aggregations.getAsMap();
            log.info(asMap.toString());
            Aggregation aggregation = asMap.get("workExperience.level");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void scrollSearchTest() {
        RestHighLevelClient esRestClient = getESRestClient();
        SearchRequest searchRequest = new SearchRequest(INDEX_USER_INFO);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.matchQuery("userName", "Jhon"));

        searchSourceBuilder.size(PAGE_SIZE_MAX);

        searchRequest.source(searchSourceBuilder);

        searchRequest.scroll(TimeValue.timeValueMinutes(1L));

        try {
            SearchResponse searchResponse = esRestClient.search(searchRequest, requestOptions);

            String scrollId = searchResponse.getScrollId();

            SearchHits hits = searchResponse.getHits();

            for (SearchHit hit : hits) {
                String sourceAsString = hit.getSourceAsString();
                log.info(sourceAsString);
            }
//            317983
            while (true) {
                SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId);
                searchScrollRequest.scroll(TimeValue.timeValueSeconds(30));
                SearchResponse response = esRestClient.scroll(searchScrollRequest, requestOptions);

                SearchHits hits1 = response.getHits();
                int count = 0;
                for (SearchHit documentFields : hits1) {
                    String sourceAsString = documentFields.getSourceAsString();
                    count++;
                    log.info(sourceAsString);
                }
                log.info("此次数据查询的数量：{}", count);
                scrollId = response.getScrollId();

                ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
                clearScrollRequest.addScrollId(scrollId);
                ClearScrollResponse clearScrollResponse = esRestClient.clearScroll(clearScrollRequest, requestOptions);
                boolean succeeded = clearScrollResponse.isSucceeded();

                if (count < PAGE_SIZE_MAX) {
                    break;
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }


    }

    @Test
    public void multiSearchTest() {
        RestHighLevelClient esRestClient = getESRestClient();

        MultiSearchRequest multiSearchRequest = new MultiSearchRequest();
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery("userName", "cheng dou dou"));
        searchRequest.source(sourceBuilder);
        multiSearchRequest.add(searchRequest);
        SearchRequest searchRequest1 = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("id", "567"));
        searchRequest1.source(searchSourceBuilder);
        multiSearchRequest.add(searchRequest1);

        try {
            MultiSearchResponse multiSearchResponse = esRestClient.msearch(multiSearchRequest, requestOptions);
            MultiSearchResponse.Item[] responses = multiSearchResponse.getResponses();
            for (MultiSearchResponse.Item respons : responses) {
                SearchHits hits = respons.getResponse().getHits();
                for (SearchHit hit : hits) {
                    String sourceAsString = hit.getSourceAsString();
                    log.info(sourceAsString);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Test
    public void searchTemplate() {
        RestHighLevelClient esRestClient = getESRestClient();
        SearchTemplateRequest searchTemplateRequest = new SearchTemplateRequest();

        searchTemplateRequest.setScriptType(ScriptType.STORED);
        searchTemplateRequest.setScript("_search");

        HashMap<String, Object> scriptParams = new HashMap<>();
        scriptParams.put("field", "userName");
        scriptParams.put("value", "peanut");
        scriptParams.put("size", 100);
        searchTemplateRequest.setScriptParams(scriptParams);

        searchTemplateRequest.setRequest(new SearchRequest());

        try {
            SearchTemplateResponse searchTemplateResponse = esRestClient.searchTemplate(searchTemplateRequest, requestOptions);
            SearchResponse response = searchTemplateResponse.getResponse();
            SearchHits hits = response.getHits();
            for (SearchHit hit : hits) {
                log.info(hit.getSourceAsString());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Test
    public void deleteIndex() {
        RestHighLevelClient esRestClient = getESRestClient();

        String indexName = INDEX_OPERATE_LOG;

        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        try {
            AcknowledgedResponse response = esRestClient.indices().delete(deleteIndexRequest, requestOptions);
            if (response.isAcknowledged()) {
                log.info("索引：{}删除成功！！！！", indexName);
            }
        } catch (Exception e) {
            log.info("删除索引：{}失败", indexName);
            e.printStackTrace();
        }
    }


    @Test
    public void updateByQueryTest() {
        UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest(INDEX_CUSTOMER_INFO);
        //数据版本冲突时处理
        updateByQueryRequest.setConflicts("proceed");
        updateByQueryRequest.setQuery(QueryBuilders.matchPhraseQuery("name", "Rich"));
        Script script = new Script("");
        updateByQueryRequest.setScript(script);
    }

    @Test
    public void deleteByQueryTest() {
        RestHighLevelClient esRestClient = getESRestClient();

        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //match查询会把查询条件分词，再去进行匹配查询，只要分词后的任一关键词匹配上，则返回结果
        searchSourceBuilder.query(QueryBuilders.matchQuery("name", "Jhon Peter Richard"));
//        searchSourceBuilder.query(QueryBuilders.matchPhraseQuery("name","Thomas"));
//        searchSourceBuilder.query(QueryBuilders.matchPhrasePrefixQuery("name","Thomas84"));
        //term查询不会对查询条件分词，直接以查询条件做匹配，匹配就返回
//        searchSourceBuilder.query(QueryBuilders.termQuery("name","Peter"));
        //不设置分页大小时默认返回10条数据
        searchSourceBuilder.size(PAGE_SIZE_MAX);
        searchRequest.source(searchSourceBuilder);


        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(INDEX_CUSTOMER_INFO);
        deleteByQueryRequest.setQuery(QueryBuilders.matchPhraseQuery("name", "aaa Peter bbb"));
//        deleteByQueryRequest.setQuery(QueryBuilders.matchQuery("name","Peter"));
//        deleteByQueryRequest.setQuery(QueryBuilders.termQuery("name","Peter"));
//        deleteByQueryRequest.setQuery(QueryBuilders.matchPhrasePrefixQuery("name","Peter"));
        deleteByQueryRequest.setMaxDocs(PAGE_SIZE_MAX);
        deleteByQueryRequest.setConflicts("proceed");
        deleteByQueryRequest.setRefresh(true);

        try {
            SearchResponse searchResponse = esRestClient.search(searchRequest, requestOptions);
            SearchHits hits = searchResponse.getHits();
            int count = 0;
            for (SearchHit hit : hits) {
                log.info(hit.getSourceAsString());
                count++;
            }
            long total = hits.getTotalHits().value;
            log.info("当前查询到的总数：{}，当前查询返回的数据：{}", total, count);

            Aggregations aggregations = searchResponse.getAggregations();

            Aggregation aggregation = aggregations.get("key");


            /*BulkByScrollResponse bulkByScrollResponse = esRestClient.deleteByQuery(deleteByQueryRequest, requestOptions);
            //值未为0贼表示没有数据被删除，值为1表示删除数据成功
            int batches = bulkByScrollResponse.getBatches();
            log.info("batches: {}" , batches);
            //批量操作删除的数据
            long deleted = bulkByScrollResponse.getDeleted();
            log.info("deleted: {}" , deleted);
            //总共操作的数据
            long total = bulkByScrollResponse.getTotal();
            log.info("total: {}" , total);*/


        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

}
