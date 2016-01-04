<t:include file="/include/common.jsp"/>
/*
 * $RCSfile: ${modelClassName}.java,v $$
 * $Revision: 1.1 $
 * $Date: ${timestamp} $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package ${modelPackageName};

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Title: ${modelClassName}</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author ${author}
 * @version 1.0
 */
public class ${modelClassName} implements Serializable
{
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

<c:forEach items="${columns}" var="column" varStatus="status">
<c:choose>
    <c:when test="${column.javaTypeName == 'java.util.Date'}"><c:set var="javaTypeName" value="Date"/></c:when>
    <c:when test="${column.javaTypeName == 'java.sql.Date'}"><c:set var="javaTypeName" value="Date"/></c:when>
    <c:when test="${column.javaTypeName == 'java.sql.Timestamp'}"><c:set var="javaTypeName" value="Date"/></c:when>
    <c:otherwise><c:set var="javaTypeName" value="${column.javaTypeName}"/></c:otherwise>
</c:choose>
    /**
     * @param ${column.variableName} the ${column.variableName} to set
     */
    public void ${column.methodSetter}(${javaTypeName} ${column.variableName})
    {
        this.${column.variableName} = ${column.variableName};
    }

    /**
     * @return the ${column.variableName}
     */
    public ${javaTypeName} ${column.getMethodGetter()}()
    {
        return this.${column.variableName};
    }</c:forEach>
}