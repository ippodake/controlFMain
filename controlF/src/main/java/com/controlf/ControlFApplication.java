package com.controlf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(excludeName = {
    "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration",
    "org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration",
    "org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration",
    "org.springframework.boot.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration"
})
public class ControlFApplication {
    public static void main(String[] args) {
        SpringApplication.run(ControlFApplication.class, args);
    }
}
