<t:include file="/include/common.jsp"/>
/*
 * $RCSfile: ${managerClassName}.java,v $$
 * $Revision: 1.1 $
 * $Date: ${timestamp} $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package ${managerPackageName};

import java.util.List;
import java.util.Map;

import ${modelPackageName}.${modelClassName};
import ${servicePackageName}.${serviceClassName};

/**
 * <p>Title: ${managerClassName}</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author ${author}
 * @version 1.0
 */
public class ${managerClassName} {
    private ${serviceClassName} ${serviceVariableName} = null;

    /**
     *
     */
    public ${managerClassName}() {
        this.${serviceVariableName} = new ${serviceClassName}();
    }

    /**
     * @param ${primaryKeyVariableName}
     * @return ${modelClassName}
     */
    public ${modelClassName} getById(${primaryKeyJavaTypeName} ${primaryKeyVariableName}) {
        return this.${serviceVariableName}.getById(${primaryKeyVariableName});
    }

    /**
     * @param ${modelVariableName}
     * @return int
     */
    public int create(${modelClassName} ${modelVariableName}) {
        return this.${serviceVariableName}.create(${modelVariableName});
    }

    /**
     * @param ${modelVariableName}
     * @return int
     */
    public int update(${modelClassName} ${modelVariableName}) {
        return this.${serviceVariableName}.update(${modelVariableName});
    }

    /**
     * @param ${primaryKeyVariableName}
     * @param params
     * @return int
     */
    public int update(${primaryKeyJavaTypeName} ${primaryKeyVariableName}, Map<String, Object> params) {
        return this.${serviceVariableName}.update(${primaryKeyVariableName}, params);
    }

    /**
     * @param pageNum
     * @param pageSize
     * @return List<${modelClassName}>
     */
    public List<${modelClassName}> list(int pageNum, int pageSize) {
        return this.${serviceVariableName}.list(pageNum, pageSize);
    }

    /**
     * @param ${primaryKeyVariableName}
     * @return int
     */
    public int delete(${primaryKeyJavaTypeName} ${primaryKeyVariableName}) {
        return this.${serviceVariableName}.delete(${primaryKeyVariableName});
    }
}