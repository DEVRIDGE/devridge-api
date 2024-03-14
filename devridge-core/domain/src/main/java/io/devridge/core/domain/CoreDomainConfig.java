package io.devridge.core.domain;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EntityScan("io.devridge.core.domain") //
@EnableJpaRepositories("io.devridge.core.domain")
public class CoreDomainConfig {
}
