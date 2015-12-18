package org.springframework.integration.samples.jpa;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ClassUtils;

/**
 * @author Artem Bilan
 * @since 4.2
 */
@Configuration
@ConditionalOnClass({LocalContainerEntityManagerFactoryBean.class,
		EnableTransactionManagement.class, EntityManager.class})
@Conditional(EclipseLinkAutoConfiguration.EclipseLinkEntityManagerCondition.class)
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
@Profile("eclipseLink")
public class EclipseLinkAutoConfiguration extends JpaBaseConfiguration {

	@Override
	protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
		return new EclipseLinkJpaVendorAdapter();
	}

	@Override
	protected Map<String, Object> getVendorProperties() {
		return new HashMap<String, Object>();
	}


	@Order(Ordered.HIGHEST_PRECEDENCE + 20)
	static class EclipseLinkEntityManagerCondition extends SpringBootCondition {

		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
			if (ClassUtils.isPresent("org.eclipse.persistence.jpa.JpaEntityManager", context.getClassLoader())) {
				return ConditionOutcome.match("found JpaEntityManager class");
			}
			else {
				return ConditionOutcome.noMatch("did not find JpaEntityManager class");
			}
		}

	}

}
