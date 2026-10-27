package com.riverex;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Starts the full Spring context (including PostgreSQL) to make sure the app can boot.
@SpringBootTest
class RiverExApplicationTests {

	// Passes if Spring can create all beans without throwing. Empty on purpose.
	@Test
	void contextLoads() {
	}

}
