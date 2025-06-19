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

    @Test
    public void sum_test() {
        double result = MyUtil.scaleTo100(15.0, 15.0);
        System.out.println(result);
    }
}
