<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="hotcss" content="initial-dpr=1">
    <title>学习专区</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/mobile/css/mobile.css">
    <link rel="stylesheet" href="/static/mobile/css/index.css">
    <!-- 视频插件video.js的样式-->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/ueditor/third-party/video-js/video-js.css">
    <script src="${pageContext.request.contextPath}/static/mobile/js/all.min.js"></script>
    <!-- 视频插件video.js -->
    <script src="${pageContext.request.contextPath}/static/ueditor/third-party/video-js/video.js"></script>
    <script>
        videojs.options.flash.swf = "video-js.swf";
    </script>
</head>
<body>
  <video id="example_video_1" style="width: 100%; height: 100%;" class="video-js vjs-default-skin vjs-big-play-centered" controls preload="none"
           poster="${pageContext.request.contextPath}/mobile/study/video/image/${id}">
          <source src="${pageContext.request.contextPath}/mobile/study/video/${id}" type='${videoFileType}'/>
  </video>
</body>
</html>
