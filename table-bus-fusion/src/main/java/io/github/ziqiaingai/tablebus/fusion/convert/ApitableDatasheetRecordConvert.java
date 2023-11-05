package io.github.ziqiaingai.tablebus.fusion.convert;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.ziqiaingai.tablebus.fusion.entity.ApitableDatasheetRecordEntity;
import io.github.ziqiaingai.tablebus.fusion.vo.ApitableDatasheetRecordVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Workbench - Datasheet Record Table
*
* @author zzq
* @since 1.0.0 2023-10-01
*/
@Mapper
public interface ApitableDatasheetRecordConvert extends BaseMapper{
    ApitableDatasheetRecordConvert INSTANCE = Mappers.getMapper(ApitableDatasheetRecordConvert.class);

    ApitableDatasheetRecordEntity convert(ApitableDatasheetRecordVO vo);

    ApitableDatasheetRecordVO convert(ApitableDatasheetRecordEntity entity);

    List<ApitableDatasheetRecordVO> convertList(List<ApitableDatasheetRecordEntity> list);

    default Map<String, ApitableDatasheetRecordVO> convertMap(List<ApitableDatasheetRecordEntity> list) {
        Map<String, ApitableDatasheetRecordVO> map = new HashMap<>(list.size());
        for (ApitableDatasheetRecordEntity apitableDatasheetRecordEntity : list) {
            ApitableDatasheetRecordVO convert = convert(apitableDatasheetRecordEntity);
            map.put(convert.getRecordId(), convert);
        }
        return map;
    }

    default ObjectNode convertObjectNode(List<ApitableDatasheetRecordEntity> list) {
        ObjectNode objectNode = ApitableDatasheetMetaConvert.objectMapper.createObjectNode();
        for (ApitableDatasheetRecordEntity apitableDatasheetRecordEntity : list) {
            ApitableDatasheetRecordVO convert = convert(apitableDatasheetRecordEntity);
            objectNode.put(convert.getRecordId(), ApitableDatasheetMetaConvert.objectMapper.valueToTree(convert));
        }
        return objectNode;
    }

}