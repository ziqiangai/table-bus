package io.github.ziqiaingai.tablebus.fusion.service;

import io.github.ziqiaingai.tablebus.fusion.entity.ApitableDatasheetMetaEntity;
import io.github.ziqiaingai.tablebus.fusion.query.ApitableDatasheetMetaQuery;
import io.github.ziqiaingai.tablebus.fusion.vo.ApitableDatasheetMetaVO;

import java.util.List;

/**
 * Workbench - Data Table Metadata Table
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2023-10-05
 */
public interface ApitableDatasheetMetaService extends BaseService<ApitableDatasheetMetaEntity> {


    void save(ApitableDatasheetMetaVO vo);

    void update(ApitableDatasheetMetaVO vo);

    void delete(List<Long> idList);
}