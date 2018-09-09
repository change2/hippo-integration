package org.change.hippo.server.apollo;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableApolloConfig(value = {ApolloNamespace.NS_APPLICATION}, order = 10)
public class ApolloApplication {

	@Bean
	public ApolloConfig apolloConfig() {
		return new ApolloConfig();
	}
}
