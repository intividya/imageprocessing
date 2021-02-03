package com.example.actuatorservice;

import java.util.concurrent.atomic.AtomicLong;

import javax.websocket.server.PathParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ImageController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/mandelbrot/{min_c_re}/{min_c_im}/{max_c_re}/{max_c_im}/{x}/{y}/{inf_n}")
	@ResponseBody
	public String sayHello(@PathParam("min_c_re") String minCRe, @PathParam("min_c_im") String minCIm, @PathParam("max_c_re") String maxCre, @PathParam("max_c_im") String maxCim, @PathParam("x") int x, @PathParam("y") int y, @PathParam("inf_n") String infn) {
		GrayscalePicture picture = new GrayscalePicture(x, y);
		picture.save("target/abc.png");
		return "OK";
	}

}
