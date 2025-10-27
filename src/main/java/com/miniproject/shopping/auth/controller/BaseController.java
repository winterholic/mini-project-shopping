package com.miniproject.auth.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Tag(name = "기본 경로", description = "기본 경로용")
public class BaseController {

    @GetMapping
    public String getBaseEndPoint(){
        return "Auth Server is running at " + LocalDateTime.now().toString();
    }

    @PostMapping
    public String postBaseEndPoint(){
        return LocalDateTime.now().toString();
    }
}
