package test.com.skin.generator;

import java.io.File;
import java.util.List;

import com.skin.generator.database.Table;
import com.skin.generator.database.dialect.MySQLDialect;
import com.skin.generator.database.sql.parser.CreateParser;
import com.skin.io.StringStream;
import com.skin.util.IO;

/**
 * @author weixian
 * @version 1.0
 */
public class CreateParserTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            CreateParser parser = new CreateParser(new MySQLDialect());
            String source = IO.read(new File("D:\\workspace\\degopen\\table.sql"), "utf-8");
            List<Table> list = parser.parse(source);
            System.out.println(list);

            StringStream stream = new StringStream("-- -- 应用表\r\ncreate table tv_app(");
            String token = parser.getToken(stream);
            System.out.println("token: " + token);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
