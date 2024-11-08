package com.comedica.apihello.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path="/api")
@CrossOrigin(origins="*")
public class HelloController {

    @GetMapping("hello")
 	public ResponseEntity<?> getHello(){


        return ResponseEntity.ok("hello");


    }

}
