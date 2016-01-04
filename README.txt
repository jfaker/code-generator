code-generator
===================

code-generator是一个java版的代码生成器，它具有以下功能：
    支持从数据库根据数据模型从模板文件生成各种不同的代码。
    支持直接从sql文件生成代码，该方式不需要连接数据库，只要有建表语句即可。
    支持单表生成或者批量生成。
    支持从数据库导出数据，可导出sql格式，csv格式和自定义格式。

运行之前:
    1. 修改 classpath:META-INF/conf/datasource.xml, 指定数据库连接, 可以建多个, 如果需要加载数据库驱动程序, 请将jar文件拷贝到/lib目录
    2. 修改 /webapp/config/template.xml, 修改模板路径, /webapp/template/skin下面的文件可能不适合具体的项目, 根据自己项目的特点修改模板

使用中如果有问题, 请发邮件到: xuesong.net@163.com

代码文件生成之后保存在/webapp/gen目录下面, 也可以使用Finder查看或者下载

1. 单表生成
    单击要生成的表名，右边显示该表的结构，字段类型和映射类型，可以手动编辑生成的目标类型。

2. 批量生成
    在项目初期，可以一次生成所有的代码。单击某个连接下的Tables或者Views，右边显示所有的表或者视图。
    勾选要生成的表，然后单击批量生成，即可生成所有的代码。

3. 数据库连接配置
    在datasource.xml指定连接, 可以配置多个，配置之后，在连接页面将显示所有配置的数据库连接.
    支持任何类型的数据库。

4. 模板定义
    支持任意数量的模板，在template.xml中配置，需要指定模板路径和生成路径。

    <!-- model template -->
    <template name="modelTemplate" template="/skin/model.html" outputPath="webapp/gen/java/com/skin/model/${table.className}.java" enabled="true">
        <parameter name="packageName" value="com.skin.model" description=""/>
    </template>

    <!-- daoItfc template -->
    <template name="daoItfcTemplate" template="/skin/daoItfc.html" outputPath="webapp/gen/java/com/skin/dao/${table.className}Dao.java" enabled="true">
        <parameter name="packageName" value="com.skin.dao" description=""/>
    </template>

    <!-- daoImpl template -->
    <template name="daoImplTemplate" template="/skin/daoImpl.html" outputPath="webapp/gen/java/com/skin/dao/ibatis/${table.className}DaoIBatis.java" enabled="true">
        <parameter name="packageName" value="com.skin.dao.ibatis" description=""/>
    </template>

    <!-- manager template -->
    <template name="managerTemplate" template="/skin/manager.html" outputPath="webapp/gen/java/com/skin/service/${table.className}Manager.java" enabled="true">
        <parameter name="packageName" value="com.skin.service" description=""/>
    </template>

    <!-- sqlMap template -->
    <template name="sqlMapTemplate" template="/skin/sqlMap.html" outputPath="webapp/gen/sqlmaps/${table.className}SQL.xml" enabled="true"/>

5. 生成文件浏览
    基于web方式的文件浏览器, 可以查看和下载已经生成的代码。如果是在本地运行，直接通过资源浏览器即可查看。

6. 模板文件
    模板文件采用jstl语法编写, 使用自己开发的ayada模板引擎运行。
package ${modelPackageName};

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Title: ${modelClassName}</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ${modelClassName} implements Serializable {
    private static final long serialVersionUID = 1L;
<c:forEach items="${columns}" var="column" varStatus="status">
    <c:choose>
        <c:when test="${column.javaTypeName == 'String' || column.javaTypeName == 'java.lang.String'}">private String ${column.variableName};</c:when>
        <c:when test="${column.javaTypeName == 'Integer' || column.javaTypeName == 'java.lang.Integer'}">private Integer ${column.variableName};</c:when>
        <c:when test="${column.javaTypeName == 'Float' || column.javaTypeName == 'java.lang.Float'}">private Float ${column.variableName};</c:when>
        <c:when test="${column.javaTypeName == 'Double' || column.javaTypeName == 'java.lang.Double'}">private Double ${column.variableName};</c:when>
        <c:when test="${column.javaTypeName == 'Long' || column.javaTypeName == 'java.lang.Long'}">private Long ${column.variableName};</c:when>
        <c:when test="${column.javaTypeName == 'java.util.Date'}">private Date ${column.variableName};</c:when>
        <c:when test="${column.javaTypeName == 'java.sql.Date'}">private Date ${column.variableName};</c:when>
        <c:when test="${column.javaTypeName == 'java.sql.Timestamp'}">private Timestamp ${column.variableName};</c:when>
        <c:otherwise>private ${column.javaTypeName} ${column.variableName};</c:otherwise>
</c:choose></c:forEach>
...

下载地址: https://github.com/xuesong123
