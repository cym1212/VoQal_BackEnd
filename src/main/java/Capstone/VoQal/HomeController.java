package Capstone.VoQal;

import Capstone.VoQal.domain.challenge.service.KeywordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Tag(name = "Test", description = "Test API")
@Controller
@AllArgsConstructor
public class HomeController {



    @GetMapping("/")
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
