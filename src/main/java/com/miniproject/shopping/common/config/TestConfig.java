package com.miniproject.auth.common.config;

import com.miniproject.auth.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TestConfig {
    private final UserRepository userRepository;
}
