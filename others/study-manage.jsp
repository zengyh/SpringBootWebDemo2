<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>学习专区管理</title>
    <%@ include file="../include/admin-head.jsp" %>
    <!-- Bootstrap table -->
    <link href="${pageContext.request.contextPath}/assets/bootstrap-table/dist/bootstrap-table.min.css" rel="stylesheet"
          type="text/css">
    <style>
        .table-responsive {
            position: relative;
        }
      #chooseUser span{

      }
    </style>
</head>

<body>

<div id="wrapper">

    <%@ include file="../include/admin-nav.jsp" %>

    <div id="page-wrapper">
        <div class="row">
            <div class="col-md-12">
                <h3>视频列表</h3>
                <%--<span>注意:推广用户的二维码的有效期为30天</span>--%>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->

        <div class="row">
                <table id="J_Table" data-toolbar="#toolbar" class="table-striped text-center"></table>
            </div>
        </div>

    </div>

    <div id="toolbar">
        <div class="form-inline" role="form">
            <div class="table-responsive">
                <div class="form-group">
                    <button id="J_AddBtn" class="btn btn-default">添加视频+</button>
            <br>
            <div class="form-group input-group">
                <span class="input-group-addon">归属机构</span>
                <div class="col-sm-10">
                    <select class="form-control" name="companyCode" id="J_Areas">
                        <c:forEach items="${ggCompanyDOList}" var="ggCompanyDO">
                            <option value="${ggCompanyDO.companyCode}">
                                    ${ggCompanyDO.companyCode}-${ggCompanyDO.companyCName}
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="form-group input-group">
                <span class="input-group-addon">开始时间</span>
                <input type="date" class="form-control" value="" name="startDate" placeholder="开始时间:2016-01-01"
                       id="J_StartDate"/>
            </div>
            <div class="form-group input-group">
                <span class="input-group-addon">结束时间</span>
                <input type="date" class="form-control" value="" name="endDate" placeholder="结束时间:2016-12-30"
                       id="J_EndDate"/>
            </div>
            <div class="form-group input-group">
                <span class="input-group-addon">视频名称</span>
                <input type="text" class="form-control" value="" name="videoName" id="J_VideoName"/>
            </div>
            <div class="form-group input-group">
                 <span class="input-group-addon">上传人员</span>
                 <input type="text" class="form-control" value="" name="createBy" id="J_CreateBy"/>
            </div>
            <div class="form-group input-group">
                 <span class="input-group-addon">修改人员</span>
                  <input type="text" class="form-control" value="" name="updateBy" id="J_UpdateBy"/>
             </div>
            <div class="form-group">
                <button id="J_Search" class="btn btn-default">查找</button>
            </div>
        </div>
    </div>

</div>

