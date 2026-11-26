package com.wealth.bff.client;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Client;
import feign.Request;
import feign.Response;

// Every Feign call goes out through this wrapper, which swaps the deployed BSS address for the one in BssEndpoint, so a change takes effect at once.
@Configuration
public class BssEndpointClientConfig {

	@Bean
	static BeanPostProcessor bssEndpointClientWrapper(ObjectProvider<BssEndpoint> endpoint) {
		return new BeanPostProcessor() {
			@Override
			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
				return bean instanceof Client client && !(bean instanceof RewritingClient) ? new RewritingClient(client, endpoint) : bean;
			}
		};
	}

	static final class RewritingClient implements Client {

		private final Client delegate;
		private final ObjectProvider<BssEndpoint> endpoint;

		RewritingClient(Client delegate, ObjectProvider<BssEndpoint> endpoint) {
			this.delegate = delegate;
			this.endpoint = endpoint;
		}

		@Override
		public Response execute(Request request, Request.Options options) throws IOException {
			BssEndpoint bss = endpoint.getObject();
			String deployed = bss.defaultUrl();
			String current = bss.current();
			if (current.equals(deployed) || !request.url().startsWith(deployed)) {
				return delegate.execute(request, options);
			}
			String url = current + request.url().substring(deployed.length());
			Request moved = Request.create(request.httpMethod(), url, request.headers(), Request.Body.create(request.body(), request.charset()),
					request.requestTemplate());
			return delegate.execute(moved, options);
		}
	}
}
