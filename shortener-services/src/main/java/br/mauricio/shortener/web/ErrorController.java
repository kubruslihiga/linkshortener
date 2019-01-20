package br.mauricio.shortener.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.mauricio.shortener.exception.ShortenerException;

@Controller
public class ErrorController {

	@GetMapping(path = "/error/invalidpage", produces = "text/html")
	public String invalidPage(Model model) throws ShortenerException {
		return "invalidpage";
	}
}
