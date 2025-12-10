package com.api.cachemanager;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheInspector {

	private CacheManager cacheManager;
		
	private Logger log = LoggerFactory.getLogger(CacheInspector.class);
	
	public CacheInspector(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}   

	public String printCacheContent(String cacheName) {
		Cache cache = cacheManager.getCache(cacheName);
		if (cache != null) {
			log.info("Cache Contents: "+ Objects.requireNonNull(cache.getNativeCache().toString()));
			return Objects.requireNonNull(cache.getNativeCache().toString());
		} else {
			log.info("No Such Cache Found: " + cacheName);
			return ("No Such Cache Found: " + cacheName);
		}
	}
}                         
