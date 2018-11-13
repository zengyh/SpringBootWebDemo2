项目架构：Spring Boot + Log4j2 + Hibernate + jsp + thymeleaf

项目管理工具：Maven

项目启动方式：<br>
1、IDE 通过 Run/Debug Configurations 配置外置Tomcat         <br>
2、执行Maven clean，清理掉上一次构建生成的所有文件，这一步可以省掉！
  因为，目前pom.xml里面配置了package之前会自动执行clean，
  前提是该配置在以后的Maven版本都兼容的情况下！！！               <br>
3、执行Maven package 生产成war包！                           <br>
4、IDE 运行Tomcat 

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
6. src/main/webapp/WEB-INF/views，页面存放路径<br>
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

5. 要使用SpringBoot热部署必须注意
   当项目使用的是Intellij Idea，那么必须修改以下两个地方<br>
   5.1 勾上自动编译或者手动重新编译<br>
       File ——》 Settings ——》 Compiler ——》 Build Project automatically<br>
   5.2 注册
       ctrl + shift + alt + / ——》 Registry ——》 勾选Compiler autoMake allow when app running
   
6. IDE插件必须安装Lombok插件，否则有的类会报“No Such Method”，但项目编译运行是没问题的！只是IDE中看起来红色的，IDE会提醒错误！ 
具体的安装办法可参考文档 <a href="https://blog.csdn.net/yh_zeng2/article/details/81989902">《Eclipse安装Lombok插件》</a>

7. 使用外置Tomcat，建议版本8.0以上，个人使用的版本是8.5


8. pom.xml里面配置了package之前会自动执行clean，以后的Maven版本可能不兼容！！那么就需要去掉该项配置，并且每次打包（pacakge）之前都要clean！！ 

			<!-- compile、test、package等操作之前都先进行clean，所以无需手动clean -->
			<!-- 目前没有问题，假设以后该插件的配置不兼容了，可以去掉！-->
			<!-- 如果去掉了该项配置，就需要执行这些步骤之前，手动【clean】以下， -->
			<!--   否则，打包的资源文件里面，会包含多个环境(profile)的properties，虽然不影响程序运行，但是且包含了不必要（别的环境）的配置文件！！-->
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-clean-plugin</artifactId>
				<executions>
					<execution>
                        <phase>validate</phase>
						<goals>
							<goal>clean</goal>
						</goals>
                    </execution>
				</executions>
			</plugin>
			<!-- end -->