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

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ${modelPackageName}.${modelClassName};
import ${daoPackageName}.${daoClassName};
import com.skin.cache.Cache;
import com.skin.cache.CacheFactory;
import com.skin.cache.Key;

/**
 * <p>Title: ${managerClassName}</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author ${author}
 * @version 1.0
 */
public class ${managerClassName}
{
    private Cache cache = null;
    private ${daoClassName} ${daoVariableName} = null;
    public static final String CACHE_PREFIX = "${variable}";
    public static final int EXPIRES = 600;

    /**
     * @param connection
     */
    public ${managerClassName}(Connection connection)
    {
        this.${daoVariableName} = new ${daoClassName}(connection);
        this.cache = CacheFactory.getCache();
    }

    /**
     * @param ${primaryKeyVariableName}
     * @return ${modelClassName}
     */
    public ${modelClassName} getById(${primaryKeyJavaTypeName} ${primaryKeyVariableName})
    {
        String key = Key.build(CACHE_PREFIX, ${primaryKeyVariableName});
        ${modelClassName} ${modelVariableName} = (${modelClassName})(this.cache.getCache(key));

        if(${modelVariableName} == null)
        {
            ${modelVariableName} = this.get${daoClassName}().getById(${primaryKeyVariableName});

            if(${modelVariableName} != null)
            {
                this.cache.setCache(key, EXPIRES, ${modelVariableName});
            }
        }

        return ${modelVariableName};
    }

    /**
     * @param ${modelVariableName}
     * @return int
     */
    public int create(${modelClassName} ${modelVariableName})
    {
        return this.get${daoClassName}().create(${modelVariableName});
    }

    /**
     * @param ${modelVariableName}
     * @return int
     */
    public int update(${modelClassName} ${modelVariableName})
    {
        return this.get${daoClassName}().update(${modelVariableName});
    }

    /**
     * @param pageNum
     * @param pageSize
     * @return List<${modelClassName}>
     * @throws SQLException
     */
    public List<${modelClassName}> list(int pageNum, int pageSize)
    {
        return this.get${daoClassName}().list(pageNum, pageSize);
    }

    /**
     * @param ${primaryKeyVariableName}
     * @return int
     */
    public int delete(${primaryKeyJavaTypeName} ${primaryKeyVariableName})
    {
        return this.get${daoClassName}().delete(${primaryKeyVariableName});
    }

    /**
     * @param id
     * @return String
     */
    public String getCacheKey(${primaryKeyJavaTypeName} id)
    {
        return Key.build(CACHE_PREFIX, id);
    }

    /**
     * @return the cache
     */
    public Cache getCache()
    {
        return this.cache;
    }

    /**
     * @param cache the cache to set
     */
    public void setCache(Cache cache)
    {
        this.cache = cache;
    }

    /**
     * @return the ${daoVariableName}
     */
    public ${daoClassName} get${daoClassName}()
    {
        return this.${daoVariableName};
    }

    /**
     * @param ${daoVariableName} the ${daoVariableName} to set
     */
    public void set${daoClassName}(${daoClassName} ${daoVariableName})
    {
        this.${daoVariableName} = ${daoVariableName};
    }
}