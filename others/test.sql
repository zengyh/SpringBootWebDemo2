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
  from T_DRP_USER where name = '���';

-- ������
select * from t_org;



select * from gguser a where a.validind='1';

        
select * from T_DRP_APPROVE_USER where name='�����';

select * from   t_fenxiao_user  t_business_user

-------------------------------------- ѧϰר�� ---------------------------------------
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

comment on table t_study_viedo is 'ѧϰר��';
comment on column t_study_viedo.video_name is '��Ƶ����';
comment on column t_study_viedo.video_content is '��Ƶ����';
comment on column t_study_viedo.video_file_type is '��Ƶ��ʽ';
comment on column t_study_viedo.image_conent is 'Ԥ��ͼƬ����';
comment on column t_study_viedo.image_file_type is 'Ԥ��ͼƬ��ʽ';
comment on column t_study_viedo.companycode is '������������';
comment on column t_study_viedo.create_by is '�ϴ���Ա������Ϊt_account.username';
comment on column t_study_viedo.create_date is '�ϴ�ʱ��';
comment on column t_study_viedo.update_date is '�޸�ʱ��';
comment on column t_study_viedo.update_by is '�޸���Ա������Ϊt_account.username';

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
    raise_application_error(-20000,'���ܸ���T_STUDY_VIEDO.VIDEO_CONTENT�ֶ�');
 end if;
 if updating('VIDEO_FILE_TYPE') then
    raise_application_error(-20000,'���ܸ���T_STUDY_VIEDO.VIDEO_FILE_TYPE�ֶ�');
 end if;
 if updating('CREATE_BY') then
    raise_application_error(-20000,'���ܸ���T_STUDY_VIEDO.CREATE_BY�ֶ�');
 end if;
 if updating('CREATE_DATE') then
     raise_application_error(-20000,'���ܸ���T_STUDY_VIEDO.CREATE_DATE�ֶ�');
 end if;
 if updating('ID') then
    raise_application_error(-20000,'���ܸ���T_STUDY_VIEDO.ID�ֶ�');
 end if;
 if updating('COMPANYCODE') then
    raise_application_error(-20000,'���ܸ���T_STUDY_VIEDO.COMPANYCODE�ֶ�');
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

comment on table t_study_viedo_log is 'ѧϰר����־��';
comment on column t_study_viedo_log.video_name is '��Ƶ����';
comment on column t_study_viedo_log.video_file_type is '��Ƶ��ʽ';
comment on column t_study_viedo_log.companycode is '������������';
comment on column t_study_viedo_log.create_by is '�ϴ���Ա������Ϊt_account.username';
comment on column t_study_viedo_log.create_date is '�ϴ�ʱ��';
comment on column t_study_viedo_log.operator is '������Ա������Ϊt_account.username';
comment on column t_study_viedo_log.log_date is '����ʱ��';
comment on column t_study_viedo_log.operator_msg is '��������';

create unique index indx_t_study_viedo_lg_id on t_study_viedo_log(id);
alter table t_study_viedo_log add constraint pk_t_study_viedo_lg_id primary key ( id ) using index indx_t_study_viedo_lg_id;

create sequence T_STUDY_VIEDO_LOG_SEQUENCE
minvalue 1
maxvalue 99999999
start with 1
increment by 1
NOCACHE;

-------------------------------------- �û���Ϣ ---------------------------------------
-- ΢�Ź����̨�û�
select * from t_account where username = 'admin';

-- ������Ա
SELECT   businessCode as �������, 
         agencyCode as �н��˱���,
         agencyName as �н�������,
         solutionCode as ���䷽��,
         agreementNo as Э���,
         certificateNo as ��Ӫ���֤��, 
         case
           when businessnature ='00101' then  'ֱ������'
           when businessnature ='00301' then '��Ӫ�ƶ�����Ŀ'
           when businessnature ='00303' then  '��Ӫ�ƶ�����Ŀ��΢�ţ�'
           when businessnature ='10101' then '���˴�������'
           when businessnature ='20101' then 'רҵ��������'
           when businessnature ='30101' then '��������������'
           when businessnature ='30201' then '���д�������'
           when businessnature ='30202' then '������������'
           when businessnature ='30401' then '������ҵ����'
           when businessnature ='30102' then '��������������'
           when businessnature ='40101' then '����ҵ������'
           when businessnature ='20102' then 'ר������'
           else  businessnature 
         end as ��������,
         case when status = '0' then '����'
              when status = '1' then '�����'
              else status
         end as ���״̬,
         approve_by as �����,
         approve_time as ���ʱ��,
         companycode,
         partnername,
         partnerid
from T_CAR_PROXY;

-- ������Ա�������͸�����Ӷ��������˻���openid ����Ϣ
SELECT * FROM T_DRP_USER where name ='�����';
-- ������Ա��ע����Ϣ(��openid �͹���workid)
select * from   t_fenxiao_user where name ='�����' and status = '0' /* status=0 δɾ�� */;
-- ���ݹ��Ų�ѯ��Ա�����Ļ������ϼ�ҵ��Ա����Ա���͡�ҵ��������
select * from gguser where usercode = (select workid from  t_fenxiao_user where name ='�����');
-- ���������
select * from ggcompany t where companycode = (select companycode from gguser where usercode = (select workid from  t_fenxiao_user where name ='�����'));
-- ��ѯ������Ա��΢��ͷ��΢���ǳ�
select * from T_WX_USER_INFO where openid=(select openid from   t_fenxiao_user where name ='�����');

 select * from ggcompany where companycode  like '02%' order by length(companycode);
  select * from ggcompany where companycode  not like '02%' order by length(companycode);


-------------------------------------- ��Ϣģ�� ---------------------------------------
-- ���͸���������Ϣ
SELECT  * from T_MSG_INFO where  status='1' and type='0' ORDER BY CREATE_TIME DESC;
-- �û������ĸ���������Ϣ
select a.usercode,a.usercname,a.companycode from gguser a where /* a.usercode=#{usercode}  and*/ a.validind='1';
-- ���͸��û���˽����Ϣ
SELECT  t.* from T_MSG_INFO t,t_msg_read t1 where  t.status='1' and t.type ='1'  and t.id=t1.msg_id /*and t1.sales_no=#{usercode}*/;


  
