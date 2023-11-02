package io.github.ziqiaingai.tablebus.fusion.service;

import io.github.ziqiaingai.tablebus.fusion.entity.ApitableDatasheetRecordEntity;
import io.github.ziqiaingai.tablebus.fusion.service.BaseService;
import io.github.ziqiaingai.tablebus.fusion.vo.ApitableDatasheetRecordVO;

import java.util.List;

/**
 * Workbench - Datasheet Record Table
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2023-10-05
 */
public interface ApitableDatasheetRecordService extends BaseService<ApitableDatasheetRecordEntity> {


    void save(ApitableDatasheetRecordVO vo);

    void update(ApitableDatasheetRecordVO vo);

    void delete(List<Long> idList);
}