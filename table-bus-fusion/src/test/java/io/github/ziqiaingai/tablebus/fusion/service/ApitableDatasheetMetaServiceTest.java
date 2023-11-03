package io.github.ziqiaingai.tablebus.fusion.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.ziqiaingai.tablebus.fusion.convert.ApitableDatasheetMetaConvert;
import io.github.ziqiaingai.tablebus.fusion.convert.ApitableDatasheetRecordConvert;
import io.github.ziqiaingai.tablebus.fusion.entity.*;
import io.github.ziqiaingai.tablebus.fusion.query.ApitableDatasheetMetaQuery;
import io.github.ziqiaingai.tablebus.fusion.vo.ApitableDatasheetMetaVO;
import io.github.ziqiaingai.tablebus.fusion.vo.ApitableDatasheetRecordVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ApitableDatasheetMetaServiceTest {

    @Resource
    private ApitableDatasheetMetaService apitableDatasheetMetaService;

    @Resource
    private ApitableDatasheetRecordService apitableDatasheetRecordService;

    @Resource private MetaViewsService metaViewsService;

    @Resource
    private MetaArchIdsService metaArchIdsService;

    @Resource
    private MetaFieldMapService metaFieldMapService;

    @Test
    public void testSplitTable() {
        ApitableDatasheetMetaConvert instance = ApitableDatasheetMetaConvert.INSTANCE;


        ApitableDatasheetMetaEntity xx = apitableDatasheetMetaService.getOne(new QueryWrapper<ApitableDatasheetMetaEntity>()
                .lambda()
                .eq(ApitableDatasheetMetaEntity::getDstId, "dst4VSuMWKa6y9bF5G7"));


        long now = System.currentTimeMillis();
        ApitableDatasheetMetaEntity dst4VSuMWK6y9bF5G7 = apitableDatasheetMetaService.getOne(new QueryWrapper<ApitableDatasheetMetaEntity>()
                .lambda()
                .eq(ApitableDatasheetMetaEntity::getDstId, "dstjvT6jkHKvWUAe0i"));
        long now1 = System.currentTimeMillis();
        System.out.println("query cost: " + (now1 - now));
        ApitableDatasheetMetaVO convert = instance.convert(dst4VSuMWK6y9bF5G7);
        long now2 = System.currentTimeMillis();
        System.out.println("parser cost: " + (now2 - now1));
        MetaArchIdsEntity metaArchIdsEntity = instance.convertToArchIds(convert);
        ObjectNode metaData = (ObjectNode) convert.getMetaData();
        metaData.fieldNames().forEachRemaining(System.out::println);
        List<MetaViewsEntity> metaViewsEntities = instance.convertToViews(convert);
        MetaFieldMapEntity metaFieldMapEntity = instance.convertToFieldsMap(convert);
        System.out.println(metaArchIdsEntity);
        metaArchIdsService.save(metaArchIdsEntity);
        metaViewsService.saveBatch(metaViewsEntities);
        metaFieldMapService.save(metaFieldMapEntity);

    }

    @Test
    public void testGetMeta5000Records() {


        ApitableDatasheetMetaEntity xx = apitableDatasheetMetaService.getOne(new QueryWrapper<ApitableDatasheetMetaEntity>()
                .lambda()
                .eq(ApitableDatasheetMetaEntity::getDstId, "dst4VSuMWKa6y9bF5G7"));

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        long now = System.currentTimeMillis();
        ApitableDatasheetMetaEntity dst4VSuMWK6y9bF5G7 = apitableDatasheetMetaService.getOne(new QueryWrapper<ApitableDatasheetMetaEntity>()
                .lambda()
                .eq(ApitableDatasheetMetaEntity::getDstId, "dst4VSuMWK6y9bF5G7"));
        long now1 = System.currentTimeMillis();
        System.out.println("query cost: " + (now1 - now));
        ApitableDatasheetMetaVO convert = ApitableDatasheetMetaConvert.INSTANCE.convert(dst4VSuMWK6y9bF5G7);
        long now2 = System.currentTimeMillis();
        System.out.println("parser cost: " + (now2 - now1));

        try {
            // 创建三个Callable任务，分别调用三个Service的方法
            Callable<MetaFieldMapEntity> taskA = () -> metaFieldMapService.getOne(new QueryWrapper<MetaFieldMapEntity>()
                    .lambda()
                    .eq(MetaFieldMapEntity::getDstId, "dst4VSuMWK6y9bF5G7"));
            Callable<List<MetaViewsEntity>> taskB = () -> metaViewsService.list(new QueryWrapper<MetaViewsEntity>()
                    .lambda()
                    .eq(MetaViewsEntity::getDstId, "dst4VSuMWK6y9bF5G7"));
            Callable<MetaArchIdsEntity> taskC = () -> metaArchIdsService.getOne(new QueryWrapper<MetaArchIdsEntity>()
                    .lambda()
                    .eq(MetaArchIdsEntity::getDstId, "dst4VSuMWK6y9bF5G7"));

            // 提交任务并获取Future对象
            Future<MetaFieldMapEntity> futureA = executorService.submit(taskA);
            Future<List<MetaViewsEntity>> futureB = executorService.submit(taskB);
            Future<MetaArchIdsEntity> futureC = executorService.submit(taskC);

            // 等待所有任务完成
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            // 获取任务的结果
            MetaFieldMapEntity dataA = futureA.get();
            List<MetaViewsEntity> dataB = futureB.get();
            MetaArchIdsEntity dataC = futureC.get();

            // 组装数据
            ApitableDatasheetMetaVO vo = ApitableDatasheetMetaConvert.INSTANCE.convert(dataA);
            ObjectNode meta = ApitableDatasheetMetaConvert.objectMapper.createObjectNode();
            meta.set("fieldMap", ApitableDatasheetMetaConvert.objectMapper.readTree(dataA.getFieldMap()));
            meta.set("archivedRecordIds", ApitableDatasheetMetaConvert.objectMapper.readTree(dataC.getArchIds()));
            ArrayNode views = ApitableDatasheetRecordConvert.objectMapper.createArrayNode();
            for (MetaViewsEntity metaViewsEntity : dataB) {
                views.add(ApitableDatasheetRecordConvert.objectMapper.readTree(metaViewsEntity.getView()));
            }
            meta.set("views", views);
            vo.setMetaData(meta);
            System.out.println("combine cost: " + (System.currentTimeMillis() - now2));
            assertEquals(convert, vo);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    public void testGetRecord() {
        long now = System.currentTimeMillis();
        List<ApitableDatasheetRecordEntity> dst4VSuMWK6y9bF5G7 = apitableDatasheetRecordService.list(new QueryWrapper<ApitableDatasheetRecordEntity>()
                .lambda()
                .eq(ApitableDatasheetRecordEntity::getDstId, "dst4VSuMWK6y9bF5G7"));
        long now1 = System.currentTimeMillis();
        System.out.println(now1 - now);
        List<ApitableDatasheetRecordVO> apitableDatasheetRecordVOS = ApitableDatasheetRecordConvert.INSTANCE.convertList(dst4VSuMWK6y9bF5G7);
        System.out.println(System.currentTimeMillis() - now1);
    }
}