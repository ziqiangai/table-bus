package io.github.ziqiaingai.tablebus.fusion.convert;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.ziqiaingai.tablebus.fusion.entity.ApitableDatasheetMetaEntity;
import io.github.ziqiaingai.tablebus.fusion.entity.MetaArchIdsEntity;
import io.github.ziqiaingai.tablebus.fusion.entity.MetaFieldMapEntity;
import io.github.ziqiaingai.tablebus.fusion.entity.MetaViewsEntity;
import io.github.ziqiaingai.tablebus.fusion.vo.ApitableDatasheetMetaVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
* Workbench - Data Table Metadata Table
*
* @author zzq
* @since 1.0.0 2023-10-01
*/
@Mapper
public interface ApitableDatasheetMetaConvert extends BaseMapper{
    ApitableDatasheetMetaConvert INSTANCE = Mappers.getMapper(ApitableDatasheetMetaConvert.class);

    ApitableDatasheetMetaEntity convert(ApitableDatasheetMetaVO vo);

    ApitableDatasheetMetaVO convert(ApitableDatasheetMetaEntity entity);

    List<ApitableDatasheetMetaVO> convertList(List<ApitableDatasheetMetaEntity> list);

    MetaArchIdsEntity convertToArchIdsBase(ApitableDatasheetMetaVO vo);
    default MetaArchIdsEntity convertToArchIds(ApitableDatasheetMetaVO vo){
        MetaArchIdsEntity metaArchIdsEntity = convertToArchIdsBase(vo);
        metaArchIdsEntity.setArchIds(vo.getMetaData().path("archivedRecordIds").toString());
        return metaArchIdsEntity;
    }

    default MetaFieldMapEntity convertToFieldsMap(ApitableDatasheetMetaVO vo){
        MetaFieldMapEntity metaFieldMapEntity = convertToFieldsMapBase(vo);
        JsonNode metaData = vo.getMetaData();
        JsonNode fields = metaData.path("fieldMap");
        metaFieldMapEntity.setFieldMap(fields.toString());
        return metaFieldMapEntity;
    }


    ApitableDatasheetMetaVO convert(MetaFieldMapEntity entity);

    MetaFieldMapEntity convertToFieldsMapBase(ApitableDatasheetMetaVO vo);

    default List<MetaViewsEntity> convertToViews(ApitableDatasheetMetaVO vo){
        JsonNode metaData = vo.getMetaData();
        JsonNode path = metaData.path("views");
        List<MetaViewsEntity> metaViewsEntities = new ArrayList<>(path.size());
        for (JsonNode jsonNode : path) {
            MetaViewsEntity metaViewsEntity = new MetaViewsEntity();
            metaViewsEntity.setViewId(jsonNode.path("id").asText());
            metaViewsEntity.setView(jsonNode.toString());
            metaViewsEntity.setDstId(vo.getDstId());
            metaViewsEntities.add(metaViewsEntity);
        }
        return metaViewsEntities;
    }

}