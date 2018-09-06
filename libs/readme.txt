项目依赖的本地第三方jar包存放位置(既Maven仓库上没有的jar包，只能网上找其它资源下载的)，放在了这个路径之后，还要在pom.xml中配置，如下：
		<!-- 添加 ODBC 驱动包 -->
		<dependency>
			<groupId>com.oracle</groupId>
			<artifactId>ojdbc8</artifactId>
			<scope>system</scope>
			<systemPath>${project.basedir}/libs/ojdbc8.jar</systemPath>
            <version>8</version> <!-- 随便配置的版本号，不配置编译打包等会报错 -->
		</dependency>
