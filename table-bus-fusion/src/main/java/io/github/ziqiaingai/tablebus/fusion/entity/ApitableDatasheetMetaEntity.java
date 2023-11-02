package io.github.ziqiaingai.tablebus.fusion.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * Workbench - Data Table Metadata Table
 *
 * @author zzq
 * @since 1.0.0 2023-10-02
 */

@Data
@TableName("apitable_datasheet_meta")
public class ApitableDatasheetMetaEntity {
	/**
	* Primary key
	*/
	@TableId
	private Long id;

	/**
	* Number table custom ID(link#xxxx_datasheet#dst_id)
	*/
	private String dstId;

	/**
	* Metadata
	*/
	private String metaData;

	/**
	* Version No
	*/
	private Long revision;

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