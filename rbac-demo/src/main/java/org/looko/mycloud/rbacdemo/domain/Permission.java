package org.looko.mycloud.rbacdemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName permission
 */
@TableName(value ="permission")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 0-Read 1-Create 2-Update 3-Delete 4-Browse 5-Enable
     */
    private Integer action;

    /**
     * Page/Element/Contract/TradeOrder/Delivery
     */
    private String target;

    /**
     * Page/Element/Department
     */
    private String subject;

    /**
     * PageId/ElementId/DepartmentId
     */
    private Long subjectId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}