package io.github.ziqiaingai.tablebus.fusion.dao;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.ziqiaingai.tablebus.fusion.config.BaseDao;
import io.github.ziqiaingai.tablebus.fusion.entity.ApitableDatasheetRecordEntity;
import io.github.ziqiaingai.tablebus.fusion.entity.MetaArchIdsEntity;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

/**
 * Workbench - Data Table Metadata Table
 *
 * @author zzq
 * @since 1.0.0 2023-10-02
 */
@Mapper
public interface MetaArchIdsDao extends BaseDao<MetaArchIdsEntity> {

}