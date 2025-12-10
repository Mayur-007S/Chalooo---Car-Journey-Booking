package com.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.cachemanager.CacheInspector;

@RestController
@RequestMapping("/api/v1/cache")
public class CacheController {

	private CacheInspector cacheinspector;

	public CacheController(CacheInspector cacheinspector) {
		this.cacheinspector = cacheinspector;
	}
	
	@GetMapping("/{cache}")
	public ResponseEntity<String> getCacheData(@PathVariable(value = "cache") String cache) {
		return ResponseEntity.ok(cacheinspector.printCacheContent(cache));
	}
	
}
