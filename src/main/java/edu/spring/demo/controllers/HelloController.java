package edu.spring.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class HelloController {
	@GetMapping("")
	@ResponseBody
	public String helloAction() {
		return "Hello world!";
	}
	
	@GetMapping(path={"msg/{c}","msg/{c}/"})
	@ResponseBody
	public String messageAction(@PathVariable("c") String content) {
		return content;
	}
	
	@GetMapping(path={"msg/view/{c}","msg/view/{c}/"})
	public String messageViewAction(ModelMap data, @PathVariable("c") String content) {
		int value=220;
		data.addAttribute("value", value);
		return "helloView";
	}
	@GetMapping("msg/view/2/{c}")
	public ModelAndView messageViewAction2(@PathVariable("c") String content) {
		ModelAndView mv=new ModelAndView("helloView");
		mv.addObject("value", 220);
		mv.addObject("html","<h2>Test de html</h2>");
		return mv;
	}
}
