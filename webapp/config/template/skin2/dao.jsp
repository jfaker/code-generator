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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ${modelPackageName}.${modelClassName};
import com.skin.j2ee.dao.DAOException;
import com.skin.j2ee.dao.Dao;

/**
 * <p>Title: ${daoClassName}</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author ${author}
 * @version 1.0
 */
public class ${daoClassName} extends Dao<${modelClassName}, ${primaryKeyJavaTypeWrapName}>
{
    private static final Logger logger = LoggerFactory.getLogger(${daoClassName}.class);

    public ${daoClassName}()
    {
    }

    /**
     * @param connection
     */
    public ${daoClassName}(Connection connection)
    {
        super(connection);
    }

    /**
     * @param id
     * @return ${modelClassName}
     * @throws SQLException
     */
    public ${modelClassName} getById(${primaryKeyJavaTypeName} id)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("select");<c:forEach items="${columns}" var="column" varStatus="status">
        sql.append(" A.${column.columnName}<c:if test="${(status.index + 1) < columnsSize}">,</c:if>");</c:forEach>
        sql.append(" FROM ${tableName} A");
        sql.append(" WHERE A.${primaryKeyColumnName}=?");

        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try
        {
            statement = this.getConnection().prepareStatement(sql.toString());
            <c:choose>
                <c:when test="${util.equals(primaryKeyJavaTypeName, 'int') || util.equals(primaryKeyJavaTypeName, 'Integer') || util.equals(primaryKeyJavaTypeName, 'java.lang.Integer')}">statement.setInt(1, id);</c:when>
                <c:when test="${util.equals(primaryKeyJavaTypeName, 'float') || util.equals(primaryKeyJavaTypeName, 'Float') || util.equals(primaryKeyJavaTypeName, 'java.lang.Float')}">statement.setFloat(1, id);</c:when>
                <c:when test="${util.equals(primaryKeyJavaTypeName, 'double') || util.equals(primaryKeyJavaTypeName, 'Double') || util.equals(primaryKeyJavaTypeName, 'java.lang.Double')}">statement.setDouble(1, id);</c:when>
                <c:when test="${util.equals(primaryKeyJavaTypeName, 'long') || util.equals(primaryKeyJavaTypeName, 'Long') || util.equals(primaryKeyJavaTypeName, 'java.lang.Long')}">statement.setLong(1, id);</c:when>
                <c:when test="${util.equals(primaryKeyJavaTypeName, 'String') || util.equals(primaryKeyJavaTypeName, 'java.lang.String')}">statement.setString(1, id);</c:when>
                <c:when test="${util.equals(primaryKeyJavaTypeName, 'java.util.Date')}">statement.setTimestamp(1, this.getTimestamp(id));</c:when>
                <c:when test="${util.equals(primaryKeyJavaTypeName, 'java.sql.Date')}">statement.setTimestamp(1, this.getTimestamp(id));</c:when>
                <c:when test="${util.equals(primaryKeyJavaTypeName, 'java.sql.Timestamp')}">statement.setTimestamp(1, id);</c:when>
                <c:otherwise>/* ERROR: Unknown DataType: ${column.columnName}: ${column.javaTypeName} */</c:otherwise>
            </c:choose>
            resultSet = statement.executeQuery();

            if(resultSet.next()) 
            {
                ${modelClassName} ${modelVariableName} = new ${modelClassName}();<c:forEach items="${columns}" var="column" varStatus="status">
                <c:choose>
                    <c:when test="${util.equals(column.javaTypeName, 'int') || util.equals(column.javaTypeName, 'Integer') || util.equals(column.javaTypeName, 'java.lang.Integer')}">${modelVariableName}.${column.methodSetter}(resultSet.getInt("${column.columnName}"));</c:when>
                    <c:when test="${util.equals(column.javaTypeName, 'float') || util.equals(column.javaTypeName, 'Float') || util.equals(column.javaTypeName, 'java.lang.Float')}">${modelVariableName}.${column.methodSetter}(resultSet.getFloat("${column.columnName}"));</c:when>
                    <c:when test="${util.equals(column.javaTypeName, 'double') || util.equals(column.javaTypeName, 'Double') || util.equals(column.javaTypeName, 'java.lang.Double')}">${modelVariableName}.${column.methodSetter}(resultSet.getDouble("${column.columnName}"));</c:when>
                    <c:when test="${util.equals(column.javaTypeName, 'long') || util.equals(column.javaTypeName, 'Long') || util.equals(column.javaTypeName, 'java.lang.Long')}">${modelVariableName}.${column.methodSetter}(resultSet.getLong("${column.columnName}"));</c:when>
                    <c:when test="${util.equals(column.javaTypeName, 'String') || util.equals(column.javaTypeName, 'java.lang.String')}">${modelVariableName}.${column.methodSetter}(resultSet.getString("${column.columnName}"));</c:when>
                    <c:when test="${util.equals(column.javaTypeName, 'java.util.Date')}">${modelVariableName}.${column.methodSetter}(resultSet.getTimestamp("${column.columnName}"));</c:when>
                    <c:when test="${util.equals(column.javaTypeName, 'java.sql.Date')}">${modelVariableName}.${column.methodSetter}(resultSet.getTimestamp("${column.columnName}"));</c:when>
                    <c:when test="${util.equals(column.javaTypeName, 'java.sql.Timestamp')}">${modelVariableName}.${column.methodSetter}(resultSet.getTimestamp("${column.columnName}"));</c:when>
                    <c:otherwise>/* ERROR: Unknown DataType: ${column.columnName}: ${column.javaTypeName} */</c:otherwise>
                </c:choose></c:forEach>
                return ${modelVariableName};
            }
        }
        catch(SQLException e)
        {
            throw new DAOException(e);
        }
        finally
        {
            this.close(resultSet);
            this.close(statement);
        }

