package io.github.ziqiaingai.tablebus.fusion.query;

import io.github.ziqiaingai.tablebus.fusion.config.Query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
* Workbench - Data Table Metadata Table查询
*
* @author zzq
* @since 1.0.0 2023-10-01
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "Workbench - Data Table Metadata Table查询")
public class ApitableDatasheetMetaQuery extends Query {
}