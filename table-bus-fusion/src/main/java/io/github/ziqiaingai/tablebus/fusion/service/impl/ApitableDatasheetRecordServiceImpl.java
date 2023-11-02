package io.github.ziqiaingai.tablebus.fusion.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.ziqiaingai.tablebus.fusion.convert.ApitableDatasheetRecordConvert;
import io.github.ziqiaingai.tablebus.fusion.dao.ApitableDatasheetRecordDao;
import io.github.ziqiaingai.tablebus.fusion.entity.ApitableDatasheetRecordEntity;
import io.github.ziqiaingai.tablebus.fusion.query.ApitableDatasheetRecordQuery;
import io.github.ziqiaingai.tablebus.fusion.service.ApitableDatasheetRecordService;
import io.github.ziqiaingai.tablebus.fusion.service.BaseServiceImpl;
import io.github.ziqiaingai.tablebus.fusion.vo.ApitableDatasheetRecordVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Workbench - Datasheet Record Table
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2023-10-05
 */
@Service
@AllArgsConstructor
public class ApitableDatasheetRecordServiceImpl extends BaseServiceImpl<ApitableDatasheetRecordDao, ApitableDatasheetRecordEntity> implements ApitableDatasheetRecordService {

    private LambdaQueryWrapper<ApitableDatasheetRecordEntity> getWrapper(ApitableDatasheetRecordQuery query){
        LambdaQueryWrapper<ApitableDatasheetRecordEntity> wrapper = Wrappers.lambdaQuery();

        return wrapper;
    }

    @Override
    public void save(ApitableDatasheetRecordVO vo) {
        ApitableDatasheetRecordEntity entity = ApitableDatasheetRecordConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(ApitableDatasheetRecordVO vo) {
        ApitableDatasheetRecordEntity entity = ApitableDatasheetRecordConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}