package shop.mtcoding.blog;

import org.junit.jupiter.api.Test;
import shop.mtcoding.blog.core.utils.MyUtil;

import java.util.List;

public class MyUtilTest {

    @Test
    public void parse_test() {
        String data = """
                안녕
                         """;
        List<String> results = MyUtil.parseMultiline(data);
        System.out.println(results);
    }
}
