package br.com.vestfatec;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteController {

    @GetMapping("/teste")
    public String testeServidor() {
        return "Servidor rodando";
    }
}