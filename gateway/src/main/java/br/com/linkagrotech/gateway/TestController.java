package br.com.linkagrotech.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teste")
public class TestController {

  @GetMapping("/get")
  public String getTeste() {
    return "Acessou";
  }

}