        return null;
    }

    /**
     * @param ${modelVariableName}
     * @throws SQLException
     */
    public int create(${modelClassName} ${modelVariableName})
    {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ${tableName}(");<c:forEach items="${columns}" var="column" varStatus="status"><c:if test="${column.autoIncrement == 0}">
        sql.append(" ${column.columnName}<c:if test="${(status.index + 1) < columnsSize}">,</c:if>");</c:if></c:forEach>
        sql.append(") VALUES(<c:forEach items="${columns}" var="column" varStatus="status"><c:if test="${column.autoIncrement == 0}">?<c:if test="${(status.index + 1) < columnsSize}">, </c:if></c:if></c:forEach>)");

        if(logger.isDebugEnabled())
        {
            logger.debug(sql.toString());
        }

        PreparedStatement statement = null;

        try
        {
            <c:set var="index" value="1"/>
            statement = this.getConnection().prepareStatement(sql.toString());<c:forEach items="${columns}" var="column" varStatus="status"><c:if test="${column.autoIncrement == 0}">
            <c:choose>
                <c:when test="${util.equals(column.javaTypeName, 'int') || util.equals(column.javaTypeName, 'Integer') || util.equals(column.javaTypeName, 'java.lang.Integer')}">statement.setInt(${index}, ${modelVariableName}.${column.methodGetter}());</c:when>
                <c:when test="${util.equals(column.javaTypeName, 'float') || util.equals(column.javaTypeName, 'Float') || util.equals(column.javaTypeName, 'java.lang.Float')}">statement.setFloat(${index}, ${modelVariableName}.${column.methodGetter}());</c:when>
                <c:when test="${util.equals(column.javaTypeName, 'double') || util.equals(column.javaTypeName, 'Double') || util.equals(column.javaTypeName, 'java.lang.Double')}">statement.setDouble(${index}, ${modelVariableName}.${column.methodGetter}());</c:when>
                <c:when test="${util.equals(column.javaTypeName, 'long') || util.equals(column.javaTypeName, 'Long') || util.equals(column.javaTypeName, 'java.lang.Long')}">statement.setLong(${index}, ${modelVariableName}.${column.methodGetter}());</c:when>
                <c:when test="${util.equals(column.javaTypeName, 'String') || util.equals(column.javaTypeName, 'java.lang.String')}">statement.setString(${index}, ${modelVariableName}.${column.methodGetter}());</c:when>
                <c:when test="${util.equals(column.javaTypeName, 'java.util.Date')}">statement.setTimestamp(${index}, this.getTimestamp(${modelVariableName}.${column.methodGetter}()));</c:when>
                <c:when test="${util.equals(column.javaTypeName, 'java.sql.Date')}">statement.setTimestamp(${index}, this.getTimestamp(${modelVariableName}.${column.methodGetter}()));</c:when>
                <c:when test="${util.equals(column.javaTypeName, 'java.sql.Timestamp')}">statement.setTimestamp(${index}, ${modelVariableName}.${column.methodGetter}());</c:when>
                <c:otherwise>/* ERROR: Unknown DataType: ${column.columnName}: ${column.javaTypeName} */</c:otherwise>
            </c:choose><c:set var="index" value="${index + 1}"/></c:if></c:forEach>
            return statement.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new DAOException(e);
        }
        finally
        {
            this.close(statement);
        }
    }

    /**
     * @param ${modelVariableName}
     * @return int
     * @throws SQLException
     */
    public int update(${modelClassName} ${modelVariableName})
    {
        StringBuilder sql = new StringBuilder();
        sql.append("update ${tableName} set");<c:forEach items="${columns}" var="column" varStatus="status">
        sql.append(" ${column.columnName}=?<c:if test="${(status.index + 1) < columnsSize}">,</c:if>");</c:forEach>
        sql.append(" WHERE ${primaryKeyColumnName}=?");

        if(logger.isDebugEnabled())
        {
            logger.debug(sql.toString());
        }

        PreparedStatement statement = null;

        try
        {
            statement = this.getConnection().prepareStatement(sql.toString());<c:forEach items="${columns}" var="column" varStatus="status">
            <c:choose>
                <c:when test="${util.equals(column.javaTypeName, 'int') || util.equals(column.javaTypeName, 'Integer') || util.equals(column.javaTypeName, 'java.lang.Integer')}">statement.setInt(${status.index + 1}, ${modelVariableName}.${column.methodGetter}());</c:when>
                <c:when test="${util.equals(column.javaTypeName, 'float') || util.equals(column.javaTypeName, 'Float') || util.equals(column.javaTypeName, 'java.lang.Float')}">statement.setFloat(${status.index + 1}, ${modelVariableName}.${column.methodGetter}());</c:when>
                <c:when test="${util.equals(column.javaTypeName, 'double') || util.equals(column.javaTypeName, 'Double') || util.equals(column.javaTypeName, 'java.lang.Double')}">statement.setDouble(${status.index + 1}, ${modelVariableName}.${column.methodGetter}());</c:when>
                <c:when test="${util.equals(column.javaTypeName, 'long') || util.equals(column.javaTypeName, 'Long') || util.equals(column.javaTypeName, 'java.lang.Long')}">statement.setLong(${status.index + 1}, ${modelVariableName}.${column.methodGetter}());</c:when>
                <c:when test="${util.equals(column.javaTypeName, 'String') || util.equals(column.javaTypeName, 'java.lang.String')}">statement.setString(${status.index + 1}, ${modelVariableName}.${column.methodGetter}());</c:when>
                <c:when test="${util.equals(column.javaTypeName, 'java.util.Date')}">statement.setTimestamp(${status.index + 1}, this.getTimestamp(${modelVariableName}.${column.methodGetter}()));</c:when>
                <c:when test="${util.equals(column.javaTypeName, 'java.sql.Date')}">statement.setTimestamp(${status.index + 1}, this.getTimestamp(${modelVariableName}.${column.methodGetter}()));</c:when>
                <c:when test="${util.equals(column.javaTypeName, 'java.sql.Timestamp')}">statement.setTimestamp(${status.index + 1}, ${modelVariableName}.${column.methodGetter}());</c:when>
                <c:otherwise>/* ERROR: Unknown DataType: ${column.columnName}: ${column.javaTypeName} */</c:otherwise>
            </c:choose></c:forEach><c:forEach items="${table.primaryKeys}" var="column" varStatus="status">
            <c:choose>
                <c:when test="${util.equals(column.javaTypeName, 'int') || util.equals(column.javaTypeName, 'Integer') || util.equals(column.javaTypeName, 'java.lang.Integer')}">statement.setInt(${status.index + 1}, ${modelVariableName}.${column.methodGetter}());</c:when>
                <c:when test="${util.equals(column.javaTypeName, 'float') || util.equals(column.javaTypeName, 'Float') || util.equals(column.javaTypeName, 'java.lang.Float')}">statement.setFloat(${status.index + 1}, ${modelVariableName}.${column.methodGetter}());</c:when>
                <c:when test="${util.equals(column.javaTypeName, 'double') || util.equals(column.javaTypeName, 'Double') || util.equals(column.javaTypeName, 'java.lang.Double')}">statement.setDouble(${status.index + 1}, ${modelVariableName}.${column.methodGetter}());</c:when>
                <c:when test="${util.equals(column.javaTypeName, 'long') || util.equals(column.javaTypeName, 'Long') || util.equals(column.javaTypeName, 'java.lang.Long')}">statement.setLong(${status.index + 1}, ${modelVariableName}.${column.methodGetter}());</c:when>
                <c:when test="${util.equals(column.javaTypeName, 'String') || util.equals(column.javaTypeName, 'java.lang.String')}">statement.setString(${status.index + 1}, ${modelVariableName}.${column.methodGetter}());</c:when>
                <c:when test="${util.equals(column.javaTypeName, 'java.util.Date')}">statement.setTimestamp(${status.index + 1}, this.getTimestamp(${modelVariableName}.${column.methodGetter}()));</c:when>
                <c:when test="${util.equals(column.javaTypeName, 'java.sql.Date')}">statement.setTimestamp(${status.index + 1}, this.getTimestamp(${modelVariableName}.${column.methodGetter}()));</c:when>
                <c:when test="${util.equals(column.javaTypeName, 'java.sql.Timestamp')}">statement.setTimestamp(${status.index + 1}, ${modelVariableName}.${column.methodGetter}());</c:when>
                <c:otherwise>/* ERROR: Unknown DataType: ${column.columnName}: ${column.javaTypeName} */</c:otherwise>
            </c:choose></c:forEach>

            return statement.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new DAOException(e);
        }
        finally
        {
            this.close(statement);
        }
    }

    /**
     * @param pageNum
     * @param pageSize
     * @return List<${modelClassName}>
     * @throws SQLException
     */
    public List<${modelClassName}> list(int pageNum, int pageSize)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("select");<c:forEach items="${columns}" var="column" varStatus="status">
        sql.append(" a.${column.columnName}<c:if test="${(status.index + 1) < columnsSize}">,</c:if>");</c:forEach>
        sql.append(" from ${tableName} a");

        if(logger.isDebugEnabled())
        {
            logger.debug(sql.toString());
        }

        ResultSet resultSet = null;
        PreparedStatement statement = null;
        List<${modelClassName}> list = new ArrayList<${modelClassName}>();

        try
        {
            int count = this.count(sql.toString());
            statement = this.getConnection().prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery();
            this.absolute(resultSet, pageNum, pageSize, count);

            for(int i = 0; resultSet.next() && i < pageSize; i++)
            {
                ${modelClassName} ${modelVariableName} = new ${modelClassName}();<c:forEach items="${columns}" var="column" varStatus="status">
                <c:choose>
                    <c:when test="${util.equals(column.javaTypeName, 'int') || util.equals(column.javaTypeName, 'Integer') || util.equals(column.javaTypeName, 'java.lang.Integer')}">${modelVariableName}.${column.methodSetter}(resultSet.getInt("${column.columnName}"));</c:when>
                    <c:when test="${util.equals(column.javaTypeName, 'float') || util.equals(column.javaTypeName, 'Float') || util.equals(column.javaTypeName, 'java.lang.Float')}">${modelVariableName}.${column.methodSetter}(resultSet.getFloat("${column.columnName}"));</c:when>
                    <c:when test="${util.equals(column.javaTypeName, 'double') || util.equals(column.javaTypeName, 'Double') || util.equals(column.javaTypeName, 'java.lang.Double')}">${modelVariableName}.${column.methodSetter}(resultSet.getDouble("${column.columnName}"));</c:when>
                    <c:when test="${util.equals(column.javaTypeName, 'long') || util.equals(column.javaTypeName, 'Long') || util.equals(column.javaTypeName, 'java.lang.Long')}">${modelVariableName}.${column.methodSetter}(resultSet.getLong("${column.columnName}"));</c:when>
                    <c:when test="${util.equals(column.javaTypeName, 'String') || util.equals(column.javaTypeName, 'java.lang.String')}">${modelVariableName}.${column.methodSetter}(resultSet.getString("${column.columnName}"));</c:when>
                    <c:when test="${util.equals(column.javaTypeName, 'java.util.Date')}">${modelVariableName}.${column.methodSetter}(resultSet.getTimestamp("${column.columnName}"));</c:when>
                    <c:when test="${util.equals(column.javaTypeName, 'java.sql.Date')}">${modelVariableName}.${column.methodSetter}(resultSet.getTimestamp("${column.columnName}"));</c:when>
                    <c:when test="${util.equals(column.javaTypeName, 'java.sql.Timestamp')}">${modelVariableName}.${column.methodSetter}(resultSet.getTimestamp("${column.columnName}"));</c:when>
                    <c:otherwise>/* ERROR: Unknown DataType: ${column.columnName}: ${column.javaTypeName} */</c:otherwise>
                </c:choose></c:forEach>
                list.add(${modelVariableName});
            }
        }
        catch(SQLException e)
        {
            throw new DAOException(e);
        }
        finally
        {
            this.close(resultSet);
            this.close(statement);
        }

        return list;
    }
