package com.bs.search.common.config;

import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.DiskStoreConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import net.sf.ehcache.config.CacheConfiguration.TransactionalMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * packageName : com.bs.search.common.config
 * fileName : CacheConfig
 * author : yelee
 * date : 2021-12-04
 * description :
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-04       isbn8         최초 생성
 */
@EnableCaching
@Configuration
public class CacheConfig {

    private net.sf.ehcache.CacheManager createCacheManager() {
        net.sf.ehcache.config.Configuration configuration = new net.sf.ehcache.config.Configuration();
        configuration.diskStore(new DiskStoreConfiguration().path("java.io.tmpdir"));
        return net.sf.ehcache.CacheManager.create(configuration);
    }

    @Bean
    public EhCacheCacheManager ehCacheCacheManager() {
        net.sf.ehcache.CacheManager manager = this.createCacheManager();
        Cache getMenuCache = new Cache(new CacheConfiguration()
                .maxEntriesLocalHeap(1000)
                .maxEntriesLocalDisk(10000)
                .eternal(false)
                .timeToIdleSeconds(1800)
                .timeToLiveSeconds(1800)
                .memoryStoreEvictionPolicy("LFU")
                .transactionalMode(TransactionalMode.OFF)
                .persistence(new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP))
                .name("search")
        );
        manager.addCache(getMenuCache);
        return new EhCacheCacheManager(manager);
    }

}
