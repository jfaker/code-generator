<t:include file="/include/common.jsp"/>
/*
 * $RCSfile: ${daoClassName}.java,v $$
 * $Revision: 1.1 $
 * $Date: ${timestamp} $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package ${daoPackageName};

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import ${modelPackageName}.${modelClassName};
import com.skin.j2ee.dao.Dao;
import com.skin.j2ee.dao.Insert;
import com.skin.j2ee.dao.Update;

/**
 * <p>Title: ${daoClassName}</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author ${author}
 * @version 1.0
 */
public class ${daoClassName} extends Dao<${modelClassName}, ${primaryKeyJavaTypeWrapName}> {
    /**
     *
     */
    public ${daoClassName}() {
    }

    /**
     * @param connection
     */
    public ${daoClassName}(Connection connection) {
        super(connection);
    }

    /**
     * @param ${primaryKeyVariableName}
     * @return ${modelClassName}
     */
    public ${modelClassName} getById(${primaryKeyJavaTypeName} ${primaryKeyVariableName}) {
        String sql = "select * from ${tableName} where ${primaryKeyColumnName}=?";
        return this.getBean(sql, new Object[]{${primaryKeyVariableName}});
    }

    /**
     * @param ${modelVariableName}
     * @return int
     */
    public int create(${modelClassName} ${modelVariableName}) {
        Insert insert = new Insert("${tableName}", ${modelVariableName}, true);
        return insert.execute(this.getConnection());
    }

    /**
     * @param ${modelVariableName}
     * @return int
     */
    public int update(${modelClassName} ${modelVariableName}) {
        Update update = new Update("${tableName}", ${modelVariableName}, false);
        update.where("${primaryKeyVariableName}", "=", ${modelVariableName}.${primaryKeyMethodGetter}());
        return update.execute(this.getConnection());
    }

    /**
     * @param ${primaryKeyVariableName}
     * @param params
     * @return int
     */
    public int update(${primaryKeyJavaTypeName} ${primaryKeyVariableName}, Map<String, Object> params) {
        Update update = new Update("${tableName}", params, false);
        update.where("${primaryKeyVariableName}", "=", ${primaryKeyVariableName});
        return update.execute(this.getConnection());
    }

    /**
     * @param pageNum
     * @param pageSize
     * @return List<${modelClassName}>
     */
    public List<${modelClassName}> list(int pageNum, int pageSize) {
        String sql = "select * from ${tableName}";
        return this.getList(sql, new Object[0], pageNum, pageSize);
    }

<t:comment>
    /**
     * @param size
     * @return List<${modelClassName}>
     */
    public List<${modelClassName}> list(int size) {
        String sql = "select * from ${tableName}";
        return this.getList(sql, new Object[0], 1, pageSize);
    }
</t:comment>
    /**
     * @param ${primaryKeyVariableName}
     * @return int
     */
    public int delete(${primaryKeyJavaTypeName} ${primaryKeyVariableName}) {
        if(${primaryKeyVariableName} > 0L) {
            return this.update("delete from ${tableName} where ${primaryKeyColumnName}=?", new Object[]{${primaryKeyVariableName}});
        }
        return 0;
    }

    /**
     * @param resultSet
     * @return ${modelClassName}
     * @throw SQLException
     */
    @Override
    public ${modelClassName} getBean(ResultSet resultSet) throws SQLException {
        ${modelClassName} ${modelVariableName} = new ${modelClassName}();
    <c:forEach items="${columns}" var="column" varStatus="status">
    <c:choose>
        <c:when test="${util.equals(column.javaTypeName, 'int') || util.equals(column.javaTypeName, 'Integer') || util.equals(column.javaTypeName, 'java.lang.Integer')}">
        ${modelVariableName}.${column.methodSetter}(resultSet.getInt("${column.columnName}"));
        </c:when>
        <c:when test="${util.equals(column.javaTypeName, 'float') || util.equals(column.javaTypeName, 'Float') || util.equals(column.javaTypeName, 'java.lang.Float')}">
        ${modelVariableName}.${column.methodSetter}(resultSet.getFloat("${column.columnName}"));
        </c:when>
        <c:when test="${util.equals(column.javaTypeName, 'double') || util.equals(column.javaTypeName, 'Double') || util.equals(column.javaTypeName, 'java.lang.Double')}">
        ${modelVariableName}.${column.methodSetter}(resultSet.getDouble("${column.columnName}"));
        </c:when>
        <c:when test="${util.equals(column.javaTypeName, 'long') || util.equals(column.javaTypeName, 'Long') || util.equals(column.javaTypeName, 'java.lang.Long')}">
        ${modelVariableName}.${column.methodSetter}(resultSet.getLong("${column.columnName}"));
        </c:when>
        <c:when test="${util.equals(column.javaTypeName, 'String') || util.equals(column.javaTypeName, 'java.lang.String')}">
        ${modelVariableName}.${column.methodSetter}(resultSet.getString("${column.columnName}"));
        </c:when>
        <c:when test="${util.equals(column.javaTypeName, 'java.util.Date')}">
        ${modelVariableName}.${column.methodSetter}(resultSet.getTimestamp("${column.columnName}"));
        </c:when>
        <c:when test="${util.equals(column.javaTypeName, 'java.sql.Date')}">
        ${modelVariableName}.${column.methodSetter}(resultSet.getTimestamp("${column.columnName}"));
        </c:when>
        <c:when test="${util.equals(column.javaTypeName, 'java.sql.Timestamp')}">
        ${modelVariableName}.${column.methodSetter}(resultSet.getTimestamp("${column.columnName}"));
        </c:when>
        <c:otherwise>
        /* ERROR: Unknown DataType: ${column.columnName}: ${column.javaTypeName} */
        </c:otherwise>
    </c:choose>
    </c:forEach>
        return ${modelVariableName};
    }
<t:comment>
    /**
     * @param ${modelVariableName}
     * @return List<Object>
     */
    public List<Object> getInsertParameters(${modelClassName} ${modelVariableName}) {
        List<Object> parameters = new ArrayList<Object>();<c:forEach items="${columns}" var="column" varStatus="status">
        parameters.add(${modelVariableName}.${column.methodGetter}());</c:forEach>
        return parameters;
    }

    /**
     * @param ${modelVariableName}
     * @return List<Object>
     */
    public List<Object> getUpdateParameters(${modelClassName} ${modelVariableName}) {
        List<Object> parameters = new ArrayList<Object>();<c:forEach items="${columns}" var="column" varStatus="status">
        parameters.add(${modelVariableName}.${column.methodGetter}());</c:forEach><c:forEach items="${table.primaryKeys}" var="column" varStatus="status">
        parameters.add(${modelVariableName}.${column.methodGetter}());</c:forEach>
        return parameters;
    }

    /**
     * @param cityModel
     * @return String
     */
    public String toSqlString(${modelClassName} ${modelVariableName}) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ${tableName}(\r\n");<c:forEach items="${columns}" var="column" varStatus="status">
        sql.append("A.${column.columnName}<c:if test="${(status.index + 1) < columnsSize}">,</c:if>");</c:forEach>
        sql.append(") VALUES (");<c:forEach items="${columns}" var="column" varStatus="status">
        sql.append(quote(${modelVariableName}.${column.methodGetter}()))<c:if test="${(status.index + 1) < columnsSize}">.append(",")</c:if>;</c:forEach>
        return sql.toString();
    }
</t:comment>
}