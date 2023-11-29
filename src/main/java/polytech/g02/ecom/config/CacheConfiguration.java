package polytech.g02.ecom.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, polytech.g02.ecom.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, polytech.g02.ecom.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, polytech.g02.ecom.domain.User.class.getName());
            createCache(cm, polytech.g02.ecom.domain.Authority.class.getName());
            createCache(cm, polytech.g02.ecom.domain.User.class.getName() + ".authorities");
            createCache(cm, polytech.g02.ecom.domain.Patient.class.getName());
            createCache(cm, polytech.g02.ecom.domain.Patient.class.getName() + ".mesures");
            createCache(cm, polytech.g02.ecom.domain.Patient.class.getName() + ".aideSoignants");
            createCache(cm, polytech.g02.ecom.domain.Patient.class.getName() + ".infirmieres");
            createCache(cm, polytech.g02.ecom.domain.Patient.class.getName() + ".repas");
            createCache(cm, polytech.g02.ecom.domain.Patient.class.getName() + ".alertes");
            createCache(cm, polytech.g02.ecom.domain.AideSoignant.class.getName());
            createCache(cm, polytech.g02.ecom.domain.AideSoignant.class.getName() + ".users");
            createCache(cm, polytech.g02.ecom.domain.AideSoignant.class.getName() + ".patients");
            createCache(cm, polytech.g02.ecom.domain.Infirmiere.class.getName());
            createCache(cm, polytech.g02.ecom.domain.Infirmiere.class.getName() + ".users");
            createCache(cm, polytech.g02.ecom.domain.Infirmiere.class.getName() + ".patients");
            createCache(cm, polytech.g02.ecom.domain.Medecin.class.getName());
            createCache(cm, polytech.g02.ecom.domain.Medecin.class.getName() + ".patients");
            createCache(cm, polytech.g02.ecom.domain.Medecin.class.getName() + ".users");
            createCache(cm, polytech.g02.ecom.domain.Medecin.class.getName() + ".alertes");
            createCache(cm, polytech.g02.ecom.domain.Medecin.class.getName() + ".rappels");
            createCache(cm, polytech.g02.ecom.domain.Etablissement.class.getName());
            createCache(cm, polytech.g02.ecom.domain.Etablissement.class.getName() + ".patients");
            createCache(cm, polytech.g02.ecom.domain.Etablissement.class.getName() + ".users");
            createCache(cm, polytech.g02.ecom.domain.Repas.class.getName());
            createCache(cm, polytech.g02.ecom.domain.Repas.class.getName() + ".patients");
            createCache(cm, polytech.g02.ecom.domain.Rappel.class.getName());
            createCache(cm, polytech.g02.ecom.domain.Rappel.class.getName() + ".medecins");
            createCache(cm, polytech.g02.ecom.domain.Alerte.class.getName());
            createCache(cm, polytech.g02.ecom.domain.Alerte.class.getName() + ".patients");
            createCache(cm, polytech.g02.ecom.domain.Alerte.class.getName() + ".medecins");
            createCache(cm, polytech.g02.ecom.domain.Mesure.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
