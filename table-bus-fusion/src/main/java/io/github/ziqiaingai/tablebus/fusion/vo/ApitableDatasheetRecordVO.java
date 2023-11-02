package io.github.ziqiaingai.tablebus.fusion.vo;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;

/**
* Workbench - Datasheet Record Table
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2023-10-05
*/
@Data
@Schema(description = "Workbench - Datasheet Record Table")
public class ApitableDatasheetRecordVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "Primary key")
	private Long id;

	@Schema(description = "Operation ID")
	private String recordId;

	@Schema(description = "Datasheet ID(link#xxxx_datasheet#dst_id)")
	private String dstId;

	@Schema(description = "Data recorded in one row (corresponding to each field)")
	private JsonNode data;

	@Schema(description = "The historical version number sorted is the revision of the original operation, and the array subscript is the revision of the current record")
	private String revisionHistory;

	@Schema(description = "Version No")
	private Long revision;

	@Schema(description = "Field Update Information")
	private JsonNode fieldUpdatedInfo;

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