package com.viesonet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.service.ProductsService;

@CrossOrigin("*")
@RestController
public class PostProductController {

    @Autowired
    ProductsService productsService;

   @GetMapping("staff/postsproduct")
   public ResponseEntity<Page<Object>> postsProduct(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "9") int size) {
		Page<Object> result = productsService.findPostsProductWithProcessing(page, size);
		return ResponseEntity.ok(result);
	}
    


}
