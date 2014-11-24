package priceboard;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.eaio.uuid.UUID;

public class UUIDTest {

	@Test
	public void testGenerateUUID() {
		List<String> uuidList = new ArrayList<String>();
		for(int i = 0; i < 1000; i++) {
			String uuid = new UUID().toString();
			Assert.assertFalse(uuidList.contains(uuid));
			uuidList.add(uuid);
		}
	}
}
