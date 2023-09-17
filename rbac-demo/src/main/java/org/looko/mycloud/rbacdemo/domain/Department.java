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
 * @TableName department
 */
@TableName(value ="department")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private Integer level;

    /**
     * 
     */
    private Long parentId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}