package io.github.ziqiaingai.tablebus.fusion.convert;

import io.github.ziqiaingai.tablebus.fusion.entity.ApitableDatasheetRecordEntity;
import io.github.ziqiaingai.tablebus.fusion.vo.ApitableDatasheetRecordVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

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

}