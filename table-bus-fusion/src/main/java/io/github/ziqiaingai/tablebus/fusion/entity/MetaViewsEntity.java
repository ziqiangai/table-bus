package io.github.ziqiaingai.tablebus.fusion.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Workbench - Data Table Metadata Table
 *
 * @author zzq
 * @since 1.0.0 2023-10-02
 */

@Data
@TableName("apitable_datasheet_view_sub")
public class MetaViewsEntity {
	/**
	* Primary key
	*/
	@TableId
	private Long id;

	/**
	* Number table custom ID(link#xxxx_datasheet#dst_id)
	*/
	private String dstId;
	private String viewId;

	/**
	* Metadata
	*/
	private String view;

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