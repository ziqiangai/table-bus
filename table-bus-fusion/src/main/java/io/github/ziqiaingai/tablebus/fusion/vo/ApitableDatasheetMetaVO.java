package io.github.ziqiaingai.tablebus.fusion.vo;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
* Workbench - Data Table Metadata Table
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2023-10-05
*/
@Data
@Schema(description = "Workbench - Data Table Metadata Table")
public class ApitableDatasheetMetaVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "Primary key")
	private Long id;

	@Schema(description = "Number table custom ID(link#xxxx_datasheet#dst_id)")
	private String dstId;

	@Schema(description = "metaData")
	private JsonNode metaData;

	@Schema(description = "Version No")
	private Long revision;

	@Schema(description = "Delete tag(0:No,1:Yes)")
	private Integer isDeleted;

	@Schema(description = "Create User")
	private Long createdBy;

	@Schema(description = "Last Update User")
	private Long updatedBy;

	@Schema(description = "Create time")
	private Long createTime;

	@Schema(description = "Update time")
	private Long updateTime;

	@Schema(description = "Create Time")
	private Long createdAt;

	@Schema(description = "Update Time")
	private Long updatedAt;


}