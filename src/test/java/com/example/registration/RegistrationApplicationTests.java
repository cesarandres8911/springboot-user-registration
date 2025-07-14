package com.example.registration;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(RegistrationApplicationTests.TestConfig.class)
class RegistrationApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("[DEBUG_LOG] Running contextLoads test");
	}

	@Configuration
	static class TestConfig {
		@Bean
		public AuthenticationManager authenticationManager() {
			return Mockito.mock(AuthenticationManager.class);
		}

		@Bean
		public AuthenticationProvider authenticationProvider() {
			return Mockito.mock(AuthenticationProvider.class);
		}
	}
}
