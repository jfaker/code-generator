code-generator
===================

code-generator��һ��java��Ĵ��������������������¹��ܣ�
    ֧�ִ����ݿ��������ģ�ʹ�ģ���ļ����ɸ��ֲ�ͬ�Ĵ��롣
    ֧��ֱ�Ӵ�sql�ļ����ɴ��룬�÷�ʽ����Ҫ�������ݿ⣬ֻҪ�н�����伴�ɡ�
    ֧�ֵ������ɻ����������ɡ�
    ֧�ִ����ݿ⵼�����ݣ��ɵ���sql��ʽ��csv��ʽ���Զ����ʽ��

����֮ǰ:
    1. �޸� classpath:META-INF/conf/datasource.xml, ָ�����ݿ�����, ���Խ����, �����Ҫ�������ݿ���������, �뽫jar�ļ�������/libĿ¼
    2. �޸� /webapp/config/template.xml, �޸�ģ��·��, /webapp/template/skin������ļ����ܲ��ʺϾ������Ŀ, �����Լ���Ŀ���ص��޸�ģ��

ʹ�������������, �뷢�ʼ���: xuesong.net@163.com

�����ļ�����֮�󱣴���/webapp/genĿ¼����, Ҳ����ʹ��Finder�鿴��������

1. ��������
    ����Ҫ���ɵı������ұ���ʾ�ñ�Ľṹ���ֶ����ͺ�ӳ�����ͣ������ֶ��༭���ɵ�Ŀ�����͡�

2. ��������
    ����Ŀ���ڣ�����һ���������еĴ��롣����ĳ�������µ�Tables����Views���ұ���ʾ���еı������ͼ��
    ��ѡҪ���ɵı�Ȼ�󵥻��������ɣ������������еĴ��롣

3. ���ݿ���������
    ��datasource.xmlָ������, �������ö��������֮��������ҳ�潫��ʾ�������õ����ݿ�����.
    ֧���κ����͵����ݿ⡣

4. ģ�嶨��
    ֧������������ģ�壬��template.xml�����ã���Ҫָ��ģ��·��������·����

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

5. �����ļ����
    ����web��ʽ���ļ������, ���Բ鿴�������Ѿ����ɵĴ��롣������ڱ������У�ֱ��ͨ����Դ��������ɲ鿴��

6. ģ���ļ�
    ģ���ļ�����jstl�﷨��д, ʹ���Լ�������ayadaģ���������С�
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

���ص�ַ: https://github.com/xuesong123
