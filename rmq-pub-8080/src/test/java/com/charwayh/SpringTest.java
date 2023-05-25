package com.charwayh;

import com.charwayh.mapper.MQLogMapper;
import net.bytebuddy.asm.Advice;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    }
