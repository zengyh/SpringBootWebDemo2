<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>学习专区视频上传</title>
    <%@ include file="../include/admin-head.jsp" %>
    <!-- Bootstrap table -->
    <link href="${pageContext.request.contextPath}/assets/bootstrap-table/dist/bootstrap-table.min.css" rel="stylesheet"
          type="text/css">

    <script type="text/javascript">
        //校验上传的视频预览图片文件类型
        function checkImageFile(el){
            var allowTypes = ["image/jpeg","image/png","image/x-png","image/bmp","image/gif"]; //允许上传的文件类型
            var allowUpload = true; //经过校验之后是否允许上传
            var errorMessage = "";  //校验文件之后，文件不符合要求的提示信息

            if(el.files){ //非IE
                var files = el.files;
                for(var i=0; i< files.length; i++){
                    var fileName = files[i].name;    //文件名
                    var fileType = files[i].type;    //文件类型

                    var typeAccepted = false;
                    for(var j = 0; j < allowTypes.length; j++){
                        if(allowTypes[j] == fileType){
                            typeAccepted = true;
                            break;
                        }
                    }
                    if(typeAccepted != true){
                        errorMessage += fileName + "不是图片，只能上传图片！";
                        allowUpload = false;
                    }
                }
            }else{    //兼容IE
                var filePath = el.value;
                var fileSystem = new ActiveXObject("Scripting.FileSystemObject");
                var file = fileSystem.GetFile (filePath);
                var fileName = file.name;    //文件名
                var fileType = file.type;    //文件类型

                var typeAccepted = false;
                for(var j = 0; j < allowTypes.length; j++){
                    if(allowTypes[j] == fileType){
                        typeAccepted = true;
                        break;
                    }
                }
                if(typeAccepted != true){
                    errorMessage += fileName + "不是图片，只能上传图片！";
                    allowUpload = false;
                }
            }

            if(allowUpload != true){
                el.outerHTML = el.outerHTML; //清空选择的文件
                alert(errorMessage);
            }
        }

        //校验上传的视频文件类型
        function checkVideoFile(el){
            var allowTypes = ["video/x-flv","video/mp4","video/3gpp","video/quicktime","video/x-msvideo","video/x-ms-wmv"]; //允许上传的文件类型flv,mp4,3gp,mov,avi,wmv
            var allowUpload = true; //经过校验之后是否允许上传
            var errorMessage = "";  //校验文件之后，文件不符合要求的提示信息

            if(el.files){ //非IE
                var files = el.files;
                for(var i=0; i< files.length; i++){
                    var fileName = files[i].name;    //文件名
                    var fileType = files[i].type;    //文件类型

                    var typeAccepted = false;
                    for(var j = 0; j < allowTypes.length; j++){
                        if(allowTypes[j] == fileType){
                            typeAccepted = true;
                            break;
                        }
                    }
                    if(typeAccepted != true){
                        errorMessage += fileName + "不是视频，只能上传视频！";
                        allowUpload = false;
                    }else{
                        var indx = fileName.lastIndexOf(".");
                        $("#title").val(fileName.substring(0,indx));
                    }
                }
            }else{    //兼容IE
                var filePath = el.value;
                var fileSystem = new ActiveXObject("Scripting.FileSystemObject");
                var file = fileSystem.GetFile (filePath);
                var fileName = file.name;    //文件名
                var fileType = file.type;    //文件类型

                var typeAccepted = false;
                for(var j = 0; j < allowTypes.length; j++){
                    if(allowTypes[j] == fileType){
                        typeAccepted = true;
                        break;
                    }
                }
                if(typeAccepted != true){
                    errorMessage += fileName + "不是视频，只能上传视频！";
                    allowUpload = false;
                }else{
                    var indx = fileName.lastIndexOf(".");
                    $("#title").val(fileName.substring(0,indx));
                }
            }

            if(allowUpload != true){
                el.outerHTML = el.outerHTML; //清空选择的文件
                $("#title").val("");
                alert(errorMessage);
            }
        }
        function checkForm(){
            if(document.getElementById("file1").value == ""){
                alert("请选择要上传的视频文件！");
                return false;
            }else if(document.getElementById("file2").value == ""){
                alert("请选择要上传的视频预览图片！");
                return false;
            }
            return true;
        }
    </script>
</head>

<body>
<div id="wrapper">

    <%@ include file="../include/admin-nav.jsp" %>

    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h3 class="page-header">添加学习专区视频</h3>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <form action="${pageContext.request.contextPath}/admin/study-manage/video-upload" method="post" class="form-horizontal"  enctype="multipart/form-data" onsubmit="return checkForm();">
                    <div class="form-group">
                        <label for="file1" class="col-sm-2 control-label">视频文件</label>
                        <div class="col-sm-10">
                            <input type="file" class="form-control" name="file1" id="file1" onchange="checkVideoFile(this)"/> <br>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="file2" class="col-sm-2 control-label">视频预览图片</label>
                        <div class="col-sm-10">
                            <input type="file" class="form-control" name="file2" id="file2" onchange="checkImageFile(this)"/> <br>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="title" class="col-sm-2 control-label">视频名称</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="title" id="title" placeholder="默认为视频文件名称"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <input  class="btn btn-primary btn-submit-do" type="submit" value="提交">
                        </div>
                    </div>
                </form>
            </div>
        </div>

    </div>
</div>
<%@ include file="../include/admin-footer-script.jsp" %>
</body>
</html>