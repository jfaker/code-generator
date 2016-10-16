package com.skin.generator.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import com.skin.generator.database.Table;
import com.skin.generator.database.dialect.Dialect;
import com.skin.generator.database.dialect.MySQLDialect;
import com.skin.generator.database.sql.parser.CreateParser;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.annotation.UrlPattern;
import com.skin.j2ee.util.JsonUtil;

/**
 * @author weixian
 * @version 1.0
 */
public class CreateAction extends BaseAction {
    /**
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("/editor/create.html")
    public void list() throws IOException, ServletException {
        this.forward("/template/generator/data/create.jsp");
    }

    /**
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("/ajax/editor/createParse.html")
    public void parse() throws IOException, ServletException {
        String sql = this.getParameter("sql", "");
        Dialect dialect = new MySQLDialect();
        CreateParser createParser = new CreateParser(dialect);
        List<Table> tableList = createParser.parse(sql);

        if(tableList != null && tableList.size() > 0) {
        	Table table = tableList.get(0);
        	JsonUtil.success(this.request, this.response, table);
        }
    	JsonUtil.success(this.request, this.response, null);
    }
}
