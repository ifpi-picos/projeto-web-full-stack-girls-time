package com.teamg.BookBee;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookBeeApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class BookBeeApplicationTests {

	@Test
	void contextLoads() {
	}

}
