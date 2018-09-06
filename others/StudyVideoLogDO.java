package io.bloom.insurance.web.model;

import io.bloom.buava.model.ModelDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 编写人: yh.zeng
 * 编写时间: 2018-8-24
 * 文件描述: 学习专区上视频操作日志
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudyVideoLogDO extends ModelDO {
    private String videoName;      //视频名称

    private String videoFileType;   //视频格式

    private String companyCode;   //所属机构代码

    private String createBy;   //上传人员，内容为t_account.username

    private String operator;   //操作人员，内容为t_account.username

    private String companyCName;  //所属机构名称

    private String operatorMsg;  //操作描述

}