<c:comment>
    /**
     * @return List<${modelClassName}>
     * @throws SQLException
     */
    public List<${modelClassName}> list(int size)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("select");<c:forEach items="${columns}" var="column" varStatus="status">
        sql.append(" A.${column.columnName}<c:if test="${(status.index + 1) < columnsSize}">,</c:if>");</c:forEach>
        sql.append(" FROM ${tableName} A");

        if(logger.isDebugEnabled())
        {
            logger.debug(sql.toString());
        }

        ResultSet resultSet = null;
        PreparedStatement statement = null;
        List<${modelClassName}> list = new ArrayList<${modelClassName}>();

        try
        {
            statement = this.getConnection().prepareStatement(sql.toString());
            resultSet = statement.executeQuery();

            while(resultSet.next())
            {
                ${modelClassName} ${modelVariableName} = new ${modelClassName}();<c:forEach items="${columns}" var="column" varStatus="status">
                <c:choose>
                    <c:when test="${util.equals(column.javaTypeName, 'int') || util.equals(column.javaTypeName, 'Integer') || util.equals(column.javaTypeName, 'java.lang.Integer')}">${modelVariableName}.${column.methodSetter}(resultSet.getInt("${column.columnName}"));</c:when>
                    <c:when test="${util.equals(column.javaTypeName, 'float') || util.equals(column.javaTypeName, 'Float') || util.equals(column.javaTypeName, 'java.lang.Float')}">${modelVariableName}.${column.methodSetter}(resultSet.getFloat("${column.columnName}"));</c:when>
                    <c:when test="${util.equals(column.javaTypeName, 'double') || util.equals(column.javaTypeName, 'Double') || util.equals(column.javaTypeName, 'java.lang.Double')}">${modelVariableName}.${column.methodSetter}(resultSet.getDouble("${column.columnName}"));</c:when>
                    <c:when test="${util.equals(column.javaTypeName, 'long') || util.equals(column.javaTypeName, 'Long') || util.equals(column.javaTypeName, 'java.lang.Long')}">${modelVariableName}.${column.methodSetter}(resultSet.getLong("${column.columnName}"));</c:when>
                    <c:when test="${util.equals(column.javaTypeName, 'String') || util.equals(column.javaTypeName, 'java.lang.String')}">${modelVariableName}.${column.methodSetter}(resultSet.getString("${column.columnName}"));</c:when>
                    <c:when test="${util.equals(column.javaTypeName, 'java.util.Date')}">${modelVariableName}.${column.methodSetter}(resultSet.getTimestamp("${column.columnName}"));</c:when>
                    <c:when test="${util.equals(column.javaTypeName, 'java.sql.Date')}">${modelVariableName}.${column.methodSetter}(resultSet.getTimestamp("${column.columnName}"));</c:when>
                    <c:when test="${util.equals(column.javaTypeName, 'java.sql.Timestamp')}">${modelVariableName}.${column.methodSetter}(resultSet.getTimestamp("${column.columnName}"));</c:when>
                    <c:otherwise>/* ERROR: Unknown DataType: ${column.columnName}: ${column.javaTypeName} */</c:otherwise>
                </c:choose></c:forEach>
                list.add(${modelVariableName});

                if(list.size() >= size)
                {
                    break;
                }
            }
        }
        catch(SQLException e)
        {
            throw new DAOException(e);
        }
        finally
        {
            this.close(resultSet);
            this.close(statement);
        }

        return list;
    }
