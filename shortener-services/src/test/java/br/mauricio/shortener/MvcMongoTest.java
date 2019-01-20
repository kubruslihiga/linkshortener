package br.mauricio.shortener;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = { ShortenerApplication.class })
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class MvcMongoTest {

	@Autowired
	private MockMvc mock;

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Test
	public void test() throws Exception {
		List<String> readAllLines = Files.readAllLines(Paths.get("src/test/resources/json/post.txt"));
		Set<String> result = new HashSet<>();
		for (String line : readAllLines) {
			JsonNode requestJson = OBJECT_MAPPER.readTree(line);
			JsonNode urlCompleteNode = requestJson.get("urlComplete");
			String urlComplete = urlCompleteNode.asText();
			MvcResult mvcResult = mock.perform(post("/post").contentType(MediaType.APPLICATION_JSON).content(line)).andExpect(status().isOk()).andReturn();
			JsonNode jsonNode = OBJECT_MAPPER.readTree(mvcResult.getResponse().getContentAsByteArray());
			JsonNode shortUrlNode = jsonNode.get("shortUrl");
			String shortUrl = shortUrlNode.asText();
			if (!result.add(shortUrl)) {
				Assert.fail("Short link is repeated. It cannot happen.");
			}
			String id = shortUrl.substring(shortUrl.lastIndexOf("/") + 1);
			MvcResult mvcGetResult = mock.perform(get("/" + id)).andReturn();
			String redirectedUrl = mvcGetResult.getResponse().getRedirectedUrl();
			Assert.assertEquals(urlComplete, redirectedUrl);
		}
	}
}