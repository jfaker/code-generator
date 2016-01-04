<t:include file="/include/common.jsp"/>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE sqlMap PUBLIC "-//ibatis.apache.org//DTD SQL Map 2.0//EN" "http://ibatis.apache.org/dtd/sql-map-2.dtd">
<sqlMap namespace="${modelClassName}">
    <resultMap id="${modelClassName}Result" class="${modelPackageName}.${modelClassName}"><c:forEach items="${columns}" var="column" varStatus="status">
        <result property="${column.variableName}" column="${column.columnCode}" jdbcType="${column.typeName}"/></c:forEach>
    </resultMap>

    <sql id="selectSql">
        <![CDATA[
        select<c:forEach items="${columns}" var="column" varStatus="status">
            ${column.columnCode}<c:if test="${status.index < (columns.size() - 1)}">,</c:if></c:forEach>
        from ${table.tableCode}
        ]]>
    </sql>

    <select id="getById" parameterClass="${modelPackageName}.${modelClassName}" resultMap="${modelClassName}Result">
        <include refid="selectSql"/>

        <dynamic prepend="where">
            <isNotEmpty prepend="and" property="${primaryKeyVariableName}">
                ${primaryKeyColumnName} = #${primaryKeyVariableName}: ${primaryKeyDataTypeName}#
            </isNotEmpty>
        </dynamic>

        <include refid="defaultOrderSql"/>
    </select>

    <insert id="insert" parameterClass="${modelPackageName}.${modelClassName}">
        <![CDATA[
        insert into ${table.tableCode}(<c:forEach items="${columns}" var="column" varStatus="status">
            ${column.columnCode}</c:forEach>
        ) values (<c:forEach items="${columns}" var="column" varStatus="status">
            #${column.variableName}: ${column.javaTypeName}<c:if test="${status.index < (columns.size() - 1)}">,</c:if></c:forEach>
        )
        ]]>
    </insert>

    <update id="updateById" parameterClass="${modelPackageName}.${modelClassName}">
        <![CDATA[
        update ${table.tableCode} set<c:forEach items="${columns}" var="column" varStatus="status">
            ${column.columnCode}=#${column.variableName}: ${column.javaTypeName}#<c:if test="${status.index < (columns.size() - 1)}">,</c:if></c:forEach>
        where ${primaryKeyColumnName}=#${primaryKeyVariableName}: ${primaryKeyDataTypeName}#
        ]]>
    </update>

    <delete id="deleteById" parameterClass="${modelPackageName}.${modelClassName}">
        <![CDATA[
        delete from ${table.tableCode} where ${primaryKeyColumnName}=#${primaryKeyVariableName}: ${primaryKeyDataTypeName}#
        ]]>
    </delete>
</sqlMap>