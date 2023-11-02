package io.github.ziqiaingai.tablebus.fusion.query;

import io.github.ziqiaingai.tablebus.fusion.config.Query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
* Workbench - Datasheet Record Table查询
*
* @author zzq
* @since 1.0.0 2023-10-01
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "Workbench - Datasheet Record Table查询")
public class ApitableDatasheetRecordQuery extends Query {
}