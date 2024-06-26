package Capstone.VoQal;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Tag(name = "Test", description = "Test API")
@Controller
@AllArgsConstructor
public class HomeController {

    @GetMapping("/test")
    @ResponseBody
    public String getRoot() {
        return "Hello World";
    }

    @GetMapping("/secured/home")
    @ResponseBody
    public String getSecuredHome() {
        return "Here is secured home";
    }
}
