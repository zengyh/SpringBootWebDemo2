select *
  from T_DRP_USER
where openid = 'o-4K9wpj9zDkVQVuWIdsjta4jvj4';
        
select * from T_WX_USER_INFO where  openid = 'oMkuL1aJ-j_oOz5JRn_B7nHJJ-qw';

select *
        from T_DRP_USER
        where  openid = 'oMkuL1aJ-j_oOz5JRn_B7nHJJ-qw';

select a.usercode,a.usercname,a.companycode from gguser a where a.usercode='PS03400126'  and a.validind='1';

select * from T_WX_USER_INFO u1 inner join T_DRP_USER u2 on u1.openid = u2.openid
where up_sales_no in (
   select a.usercode from gguser a where  a.validind='1'
);

select * from  gguser a where  a.validind='1' and usercode = 'PS01400314'


select * from T_FENXIAO_WXUSERINFO where openid = 'oMkuL1aJ-j_oOz5JRn_B7nHJJ-qw';

select *
  from T_DRP_USER where name = '杨朝红';

-- 机构表
select * from t_org;



select * from gguser a where a.validind='1';

        
select * from T_DRP_APPROVE_USER where name='曾燕辉';

select * from   t_fenxiao_user  t_business_user

-------------------------------------- 学习专区 ---------------------------------------
create table t_study_viedo(
    id  NUMBER(19),
    video_name VARCHAR2(200 CHAR) not null,
    video_content BLOB  not null,
    video_file_type  varchar(20)  not null,
    image_conent BLOB  not null,
    image_file_type  varchar(20)  not null,
    companycode VARCHAR2(10)  not null,
    create_by   VARCHAR2(255),
    create_date date,
    update_date date,
    update_by VARCHAR2(255)
);

comment on table t_study_viedo is '学习专区';
comment on column t_study_viedo.video_name is '视频名称';
comment on column t_study_viedo.video_content is '视频内容';
comment on column t_study_viedo.video_file_type is '视频格式';
comment on column t_study_viedo.image_conent is '预览图片内容';
comment on column t_study_viedo.image_file_type is '预览图片格式';
comment on column t_study_viedo.companycode is '所属机构代码';
comment on column t_study_viedo.create_by is '上传人员，内容为t_account.username';
comment on column t_study_viedo.create_date is '上传时间';
comment on column t_study_viedo.update_date is '修改时间';
comment on column t_study_viedo.update_by is '修改人员，内容为t_account.username';

create unique index indx_t_study_viedo_id on t_study_viedo(id);
alter table t_study_viedo add constraint pk_t_study_viedo_id primary key ( id ) using index indx_t_study_viedo_id;

create sequence T_STUDY_VIEDO_SEQUENCE
minvalue 1
maxvalue 99999999
start with 1
increment by 1
NOCACHE;

CREATE OR REPLACE TRIGGER TR_I_T_STUDY_VIDEO    
before insert ON t_study_viedo
FOR EACH ROW
BEGIN
 
 :new.create_date := sysdate;
 :new.update_date := sysdate;
END;

CREATE OR REPLACE TRIGGER TR_U_T_STUDY_VIDEO    
before update ON t_study_viedo
FOR EACH ROW
BEGIN
 :new.update_date := sysdate;
 if updating('VIDEO_CONTENT') then
    raise_application_error(-20000,'不能更新T_STUDY_VIEDO.VIDEO_CONTENT字段');
 end if;
 if updating('VIDEO_FILE_TYPE') then
    raise_application_error(-20000,'不能更新T_STUDY_VIEDO.VIDEO_FILE_TYPE字段');
 end if;
 if updating('CREATE_BY') then
    raise_application_error(-20000,'不能更新T_STUDY_VIEDO.CREATE_BY字段');
 end if;
 if updating('CREATE_DATE') then
     raise_application_error(-20000,'不能更新T_STUDY_VIEDO.CREATE_DATE字段');
 end if;
 if updating('ID') then
    raise_application_error(-20000,'不能更新T_STUDY_VIEDO.ID字段');
 end if;
 if updating('COMPANYCODE') then
    raise_application_error(-20000,'不能更新T_STUDY_VIEDO.COMPANYCODE字段');
 end if;
END;


