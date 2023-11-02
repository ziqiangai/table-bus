package io.github.ziqiaingai.tablebus.fusion.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * Workbench - Datasheet Record Table
 *
 * @author zzq
 * @since 1.0.0 2023-10-02
 */

@Data
@TableName("apitable_datasheet_record")
public class ApitableDatasheetRecordEntity {
	/**
	* Primary key
	*/
	@TableId
	private Long id;

	/**
	* Operation ID
	*/
	private String recordId;

	/**
	* Datasheet ID(link#xxxx_datasheet#dst_id)
	*/
	private String dstId;

	/**
	* Data recorded in one row (corresponding to each field)
	*/
	private String data;

	/**
	* The historical version number sorted is the revision of the original operation, and the array subscript is the revision of the current record
	*/
	private String revisionHistory;

	/**
	* Version No
	*/
	private Long revision;

	/**
	* Field Update Information
	*/
	private String fieldUpdatedInfo;

	/**
	* Delete tag(0:No,1:Yes)
	*/
	private Integer isDeleted;

	/**
	* Create User
	*/
	private Long createdBy;

	/**
	* Last Update User
	*/
	private Long updatedBy;

	/**
	* Create time
	*/
	private Date createTime;

	/**
	* Update time
	*/
	private Date updateTime;

	/**
	* Create Time
	*/
	private Date createdAt;

	/**
	* Update Time
	*/
	private Date updatedAt;

}