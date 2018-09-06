package io.bloom.insurance.web.model;

import io.bloom.buava.model.ModelDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 编写人: yh.zeng
 * 编写时间: 2018-8-24
 * 文件描述: 学习专区上传的视频对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudyVideoDO extends ModelDO {
    private String videoName;      //视频名称

    private byte[] videoContent;   //视频内容

    private String videoFileType;   //视频格式

    private byte[] imageContent;   //预览图片内容

    private String imageFileType;   //预览图片格式

    private String companyCode;   //所属机构代码

    private String createBy;   //上传人员，内容为t_account.username

    private String updateBy;   //修改人员，内容为t_account.username

    private String companyCName;  //所属机构名称

}
