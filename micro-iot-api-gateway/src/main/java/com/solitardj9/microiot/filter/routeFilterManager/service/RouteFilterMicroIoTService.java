package com.solitardj9.microiot.filter.routeFilterManager.service;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.solitardj9.microiot.filter.routeFilterManager.model.Token;

@Component
public class RouteFilterMicroIoTService extends ZuulFilter {
	//
	private static final Logger logger = LoggerFactory.getLogger(RouteFilterMicroIoTService.class);
	
	@Autowired
	DiscoveryClient discoveryClient;
	
	private ObjectMapper om = new ObjectMapper();
	
	private Random random = new Random();
	
	@PostConstruct
	public void init() {
		
	}
	
	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public String filterType() {
		return FilterConstants.ROUTE_TYPE;
	}

	@Override
	public int filterOrder() {
		return 0;
	}
	
	@Override
	public Object run() throws ZuulException {
    	//
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		//URL previousrouteHost = ctx.getRouteHost();
		
		String xApiTken = request.getHeader("x-api-token");
		Token token = null;
		try {
			token = om.readValue(xApiTken, Token.class);
		} catch (JsonProcessingException e) {
			logger.error("[RouteFilterMicroIoTService].run : error = " + e);
			return null;
		}
		
		String serviceId = (String)ctx.get("serviceId");
		//String previousUri = request.getRequestURI();
		//String contextUri = (String)ctx.get("requestURI");
		
		if (serviceId.equals("micro-iot-service-thing")) {
			//
			String routedServiceId = "micro-iot-service";
			ctx.set("serviceId", routedServiceId);
		}
		else if (serviceId.equals("micro-iot-service-state")) {
			//
			String routedServiceId = token.getServiceId();
			
			List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances("micro-iot-service");
			List<ServiceInstance> routedServiceInstanceList = new ArrayList<>();
			for (ServiceInstance iter : serviceInstanceList) {
				if (iter.getInstanceId().startsWith(routedServiceId)) {
					routedServiceInstanceList.add(iter);
				}
			}
			
			int size = routedServiceInstanceList.size();
			if (size <= 0) {
				logger.error("[RouteFilterMicroIoTService].run : error = routedServiceInstanceList is empty.");
				return null;
			}
			ServiceInstance routedServiceInstance = routedServiceInstanceList.get(random.nextInt(size));
			
			logger.info("[RouteFilterMicroIoTService].run : routedServiceInstance ID = " + routedServiceInstance.getInstanceId());
			logger.info("[RouteFilterMicroIoTService].run : routedServiceInstance host = " + routedServiceInstance.getHost());
			logger.info("[RouteFilterMicroIoTService].run : routedServiceInstance uri = " + routedServiceInstance.getUri());
			
			try {
				ctx.setRouteHost(routedServiceInstance.getUri().toURL());
			} catch (MalformedURLException e) {
				logger.error("[RouteFilterMicroIoTService].run : error = " + e);
				return null;
			}
		}
		else {
			// nothing to do.
		}
	    
    	return null;
	}
}