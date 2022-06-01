package it.gov.pagopa.paymentupdater.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

public class BeanConf{

	@Value("${bootstrap.servers}")
	protected String bootstrapServersKey;
	@Value("${security.protocol}")
	protected String securityProtocolKey;
	@Value("${sasl.mechanism}")
	protected String saslMechanismKey;
	@Value("${sasl.jaas.conf}")
	protected String saslJaasConfKey;

	@Value("${security.protocol.payment}")
	protected String securityProtocolKeyPayValue;
	@Value("${sasl.mechanism.payment}")
	protected String saslMechanismKeyPayValue;


	public void getProps(Map<String, Object> props, String url, String server) {
		props.put(bootstrapServersKey, server);
		props.put(securityProtocolKey, securityProtocolKeyPayValue);
		props.put(saslMechanismKey, saslMechanismKeyPayValue);
		props.put(saslJaasConfKey, url);
	}

	
	
}