<!-- /#wrapper -->
<%@ include file="../include/admin-footer-script.jsp" %>
<!-- Bootstrap table -->
<script src="${pageContext.request.contextPath}/assets/bootstrap-table/dist/bootstrap-table.min.js"></script>
<script src="${pageContext.request.contextPath}/static/bloom/js/bloom.js"></script>
<script>

    function optionFormatter(value, row, index) {
        var link = '<button class="btn btn-option btn-default btn-remove"  data-id="' + row.id + '"data-event="updateVideo" >修改</button>'
                 + '<button class="btn btn-option btn-default btn-remove"  data-id="' + row.id + '"data-event="delVideo" >删除</button>';
        return link;
    }

    function videoFormatter(value, row, index) {
        var link='<a href="${pageContext.request.contextPath}/mobile/study/videopreview/' + row.id + '">' + row.videoName + '</a>';
        return link;
    }

    function imageFormatter(value, row, index) {
        var link='<a href="${pageContext.request.contextPath}/mobile/study/imagepreview/' + row.id + '">' + row.videoName + '</a>';
        return link;
    }

    var $table = $('#J_Table').bootstrapTable({
        method: 'get',
        url: '${pageContext.request.contextPath}/admin/study-manage/study-list',
        cache: false,
        toggle: 'table',
        striped: true,
        pagination: true,
        pageSize: 5,
        pageList: [5],
        search: false,
        showColumns: false,
        showRefresh: false,
        minimumCountColumns: 2,
        sidePagination: "server", //服务端请求
        toolbar: '#toolbar',
        showExport: true,
        exportDataType:"all",
        columns: [
            {
                field: 'stateBox',
                checkbox: true
            }
            , {
                field: 'id',
                title: 'ID',
                width: 75,
                align: 'center',
                valign: 'middle'
            }
            , {
                field: 'videoName',
                title: '视频名称',
                width: 100,
                align: 'center',
                valign: 'middle',
                formatter: videoFormatter
            }
            , {
                field: 'videoName',
                title: '视频预览图片',
                width: 100,
                align: 'center',
                valign: 'middle',
                formatter: imageFormatter
            }
            , {
                field: 'companyCName',
                title: '归属机构',
                width: 335,
                align: 'center',
                valign: 'middle'
            }
            , {
                field: 'createBy',
                title: '上传人员',
                width: 170,
                align: 'center',
                valign: 'middle',
            }
            ,{
                field: 'createTime',
                title: '上传时间',
                width: 170,
                align: 'center',
                valign: 'middle',
            }
            , {
                field: 'updateBy',
                title: '最后修改人员',
                width: 170,
                align: 'center',
                valign: 'middle',
                clickToSelect: false
            }
            ,{
                field: 'updateTime',
                title: '最后修改时间',
                width: 170,
                align: 'center',
                valign: 'middle',
            }
            , {
                field: 'option',
                title: '操作',
                width: 100,
                align: 'center',
                valign: 'middle',
                formatter: optionFormatter
            }
        ]
    });

    //table refresh
    $table.on('page-change.bs.table', function () {
        setTimeout(function () {
            $(".qr").each(function () {
                var url = $(this).data('url');
                if (url) {
                    $(this).qrcode({width: 80, height: 80, correctLevel: 0, text: url});
                }
            });

        }, 1000);
    });
    //操作

    $(function () {

        //搜索
        $('#J_Search').click(function () {
            var url = bloom
                    .queryString('/admin/study-manage/study-list')
                    .param("companyCode", $('#J_Areas').val())
                    .param("videoName", $('#J_VideoName').val())
                    .param("startDate", $('#J_StartDate').val())
                    .param("endDate", $('#J_EndDate').val())
                    .param("createBy", $('#J_CreateBy').val())
                    .param("updateBy", $('#J_UpdateBy').val())
                    .toUrl();

            $table.bootstrapTable('refresh', {
                silent: true,
                url: url
            });
        });
        $("#J_Table").on("click", ".btn-option", function () {

            var event = $(this).data('event');
            var id = $(this).data('id');
            if(event=='updateVideo')
            {
                window.location.href ='/admin/study-manage/video-edit/'+id;
            }
            if(event=='delVideo')
            {
                $.ajax({
                    url: '${pageContext.request.contextPath}/admin/delVideo',
                    type: 'POST',
                    data: {id: id},
                    dataType: 'json',
                    async: false,
                    success: function (data) {
                        if (data.code == 0 ) {
                            alert('删除成功！');
                            window.location.href = '${pageContext.request.contextPath}/admin/study/study-manage/index';
                        } else {
                            alert("删除失败！");
                        }
                    },
                    error: function (data) {
                        alert("请求出错！");
                    }
                });
            }
        });
    });

    //添加
    $('#J_AddBtn').on('click', function () {
        window.location.href = '${pageContext.request.contextPath}/admin/study-manage/video-upload';
    });
   function refreshTable () {
       var url = bloom
               .queryString('/admin/study-manage/study-list')
               .toUrl();

       $table.bootstrapTable('refresh', {
           silent: true,
           url: url
       });
   }

</script>
</body>

</html>