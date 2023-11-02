package io.github.ziqiaingai.tablebus.fusion.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.ziqiaingai.tablebus.fusion.convert.ApitableDatasheetMetaConvert;
import io.github.ziqiaingai.tablebus.fusion.dao.ApitableDatasheetMetaDao;
import io.github.ziqiaingai.tablebus.fusion.entity.ApitableDatasheetMetaEntity;
import io.github.ziqiaingai.tablebus.fusion.query.ApitableDatasheetMetaQuery;
import io.github.ziqiaingai.tablebus.fusion.service.ApitableDatasheetMetaService;
import io.github.ziqiaingai.tablebus.fusion.service.BaseServiceImpl;
import io.github.ziqiaingai.tablebus.fusion.vo.ApitableDatasheetMetaVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Workbench - Data Table Metadata Table
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2023-10-05
 */
@Service
@AllArgsConstructor
public class ApitableDatasheetMetaServiceImpl extends BaseServiceImpl<ApitableDatasheetMetaDao, ApitableDatasheetMetaEntity> implements ApitableDatasheetMetaService {


    private LambdaQueryWrapper<ApitableDatasheetMetaEntity> getWrapper(ApitableDatasheetMetaQuery query){
        LambdaQueryWrapper<ApitableDatasheetMetaEntity> wrapper = Wrappers.lambdaQuery();

        return wrapper;
    }

    @Override
    public void save(ApitableDatasheetMetaVO vo) {
        ApitableDatasheetMetaEntity entity = ApitableDatasheetMetaConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(ApitableDatasheetMetaVO vo) {
        ApitableDatasheetMetaEntity entity = ApitableDatasheetMetaConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}