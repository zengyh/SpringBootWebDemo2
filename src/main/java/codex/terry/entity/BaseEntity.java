package codex.terry.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 编写人: yh.zeng
 * 编写时间: 2018-8-28
 * 文件描述: 实体类的父类模板，子类必须实现 @Entity或@Table注解
 *
 * @MappedSuperclass的用法
 *    注解的类将不是一个完整的实体类，他将不会映射到数据库表，
 *    但是他的属性都将映射到其子类的数据库字段中，不能再标注@Entity或@Table注解
 */
@MappedSuperclass
public class BaseEntity implements Serializable
{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public  Long id;


}
