项目架构：Spring Boot + Log4j2 + Hibernate + jsp

项目管理工具：Maven

项目文件夹介绍：<br>
1. libs，项目依赖的本地第三方jar包存放位置(既Maven仓库上没有的jar包，只能网上找其它资源下载的) <br>
2. src/main/java，代码存放路径 <br>
  2.1 codex.terry.webxml 代替web.xml的Java配置代码<br>
     &nbsp;&nbsp;&nbsp;&nbsp;2.1.1 codex.terry.webxml.FilterConfiguration 注册Filter过滤器<br>
  2.2 codex.terry.controller Controller目录<br>
  2.3 codex.terry.entity 实体类目录<br>
  2.4 codex.terry.filter 过滤器目录<br>
3. src/main/resources，项目资源文件存放路径 <br>
4. src/main/profiles，多个环境（开发dev、测试test、生产prod）配置文件存放路径 <br>
5. src/test，测试代码存放路径<br>
6. src/main/webapp/WEB-INF/jsp，jsp页面存放路径<br>
7. src/main/webapp/WEB-INF/static，静态资源存放路径

注意事项：<br>
1. 项目依赖的本地第三方jar包(既Maven仓库上没有的jar包，只能网上找其它资源下载的)，放在了libs\路径之后，还要在pom.xml中配置，如下：<br>
		&lt;!-- 添加 ODBC 驱动包 --&gt;<br>
		&lt;dependency&gt;<br>
			&lt;groupId&gt;com.oracle&lt;/groupId&gt;<br>
			&lt;artifactId&gt;ojdbc8&lt;/artifactId&gt;<br>
			&lt;scope&gt;system&lt;/scope&gt;<br>
			&lt;systemPath&gt;${project.basedir}/libs/ojdbc8.jar&lt;/systemPath&gt;<br>
            &lt;version&gt;8&lt;/version&gt; &lt;!-- 随便配置的版本号，不配置编译打包等会报错 --&gt; <br>
		&lt;/dependency&gt;

2. 必须在Intellij Idea中设置Web Resource Directory，否则src/main/webapp下新建jsp、html等页面资源的时候，不会根据标准模板内容进行生成文件。<br>
   设置步骤：<br>
      左上角，File中点击Project Structure项，在Modules选项卡中，找到Web模块，在Web Resource Directories中点击+添加Web Resource Directory。<br>
   也可以参考博客<a href="https://blog.csdn.net/yh_zeng2/article/details/82377985">《【IntelliJ IDEA】使用idea解决新建jsp文件而找不到jsp文件模版的新建选项》</a>
   
3. 项目的JDK必须是1.8

4. Maven配置的JDK版本必须是1.8
