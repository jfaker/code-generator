<c:set var="table" value="${table}"/>

<c:set var="name" value="${table.className}"/>
<c:set var="tableName" value="${table.tableName}"/>
<c:set var="tableCode" value="${table.tableCode}"/>
<c:set var="className" value="${table.className}"/>
<c:set var="variable" value="${table.variableName}"/>
<c:set var="variableName" value="${table.variableName}"/>
<c:set var="remarks" value="${table.remarks}"/>

<c:set var="modelClassName" value="${className}"/>
<c:set var="modelPackageName" value="${modelTemplate.parameters.packageName.value}"/>
<c:set var="modelVariableName" value="${variableName}"/>

<c:set var="daoClassName" value="${className}Dao"/>
<c:set var="daoPackageName" value="${daoTemplate.parameters.packageName.value}"/>
<c:set var="daoVariableName" value="${variable}Dao"/>

<c:set var="managerClassName" value="${className}Manager"/>
<c:set var="managerPackageName" value="${managerTemplate.parameters.packageName.value}"/>
<c:set var="managerVariableName" value="${variableName}Manager"/>

<c:set var="serviceClassName" value="${className}Service"/>
<c:set var="servicePackageName" value="${serviceTemplate.parameters.packageName.value}"/>
<c:set var="serviceVariableName" value="${variableName}Service"/>

<c:if test="${util.notNull(actionTemplate)}">
    <c:set var="actionClassName" value="${className}Action"/>
    <c:set var="actionPackageName" value="${actionTemplate.parameters.packageName.value}"/>
    <c:set var="actionVariableName" value="${variableName}Action"/>
</c:if>

<c:set var="primaryKeyColumnName" value="${primaryKeyColumnName}"/>
<c:set var="primaryKeyDataTypeName" value="${primaryKeyDataTypeName}"/>
<c:set var="primaryKeyJavaTypeName" value="${primaryKeyJavaTypeName}"/>
<c:set var="primaryKeyVariableName" value="${primaryKeyVariableName}"/>
<c:set var="primaryKeyParameterName" value="${primaryKeyParameterName}"/>
<c:set var="primaryKeyMethodSetter" value="${primaryKeyMethodSetter}"/>
<c:set var="primaryKeyMethodGetter" value="${primaryKeyMethodGetter}"/>

<c:choose>
    <c:when test="${primaryKeyJavaTypeName == 'boolean'}"><c:set var="primaryKeyJavaTypeWrapName" value="Boolean"/></c:when>
    <c:when test="${primaryKeyJavaTypeName == 'byte'}"><c:set var="primaryKeyJavaTypeWrapName" value="Byte"/></c:when>
    <c:when test="${primaryKeyJavaTypeName == 'short'}"><c:set var="primaryKeyJavaTypeWrapName" value="Short"/></c:when>
    <c:when test="${primaryKeyJavaTypeName == 'int'}"><c:set var="primaryKeyJavaTypeWrapName" value="Integer"/></c:when>
    <c:when test="${primaryKeyJavaTypeName == 'float'}"><c:set var="primaryKeyJavaTypeWrapName" value="Float"/></c:when>
    <c:when test="${primaryKeyJavaTypeName == 'double'}"><c:set var="primaryKeyJavaTypeWrapName" value="Double"/></c:when>
    <c:when test="${primaryKeyJavaTypeName == 'long'}"><c:set var="primaryKeyJavaTypeWrapName" value="Long"/></c:when>
    <c:otherwise><c:set var="primaryKeyJavaTypeWrapName" value="${primaryKeyJavaTypeName}"/></c:otherwise>
</c:choose>

<c:set var="columns" value="${table.listColumns()}"/>
<c:set var="columnsSize" value="${columns.size()}"/>
<c:set var="author" value="xuesong.net"/>