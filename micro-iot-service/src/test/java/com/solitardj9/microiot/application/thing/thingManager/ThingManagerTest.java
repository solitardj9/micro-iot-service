package com.solitardj9.microiot.application.thing.thingManager;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solitardj9.microiot.application.thing.thingManager.model.Thing;
import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingAlreayExist;
import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingManagerFailure;
import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingNotFound;
import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingVersionMismatch;
import com.solitardj9.microiot.application.thing.thingManager.service.ThingManager;
import com.solitardj9.microiot.serviceInterface.thing.thingManagerInterface.model.request.RequestCreateThing;
import com.solitardj9.microiot.serviceInterface.thing.thingManagerInterface.model.request.RequestUpdateThing;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ThingManagerTest {

	@Autowired
	ThingManager thingManager;
	
	private ObjectMapper om = new ObjectMapper();
	
	@Test
	public void testAll() {
		//
		String thingPropetry1 = "{\n" + 
				"   \"thingTypeName\" : \"myThingType\",\n" + 
				"   \"attributePayload\" : {\n" + 
				"      \"attributes\" : {\n" + 
				"         \"key1\" : \"aaa\",\n" + 
				"         \"key2\" : \"bbb\"\n" + 
				"      },\n" + 
				"      \"merge\" : false\n" + 
				"   }\n" + 
				"}";
		
		String thingPropetry2 = "{\n" + 
				"   \"thingTypeName\" : \"myThingType_1\",\n" + 
				"   \"expectedVersion\" : 1,\n" +
				"   \"removeThingType\" : false,\n" +
				"   \"attributePayload\" : {\n" + 
				"      \"attributes\" : {\n" + 
				"         \"key1\" : \"aaa\",\n" + 
				"         \"key2\" : \"ccc\",\n" + 
				"         \"key3\" : \"ddd\"\n" +
				"      },\n" + 
				"      \"merge\" : false\n" + 
				"   }\n" + 
				"}";
		
		String thingPropetry3 = "{\n" + 
				"   \"thingTypeName\" : \"myThingType_1\",\n" + 
				"   \"expectedVersion\" : 2,\n" +
				"   \"removeThingType\" : false,\n" +
				"   \"attributePayload\" : {\n" + 
				"      \"attributes\" : {\n" + 
				"         \"key1\" : \"aaa\",\n" + 
				"         \"key2\" : \"bbb\",\n" + 
				"         \"key3\" : \"ccc\",\n" +
				"         \"key4\" : \"ddd\"\n" +
				"      },\n" + 
				"      \"merge\" : true\n" + 
				"   }\n" + 
				"}";
		
		System.out.println("// 1) create things --------------------------------------------------------------------");
		for (int i = 0 ; i < 3 ; i++) {
			String thingName = "thing_" + i;
			
			try {
				RequestCreateThing request = om.readValue(thingPropetry1, RequestCreateThing.class);
				Thing thing = thingManager.createThing(thingName, request.getThingTypeName(), request.getAttributePayload().getAttributes(), request.getAttributePayload().getMerge());
				System.out.println("thing = " + thing.toString());
			}
			catch (ExceptionThingAlreayExist e) { e.printStackTrace(); }
			catch (Exception e) { e.printStackTrace(); }
		}

		System.out.println("// 2) get thing --------------------------------------------------------------------");
		try {
			String thingName = "thing_1";
			System.out.println(thingManager.getThingByThingName(thingName));
		}
		catch (com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingBadRequest | ExceptionThingNotFound e) { e.printStackTrace(); }
		
		System.out.println("// 3) update thing (merge=false)--------------------------------------------------------------------");
		try {
			String thingName = "thing_1";
			RequestUpdateThing request = om.readValue(thingPropetry2, RequestUpdateThing.class);
			thingManager.updateThing(thingName, request.getThingTypeName(), request.getRemoveThingType(), request.getAttributePayload().getAttributes(), request.getAttributePayload().getMerge(), request.getExpectedVersion());
		}
		catch (ExceptionThingNotFound e) { e.printStackTrace(); }
		catch (Exception e) { e.printStackTrace(); }
		
		System.out.println("// 4) get thing --------------------------------------------------------------------");
		try {
			String thingName = "thing_1";
			System.out.println(thingManager.getThingByThingName(thingName));
		}
		catch (com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingBadRequest | ExceptionThingNotFound e) { e.printStackTrace(); }
		
		System.out.println("// 5) update thing (merge=true)--------------------------------------------------------------------");
		try {
			String thingName = "thing_1";
			RequestUpdateThing request = om.readValue(thingPropetry3, RequestUpdateThing.class);
			thingManager.updateThing(thingName, request.getThingTypeName(), request.getRemoveThingType(), request.getAttributePayload().getAttributes(), request.getAttributePayload().getMerge(), request.getExpectedVersion());
		}
		catch (ExceptionThingNotFound e) { e.printStackTrace(); }
		catch (Exception e) { e.printStackTrace(); }
		
		System.out.println("// 6) get thing --------------------------------------------------------------------");
		try {
			String thingName = "thing_1";
			System.out.println(thingManager.getThingByThingName(thingName));
		}
		catch (com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingBadRequest | ExceptionThingNotFound e) { e.printStackTrace(); }
		
		System.out.println("// 7) delete thing --------------------------------------------------------------------");
		try {
			String thingName = "thing_2";
			Integer expectedVersion = null;
			thingManager.deleteThing(thingName, expectedVersion);
		}
		catch (com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingBadRequest | ExceptionThingNotFound | ExceptionThingVersionMismatch | ExceptionThingManagerFailure e) { e.printStackTrace(); }
		
		System.out.println("// 8) get thing --------------------------------------------------------------------");
		try {
			String thingName = "thing_2";
			System.out.println(thingManager.getThingByThingName(thingName));
		}
		catch (com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingBadRequest | ExceptionThingNotFound e) { e.printStackTrace(); }
		
	}
}