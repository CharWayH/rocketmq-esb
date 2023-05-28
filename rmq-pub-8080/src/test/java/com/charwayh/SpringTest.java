package com.charwayh;

import com.charwayh.mapper.BusinessSystemConfMapper;
import com.charwayh.upon.domain.BusinessSystemConf;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

/**
 * @author charwayH
 */
@SpringBootTest(classes = RmqPubApp8080.class)
@RunWith(SpringRunner.class)
public class SpringTest {

//    @Autowired
//    private RestHighLevelClient restHighLevelClient;
//    @Autowired
//    private MQLogMapper mqLogMapper;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private BusinessSystemConfMapper businessSystemConfMapper;

//    @Test
//    public void test01() throws IOException {
//
//        for (int i = 0; i < 10; i++) {
//
//            String id = UUID.randomUUID().toString().substring(0, 3);
//            //构建想要存储的对象
//            Map<String, Object> map = new HashMap<>();
//            map.put("msg", "test");
//            map.put("msgId","200" );
//            map.put("time", "2020-2-12");
//            //指定要存入数据的库
//            IndexRequest indexRequest
//                    = new IndexRequest("test").id(id).source(map);
//
//            //创建索引 并保存至es库中
//            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
//        }
//    }

//    @Test
//    public void contextLoads() {
//        System.out.println(restHighLevelClient);
//    }

    @Test
    public void test01() throws IOException {
        System.out.println(elasticsearchTemplate);
//        System.out.println(mqLogMapper);
        }

    @Test
    public void test02() throws IOException {
        List<BusinessSystemConf> confList = businessSystemConfMapper.getConfList();
        System.out.println(confList);
    }
    }
