package org.einnovator.blueprint.entity1.config;

import org.einnovator.util.model.ObjectBase;
import org.einnovator.util.model.ToStringCreator;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app")
public class AppConfiguration extends ObjectBase {

	public AppConfiguration() {
	}
	


	@Override
	public ToStringCreator toString(ToStringCreator creator) {
		return creator;
	}

}
