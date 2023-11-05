package io.github.ziqiaingai.tablebus.fusion.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public void testCombine5000RecordSnapshot(){
        String dstId = "dst4VSuMWK6y9bF5G7";

        ApitableDatasheetMetaEntity xx = apitableDatasheetMetaService.getOne(new QueryWrapper<ApitableDatasheetMetaEntity>()
                .lambda()
                .eq(ApitableDatasheetMetaEntity::getDstId, "xx" + dstId));

        ExecutorService executorService = Executors.newFixedThreadPool(8);
        long now = System.currentTimeMillis();
        ApitableDatasheetMetaEntity dst4VSuMWK6y9bF5G7 = apitableDatasheetMetaService.getOne(new QueryWrapper<ApitableDatasheetMetaEntity>()
                .lambda()
                .eq(ApitableDatasheetMetaEntity::getDstId, dstId));
        ApitableDatasheetMetaVO convert = ApitableDatasheetMetaConvert.INSTANCE.convert(dst4VSuMWK6y9bF5G7);

        ObjectNode snapshot = ApitableDatasheetMetaConvert.objectMapper.createObjectNode();
        snapshot.set("meta", convert.getMetaData());

        List<ApitableDatasheetRecordEntity> records = apitableDatasheetRecordService.list(new QueryWrapper<ApitableDatasheetRecordEntity>()
                .lambda()
                .eq(ApitableDatasheetRecordEntity::getDstId, dstId));
        ObjectNode map = ApitableDatasheetRecordConvert.INSTANCE.convertObjectNode(records);
        snapshot.set("records", map);
        System.out.println("combine cost: " + (System.currentTimeMillis() - now));


        long now2 = System.currentTimeMillis();
        try {
            // 定义每次查询的批次大小
            int batchSize = 1000;

            // 定义目标数据的查询条件
            LambdaQueryWrapper<ApitableDatasheetRecordEntity> queryWrapper = new QueryWrapper<ApitableDatasheetRecordEntity>()
                    .lambda()
                    .eq(ApitableDatasheetRecordEntity::getDstId, "dst4VSuMWK6y9bF5G7");

            // 查询总数据量
            long totalDataSize = apitableDatasheetRecordService.count(queryWrapper);

            // 计算需要多少次查询
            long numQueries = (totalDataSize + batchSize - 1) / batchSize;

            List<Future<List<ApitableDatasheetRecordEntity>>> futures = new CopyOnWriteArrayList<>();
            for (int i = 0; i < numQueries; i++) {
                int offset = i * batchSize;

                // 提交查询任务到线程池
                Future<List<ApitableDatasheetRecordEntity>> future = executorService.submit(() -> {
                    // 分页查询数据
                    Page<ApitableDatasheetRecordEntity> page = new Page<>(offset / batchSize + 1, batchSize);
                    List<ApitableDatasheetRecordEntity> result = apitableDatasheetRecordService.page(page, queryWrapper).getRecords();
                    return result;
                });

                futures.add(future);
            }

            // 创建三个Callable任务，分别调用三个Service的方法
            Callable<MetaFieldMapEntity> taskA = () -> metaFieldMapService.getOne(new QueryWrapper<MetaFieldMapEntity>()
                    .lambda()
                    .eq(MetaFieldMapEntity::getDstId, dstId));
            Callable<List<MetaViewsEntity>> taskB = () -> metaViewsService.list(new QueryWrapper<MetaViewsEntity>()
                    .lambda()
                    .eq(MetaViewsEntity::getDstId, dstId));
            Callable<MetaArchIdsEntity> taskC = () -> metaArchIdsService.getOne(new QueryWrapper<MetaArchIdsEntity>()
                    .lambda()
                    .eq(MetaArchIdsEntity::getDstId, dstId));

            // 提交任务并获取Future对象
            Future<MetaFieldMapEntity> futureA = executorService.submit(taskA);
            Future<List<MetaViewsEntity>> futureB = executorService.submit(taskB);
            Future<MetaArchIdsEntity> futureC = executorService.submit(taskC);

            // 等待所有任务完成
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);


            ObjectNode recordMap = ApitableDatasheetMetaConvert.objectMapper.createObjectNode();
            // 获取任务的结果
            for (Future<List<ApitableDatasheetRecordEntity>> future : futures) {
                List<ApitableDatasheetRecordEntity> records2 = future.get();
                for (ApitableDatasheetRecordEntity record : records2) {
                    recordMap.set(record.getRecordId(), ApitableDatasheetMetaConvert.objectMapper.valueToTree(record));
                }
            }

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

            ObjectNode snapshot2 = ApitableDatasheetMetaConvert.objectMapper.createObjectNode();
            snapshot2.set("meta", convert.getMetaData());
            snapshot2.set("records", map);
            System.out.println("combine cost: " + (System.currentTimeMillis() - now2));
            assertEquals(snapshot2, snapshot);
            System.out.println("size : " + (snapshot.toString().length() / 1024 / 1024));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testCombine10000RecordSnapshot(){
        String dstId = "dstjvT6jkHKvWUAe0i";

        ApitableDatasheetMetaEntity xx = apitableDatasheetMetaService.getOne(new QueryWrapper<ApitableDatasheetMetaEntity>()
                .lambda()
                .eq(ApitableDatasheetMetaEntity::getDstId, "xx" + dstId));

        ExecutorService executorService = Executors.newFixedThreadPool(8);
        long now = System.currentTimeMillis();
        ApitableDatasheetMetaEntity dst4VSuMWK6y9bF5G7 = apitableDatasheetMetaService.getOne(new QueryWrapper<ApitableDatasheetMetaEntity>()
                .lambda()
                .eq(ApitableDatasheetMetaEntity::getDstId, dstId));
        ApitableDatasheetMetaVO convert = ApitableDatasheetMetaConvert.INSTANCE.convert(dst4VSuMWK6y9bF5G7);

        ObjectNode snapshot = ApitableDatasheetMetaConvert.objectMapper.createObjectNode();
        snapshot.set("meta", convert.getMetaData());

        List<ApitableDatasheetRecordEntity> records = apitableDatasheetRecordService.list(new QueryWrapper<ApitableDatasheetRecordEntity>()
                .lambda()
                .eq(ApitableDatasheetRecordEntity::getDstId, dstId));
        ObjectNode map = ApitableDatasheetRecordConvert.INSTANCE.convertObjectNode(records);
        snapshot.set("records", map);
        System.out.println("combine cost: " + (System.currentTimeMillis() - now));


        long now2 = System.currentTimeMillis();
        try {
            // 定义每次查询的批次大小
            int batchSize = 1000;

            // 定义目标数据的查询条件
            LambdaQueryWrapper<ApitableDatasheetRecordEntity> queryWrapper = new QueryWrapper<ApitableDatasheetRecordEntity>()
                    .lambda()
                    .eq(ApitableDatasheetRecordEntity::getDstId, "dst4VSuMWK6y9bF5G7");

            // 查询总数据量
            long totalDataSize = apitableDatasheetRecordService.count(queryWrapper);

            // 计算需要多少次查询
            long numQueries = (totalDataSize + batchSize - 1) / batchSize;

            List<Future<List<ApitableDatasheetRecordEntity>>> futures = new CopyOnWriteArrayList<>();
            for (int i = 0; i < numQueries; i++) {
                int offset = i * batchSize;

                // 提交查询任务到线程池
                Future<List<ApitableDatasheetRecordEntity>> future = executorService.submit(() -> {
                    // 分页查询数据
                    Page<ApitableDatasheetRecordEntity> page = new Page<>(offset / batchSize + 1, batchSize);
                    List<ApitableDatasheetRecordEntity> result = apitableDatasheetRecordService.page(page, queryWrapper).getRecords();
                    return result;
                });

                futures.add(future);
            }

            // 创建三个Callable任务，分别调用三个Service的方法
            Callable<MetaFieldMapEntity> taskA = () -> metaFieldMapService.getOne(new QueryWrapper<MetaFieldMapEntity>()
                    .lambda()
                    .eq(MetaFieldMapEntity::getDstId, dstId));
            Callable<List<MetaViewsEntity>> taskB = () -> metaViewsService.list(new QueryWrapper<MetaViewsEntity>()
                    .lambda()
                    .eq(MetaViewsEntity::getDstId, dstId));
            Callable<MetaArchIdsEntity> taskC = () -> metaArchIdsService.getOne(new QueryWrapper<MetaArchIdsEntity>()
                    .lambda()
                    .eq(MetaArchIdsEntity::getDstId, dstId));

            // 提交任务并获取Future对象
            Future<MetaFieldMapEntity> futureA = executorService.submit(taskA);
            Future<List<MetaViewsEntity>> futureB = executorService.submit(taskB);
            Future<MetaArchIdsEntity> futureC = executorService.submit(taskC);

            // 等待所有任务完成
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);


            ObjectNode recordMap = ApitableDatasheetMetaConvert.objectMapper.createObjectNode();
            // 获取任务的结果
            for (Future<List<ApitableDatasheetRecordEntity>> future : futures) {
                List<ApitableDatasheetRecordEntity> records2 = future.get();
                for (ApitableDatasheetRecordEntity record : records2) {
                    recordMap.set(record.getRecordId(), ApitableDatasheetMetaConvert.objectMapper.valueToTree(record));
                }
            }

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

            ObjectNode snapshot2 = ApitableDatasheetMetaConvert.objectMapper.createObjectNode();
            snapshot2.set("meta", convert.getMetaData());
            snapshot2.set("records", map);
            System.out.println("combine cost: " + (System.currentTimeMillis() - now2));
            assertEquals(snapshot2, snapshot);
            System.out.println("size : " + (snapshot.toString().length() / 1024 / 1024));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testGetMeta10000Records() {


        ApitableDatasheetMetaEntity xx = apitableDatasheetMetaService.getOne(new QueryWrapper<ApitableDatasheetMetaEntity>()
                .lambda()
                .eq(ApitableDatasheetMetaEntity::getDstId, "dst4VSuMWKa6y9bF5G7"));

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        long now = System.currentTimeMillis();
        ApitableDatasheetMetaEntity dst4VSuMWK6y9bF5G7 = apitableDatasheetMetaService.getOne(new QueryWrapper<ApitableDatasheetMetaEntity>()
                .lambda()
                .eq(ApitableDatasheetMetaEntity::getDstId, "dstjvT6jkHKvWUAe0i"));
        long now1 = System.currentTimeMillis();
        System.out.println("query cost: " + (now1 - now));
        ApitableDatasheetMetaVO convert = ApitableDatasheetMetaConvert.INSTANCE.convert(dst4VSuMWK6y9bF5G7);
        long now2 = System.currentTimeMillis();
        System.out.println("parser cost: " + (now2 - now1));

        try {
            // 创建三个Callable任务，分别调用三个Service的方法
            Callable<MetaFieldMapEntity> taskA = () -> metaFieldMapService.getOne(new QueryWrapper<MetaFieldMapEntity>()
                    .lambda()
                    .eq(MetaFieldMapEntity::getDstId, "dstjvT6jkHKvWUAe0i"));
            Callable<List<MetaViewsEntity>> taskB = () -> metaViewsService.list(new QueryWrapper<MetaViewsEntity>()
                    .lambda()
                    .eq(MetaViewsEntity::getDstId, "dstjvT6jkHKvWUAe0i"));
            Callable<MetaArchIdsEntity> taskC = () -> metaArchIdsService.getOne(new QueryWrapper<MetaArchIdsEntity>()
                    .lambda()
                    .eq(MetaArchIdsEntity::getDstId, "dstjvT6jkHKvWUAe0i"));

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
            System.out.println("views size " + views.size());
            meta.set("views", views);
            vo.setMetaData(meta);
            System.out.println("combine cost: " + (System.currentTimeMillis() - now2));
            assertEquals(convert.getMetaData().path("views"), vo.getMetaData().path("views"));
            assertEquals(convert.getMetaData().path("fieldMap"), vo.getMetaData().path("fieldMap"));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


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


    }

    @Test
    public void testGet5000Record() throws InterruptedException, ExecutionException {
        apitableDatasheetRecordService.list(new QueryWrapper<ApitableDatasheetRecordEntity>()
                .lambda()
                .eq(ApitableDatasheetRecordEntity::getDstId, "xxx"));
        long now = System.currentTimeMillis();
        List<ApitableDatasheetRecordEntity> dst4VSuMWK6y9bF5G7 = apitableDatasheetRecordService.list(new QueryWrapper<ApitableDatasheetRecordEntity>()
                .lambda()
                .eq(ApitableDatasheetRecordEntity::getDstId, "dst4VSuMWK6y9bF5G7"));
        long now1 = System.currentTimeMillis();
        System.out.println(now1 - now);
        Map<String, ApitableDatasheetRecordVO> map = ApitableDatasheetRecordConvert.INSTANCE.convertMap(dst4VSuMWK6y9bF5G7);
        System.out.println("get " + map.size() + " cost: " + (System.currentTimeMillis() - now1) + " ms");


        // 并发查询

        // 创建一个自定义的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        long now2 = System.currentTimeMillis();
        // 定义每次查询的批次大小
        int batchSize = 1000;

        // 定义目标数据的查询条件
        LambdaQueryWrapper<ApitableDatasheetRecordEntity> queryWrapper = new QueryWrapper<ApitableDatasheetRecordEntity>()
                .lambda()
                .eq(ApitableDatasheetRecordEntity::getDstId, "dst4VSuMWK6y9bF5G7");

        // 查询总数据量
        long totalDataSize = apitableDatasheetRecordService.count(queryWrapper);
        System.out.println("data count: " + totalDataSize);

        // 计算需要多少次查询
        long numQueries = (totalDataSize + batchSize - 1) / batchSize;

        Map<String, ApitableDatasheetRecordVO> voMap = new HashMap<>((int) totalDataSize);
        List<Future<List<ApitableDatasheetRecordEntity>>> futures = new CopyOnWriteArrayList<>();
        for (int i = 0; i < numQueries; i++) {
            int offset = i * batchSize;

            // 提交查询任务到线程池
            Future<List<ApitableDatasheetRecordEntity>> future = executorService.submit(() -> {
                // 分页查询数据
                Page<ApitableDatasheetRecordEntity> page = new Page<>(offset / batchSize + 1, batchSize);
                List<ApitableDatasheetRecordEntity> result = apitableDatasheetRecordService.page(page, queryWrapper).getRecords();
                return result;
            });

            futures.add(future);
        }
        // 等待所有任务完成
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        // 获取任务的结果
        for (Future<List<ApitableDatasheetRecordEntity>> future : futures) {
            List<ApitableDatasheetRecordEntity> records = future.get();
            for (ApitableDatasheetRecordEntity record : records) {
                voMap.put(record.getRecordId(), ApitableDatasheetRecordConvert.INSTANCE.convert(record));
            }
        }
        System.out.println("get " + voMap.size() + " cost: " + (System.currentTimeMillis() - now2) + " ms");
        // 关闭线程池
        executorService.shutdown();


    }

    @Test
    public void testGet10000Record() throws InterruptedException, ExecutionException {
        apitableDatasheetRecordService.list(new QueryWrapper<ApitableDatasheetRecordEntity>()
                .lambda()
                .eq(ApitableDatasheetRecordEntity::getDstId, "xxx"));
        long now = System.currentTimeMillis();
        List<ApitableDatasheetRecordEntity> dst4VSuMWK6y9bF5G7 = apitableDatasheetRecordService.list(new QueryWrapper<ApitableDatasheetRecordEntity>()
                .lambda()
                .eq(ApitableDatasheetRecordEntity::getDstId, "dstjvT6jkHKvWUAe0i"));
        long now1 = System.currentTimeMillis();
        System.out.println(now1 - now);
        Map<String, ApitableDatasheetRecordVO> map = ApitableDatasheetRecordConvert.INSTANCE.convertMap(dst4VSuMWK6y9bF5G7);
        System.out.println("get " + map.size() + " cost: " + (System.currentTimeMillis() - now1) + " ms");


        // 并发查询

        // 创建一个自定义的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        long now2 = System.currentTimeMillis();
        // 定义每次查询的批次大小
        int batchSize = 1000;

        // 定义目标数据的查询条件
        LambdaQueryWrapper<ApitableDatasheetRecordEntity> queryWrapper = new QueryWrapper<ApitableDatasheetRecordEntity>()
                .lambda()
                .eq(ApitableDatasheetRecordEntity::getDstId, "dstjvT6jkHKvWUAe0i");

        // 查询总数据量
        long totalDataSize = apitableDatasheetRecordService.count(queryWrapper);
        System.out.println("data count: " + totalDataSize);

        // 计算需要多少次查询
        long numQueries = (totalDataSize + batchSize - 1) / batchSize;

        Map<String, ApitableDatasheetRecordVO> voMap = new HashMap<>((int) totalDataSize);
        List<Future<List<ApitableDatasheetRecordEntity>>> futures = new CopyOnWriteArrayList<>();
        for (int i = 0; i < numQueries; i++) {
            int offset = i * batchSize;

            // 提交查询任务到线程池
            Future<List<ApitableDatasheetRecordEntity>> future = executorService.submit(() -> {
                // 分页查询数据
                Page<ApitableDatasheetRecordEntity> page = new Page<>(offset / batchSize + 1, batchSize);
                List<ApitableDatasheetRecordEntity> result = apitableDatasheetRecordService.page(page, queryWrapper).getRecords();
                return result;
            });

            futures.add(future);
        }
        // 等待所有任务完成
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        // 获取任务的结果
        for (Future<List<ApitableDatasheetRecordEntity>> future : futures) {
            List<ApitableDatasheetRecordEntity> records = future.get();
            for (ApitableDatasheetRecordEntity record : records) {
                voMap.put(record.getRecordId(), ApitableDatasheetRecordConvert.INSTANCE.convert(record));
            }
        }
        System.out.println("get " + voMap.size() + " cost: " + (System.currentTimeMillis() - now2) + " ms");
        // 关闭线程池
        executorService.shutdown();


    }

    @Test
    public void testPageQuery(){
        // 定义目标数据的查询条件
        LambdaQueryWrapper<ApitableDatasheetRecordEntity> queryWrapper = new QueryWrapper<ApitableDatasheetRecordEntity>()
                .lambda()
                .eq(ApitableDatasheetRecordEntity::getDstId, "dst4VSuMWK6y9bF5G7");


        int batchSize = 10;
        int offset = 0;
        Page<ApitableDatasheetRecordEntity> page = new Page<>(offset / batchSize + 1, batchSize);
        List<ApitableDatasheetRecordEntity> result = apitableDatasheetRecordService.page(page, queryWrapper).getRecords();
    }
}