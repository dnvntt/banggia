package priceboard.rest.controller;

import org.jboss.logging.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class PingController {
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public @ResponseBody String checkPing(@RequestParam("ping") String jqueryCallback) {
		return jqueryCallback+"({})";
	}
}
