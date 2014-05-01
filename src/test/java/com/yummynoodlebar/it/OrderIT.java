/**
 *
 */
package com.yummynoodlebar.it;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.mdkt.datawiz.DataWizEngine;
import org.mdkt.datawiz.annotation.TadahDataSpec;
import org.mdkt.datawiz.annotation.TadahDataSpecList;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.client.RestTemplate;

import com.yummynoodlebar.rest.domain.Order;

/**
 * @author trung
 *
 */
public class OrderIT {

	@Rule
	public TestName testName = new TestName();

	@Before
	public void before() {
		DataWizEngine.instance().prepare(this.getClass(), testName.getMethodName());
	}

	@TadahDataSpecList(value = {
	   @TadahDataSpec(value="User <span tadah:var='auth'>letsnosh:noshing</span> logged in", handler=OrderDataWizHandler.class),
	   @TadahDataSpec(value="Select item <span tadah:var='order1'>yummy1</span> with quantity <span tadah:var='quantity1' tadah:type='java.lang.Integer'>12</span>", handler=OrderDataWizHandler.class),
	   @TadahDataSpec(value="Select item <span tadah:var='order2'>yummy15</span> with quantity <span tadah:var='quantity2' tadah:type='java.lang.Integer'>42</span>", handler=OrderDataWizHandler.class)
	})
	@Test
	public void authenticatedUserCreateOrders() {
		String auth = (String) DataWizEngine.instance().getContextValue("auth");
		Map<String, Integer> expectedOrder = new HashMap<>();
		expectedOrder.put((String)DataWizEngine.instance().getContextValue("order1"), (Integer)DataWizEngine.instance().getContextValue("quantity1"));
		expectedOrder.put((String)DataWizEngine.instance().getContextValue("order2"), (Integer)DataWizEngine.instance().getContextValue("quantity2"));

		HttpEntity<String> requestEntity = new HttpEntity<String>(
		        OrderDataWizHandler.toOrderJSON(expectedOrder), getHeaders(auth));

		RestTemplate template = new RestTemplate();
		HttpEntity<Order> responseEntity = template.postForEntity(
		        "http://localhost:8080/tadah-sample/aggregators/orders",
		        requestEntity, Order.class);

		Order actualOrder = responseEntity.getBody();
		Assert.assertEquals("Number of items", expectedOrder.size(), actualOrder.getItems().size());
	}

	static HttpHeaders getHeaders(String auth) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

	    byte[] encodedAuthorisation = Base64.encode(auth.getBytes());
	    headers.add("Authorization", "Basic " + new String(encodedAuthorisation));

	    return headers;
	}

}
