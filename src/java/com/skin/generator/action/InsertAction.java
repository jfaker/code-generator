package com.skin.generator.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import com.skin.generator.database.Entry;
import com.skin.generator.database.Record;
import com.skin.generator.database.sql.parser.InsertParser;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.annotation.UrlPattern;
import com.skin.j2ee.util.JsonUtil;

/**
 * @author weixian
 * @version 1.0
 */
public class InsertAction extends BaseAction {
    /**
     * @param args
     */
	public static void main(String[] args) {
		String sql = "insert into bd_app(id, app_str_id, name, company_id, profit_rate, alipay_account_id, alipay_merchant_id, sec_key, app_exe_signature_str, properties, options, gmt_create, gmt_modified, last_operator, operation_type, feature_option) values(3, '21720823', 'BD-SDK-TEST', 1, '0.7', '2088011177543483', '2088011177543483', '1539fad7784494515ff180c4432fdb24', '', '', 3, '2015-03-19 14:54:00', '2015-03-19 14:54:00', '', 1, 31);";
        InsertParser insertParser = new InsertParser();

        try {
			List<Record> recordList = insertParser.parse(sql);

			for(Record record : recordList) {
				List<Entry> entryList = record.getColumns();
				System.out.println(entryList.size());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    /**
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("/editor/insert.html")
    public void list() throws IOException, ServletException {
        this.forward("/template/generator/data/insert.jsp");
    }

    /**
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("/ajax/editor/insertParse.html")
    public void parse() throws IOException, ServletException {
        String sql = this.getParameter("sql", "");
        InsertParser insertParser = new InsertParser();
        List<Record> recordList = insertParser.parse(sql);

        if(recordList != null && recordList.size() > 0) {
        	Record record = recordList.get(0);
        	JsonUtil.success(this.request, this.response, record);
        }
    	JsonUtil.success(this.request, this.response, null);
    }
}
