<t:include file="/include/common.jsp"/>
/*
 * $RCSfile: ${serviceClassName}.java,v $$
 * $Revision: 1.1 $
 * $Date: ${timestamp} $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package ${servicePackageName};

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import ${daoPackageName}.${daoClassName};
import ${modelPackageName}.${modelClassName};
import com.skin.datasource.ConnectionManager;

/**
 * <p>Title: ${servicePackageName}</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author ${author}
 * @version 1.0
 */
public class ${serviceClassName} {
    /**
     * 
     */
    public ${serviceClassName}() {
    }

    /**
     * @param ${primaryKeyVariableName}
     * @return ${modelClassName}
     */
    public ${modelClassName} getById(${primaryKeyJavaTypeName} ${primaryKeyVariableName}) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            ${daoClassName} ${daoVariableName} = new ${daoClassName}(connection);
            return ${daoVariableName}.getById(${primaryKeyVariableName});
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param ${modelVariableName}
     * @return int
     */
    public int create(${modelClassName} ${modelVariableName}) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            ${daoClassName} ${daoVariableName} = new ${daoClassName}(connection);
            return ${daoVariableName}.create(${modelVariableName});
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param ${modelVariableName}
     * @return int
     */
    public int update(${modelClassName} ${modelVariableName}) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            ${daoClassName} ${daoVariableName} = new ${daoClassName}(connection);
            return ${daoVariableName}.update(${modelVariableName});
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param ${primaryKeyVariableName}
     * @param params
     * @return int
     */
    public int update(${primaryKeyJavaTypeName} ${primaryKeyVariableName}, Map<String, Object> params) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            ${daoClassName} ${daoVariableName} = new ${daoClassName}(connection);
            return ${daoVariableName}.update(${primaryKeyVariableName}, params);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param pageNum
     * @param pageSize
     * @return List<${modelClassName}>
     */
    public List<${modelClassName}> list(int pageNum, int pageSize) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            ${daoClassName} ${daoVariableName} = new ${daoClassName}(connection);
            return ${daoVariableName}.list(pageNum, pageSize);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param ${primaryKeyVariableName}
     * @return int
     */
    public int delete(${primaryKeyJavaTypeName} ${primaryKeyVariableName}) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            ${daoClassName} ${daoVariableName} = new ${daoClassName}(connection);
            return ${daoVariableName}.delete(${primaryKeyVariableName});
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }
}