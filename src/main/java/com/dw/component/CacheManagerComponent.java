package com.dw.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CacheManagerComponent {
    private static final Logger logger = LoggerFactory.getLogger(CacheManagerComponent.class);
    @Autowired
    private EhCacheCacheManager cacheManager;

    /**
     * 清除所有緩存
     */
    public void clearAllCaches() {
        String[] caches = cacheManager.getCacheManager().getCacheNames();
        Arrays.asList(caches).forEach(cacheName ->{
            System.out.println("--------->> [" + cacheName + "]before remove,cache size is " + cacheManager.getCacheManager().getCache(cacheName).getSize());
            List<Object> keys = cacheManager.getCacheManager().getCache(cacheName).getKeys();
            keys.forEach(key -> {
                System.out.println("_____________________" + cacheManager.getCacheManager().getCache(cacheName).get(key).getObjectValue().toString());
            });
            //cacheManager.getCacheManager().removeCache(cacheName);
            // 需要添加回，否則mybatis獲取的時候會報空指針
            //cacheManager.getCacheManager().addCache(cacheName);
            //System.out.println("--------->> after remove,cache size is " + cacheManager.getCacheManager().getCache(cacheName).getSize());
        });
    }

    /**
     * 按緩存名稱清除緩存，mybatis的緩存名稱默認為namespace，如com.dw.mapper.PosSetmealMapper
     * @param cacheName
     */
    public void clearByName(String cacheName) {
        logger.debug("--------->> before remove,cache size is " + cacheManager.getCacheManager().getCache(cacheName).getSize());
        cacheManager.getCacheManager().removeCache(cacheName);
        // 需要添加回，否則mybatis獲取的時候會報空指針
        cacheManager.getCacheManager().addCache(cacheName);
        logger.debug("--------->> after remove,cache size is " + cacheManager.getCacheManager().getCache(cacheName).getSize());
    }
}
