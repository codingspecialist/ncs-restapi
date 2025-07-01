package shop.mtcoding.blog.webv2;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class IndexController {

    @GetMapping("/health")
    public @ResponseBody String health() {
        return "ok";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/proxy/github")
    public ResponseEntity<byte[]> proxyGithub(@RequestParam String url) {
        RestTemplate rt = new RestTemplate();
        byte[] raw = rt.getForObject(url, byte[].class);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8")
                .body(raw);
    }
}