create table t_study_viedo_log(
    id  NUMBER(19),
    video_name VARCHAR2(200 CHAR) not null,
    video_file_type  varchar(20)  not null,
    companycode VARCHAR2(10)  not null,
    create_by   VARCHAR2(255),
    create_date date,
    operator  VARCHAR2(255),
    log_date date,    
    operator_msg VARCHAR2(255)
);

comment on table t_study_viedo_log is '学习专区日志表';
comment on column t_study_viedo_log.video_name is '视频名称';
comment on column t_study_viedo_log.video_file_type is '视频格式';
comment on column t_study_viedo_log.companycode is '所属机构代码';
comment on column t_study_viedo_log.create_by is '上传人员，内容为t_account.username';
comment on column t_study_viedo_log.create_date is '上传时间';
comment on column t_study_viedo_log.operator is '操作人员，内容为t_account.username';
comment on column t_study_viedo_log.log_date is '操作时间';
comment on column t_study_viedo_log.operator_msg is '操作内容';

create unique index indx_t_study_viedo_lg_id on t_study_viedo_log(id);
alter table t_study_viedo_log add constraint pk_t_study_viedo_lg_id primary key ( id ) using index indx_t_study_viedo_lg_id;

create sequence T_STUDY_VIEDO_LOG_SEQUENCE
minvalue 1
maxvalue 99999999
start with 1
increment by 1
NOCACHE;

-------------------------------------- 用户信息 ---------------------------------------
-- 微信管理后台用户
select * from t_account where username = 'admin';

-- 代理人员
SELECT   businessCode as 代理序号, 
         agencyCode as 中介人编码,
         agencyName as 中介人名称,
         solutionCode as 分配方案,
         agreementNo as 协议号,
         certificateNo as 经营许可证号, 
         case
           when businessnature ='00101' then  '直销销售'
           when businessnature ='00301' then '自营移动端项目'
           when businessnature ='00303' then  '自营移动端项目（微信）'
           when businessnature ='10101' then '个人代理销售'
           when businessnature ='20101' then '专业代理销售'
           when businessnature ='30101' then '汽车经销商销售'
           when businessnature ='30201' then '银行代理销售'
           when businessnature ='30202' then '邮政代理销售'
           when businessnature ='30401' then '其他兼业代理'
           when businessnature ='30102' then '汽车修理商销售'
           when businessnature ='40101' then '经纪业务销售'
           when businessnature ='20102' then '专代网销'
           else  businessnature 
         end as 渠道类型,
         case when status = '0' then '正常'
              when status = '1' then '待审核'
              else status
         end as 审核状态,
         approve_by as 审核人,
         approve_time as 审核时间,
         companycode,
         partnername,
         partnerid
from T_CAR_PROXY;

-- 分销人员，分销和个代的佣金比例、账户金额、openid 等信息
SELECT * FROM T_DRP_USER where name ='曾燕辉';
-- 分销人员的注册信息(含openid 和工号workid)
select * from   t_fenxiao_user where name ='曾燕辉' and status = '0' /* status=0 未删除 */;
-- 根据工号查询人员归属的机构、上级业务员、人员类型、业绩归属人
select * from gguser where usercode = (select workid from  t_fenxiao_user where name ='曾燕辉');
-- 机构代码表
select * from ggcompany t where companycode = (select companycode from gguser where usercode = (select workid from  t_fenxiao_user where name ='曾燕辉'));
-- 查询分销人员的微信头像、微信昵称
select * from T_WX_USER_INFO where openid=(select openid from   t_fenxiao_user where name ='曾燕辉');

 select * from ggcompany where companycode  like '02%' order by length(companycode);
  select * from ggcompany where companycode  not like '02%' order by length(companycode);


-------------------------------------- 消息模块 ---------------------------------------
-- 发送给机构的消息
SELECT  * from T_MSG_INFO where  status='1' and type='0' ORDER BY CREATE_TIME DESC;
-- 用户接收哪个机构的消息
select a.usercode,a.usercname,a.companycode from gguser a where /* a.usercode=#{usercode}  and*/ a.validind='1';
-- 发送给用户的私人消息
SELECT  t.* from T_MSG_INFO t,t_msg_read t1 where  t.status='1' and t.type ='1'  and t.id=t1.msg_id /*and t1.sales_no=#{usercode}*/;


  
