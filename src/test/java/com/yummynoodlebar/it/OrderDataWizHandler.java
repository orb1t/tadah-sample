/**
 *
 */
package com.yummynoodlebar.it;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.mdkt.datawiz.AbstractDataWiz;

/**
 * @author trung
 *
 */
public class OrderDataWizHandler extends AbstractDataWiz {
	public void userLoggedIn(String auth) {

	}

	public void selectItemWithQuantity(String item, Integer quantity) {

	}

	public static String toOrderJSON(Map<String, Integer> orders) {
		StringBuffer buf = new StringBuffer();
		buf.append("{\"items\":{");
		if (MapUtils.isNotEmpty(orders)) {
			for (String key : orders.keySet()) {
				buf.append("\"").append(key).append("\":").append(orders.get(key)).append(",");
			}
			buf.deleteCharAt(buf.length() - 1);
		}
		buf.append("}");
		return buf.toString();
	}
}
