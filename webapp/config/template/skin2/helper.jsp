<t:include file="/include/common.jsp"/>
<c:forEach items="${columns}" var="column" varStatus="status">
    ${modelVariableName}.${column.methodSetter}(model.${column.methodGetter}());</c:forEach>