</c:comment>
    /**
     * @param ${primaryKeyVariableName}
     * @return int
     */
    public int delete(${primaryKeyJavaTypeName} ${primaryKeyVariableName})
    {
        if(${primaryKeyVariableName} > 0L)
        {
            try
            {
                return this.delete("DELETE FROM ${tableName} WHERE ${primaryKeyColumnName}=?", ${primaryKeyVariableName});
            }
            catch(SQLException e)
            {
                throw new DAOException(e);
            }
        }
        return 0;
    }
<c:comment>
    /**
     * @param ${modelVariableName}
     * @return List<Object>
     */
    public List<Object> getInsertParameters(${modelClassName} ${modelVariableName})
    {
        List<Object> parameters = new ArrayList<Object>();<c:forEach items="${columns}" var="column" varStatus="status">
        parameters.add(${modelVariableName}.${column.methodGetter}());</c:forEach>
        return parameters;
    }

    /**
     * @param ${modelVariableName}
     * @return List<Object>
     */
    public List<Object> getUpdateParameters(${modelClassName} ${modelVariableName})
    {
        List<Object> parameters = new ArrayList<Object>();<c:forEach items="${columns}" var="column" varStatus="status">
        parameters.add(${modelVariableName}.${column.methodGetter}());</c:forEach><c:forEach items="${table.primaryKeys}" var="column" varStatus="status">
        parameters.add(${modelVariableName}.${column.methodGetter}());</c:forEach>
        return parameters;
    }

    /**
     * @param cityModel
     * @return String
     */
    public String toSqlString(${modelClassName} ${modelVariableName})
    {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ${tableName}(\r\n");<c:forEach items="${columns}" var="column" varStatus="status">
        sql.append("A.${column.columnName}<c:if test="${(status.index + 1) < columnsSize}">,</c:if>");</c:forEach>
        sql.append(") VALUES (");<c:forEach items="${columns}" var="column" varStatus="status">
        sql.append(quote(${modelVariableName}.${column.methodGetter}()))<c:if test="${(status.index + 1) < columnsSize}">.append(",")</c:if>;</c:forEach>
        return sql.toString();
    }
</c:comment>
}