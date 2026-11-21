package jsp.springbootclasstask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheInspectionService {

    @Autowired
    private CacheManager cacheManager;



    public void printCacheContents(String cacheName) {

        Cache cache = cacheManager.getCache(cacheName);

        if (cache != null) {
//            System.out.println("========== CACHE INSPECTION ==========");
            System.out.println("Cache Name: " + cacheName);
            System.out.println("Cache Contents:");

            System.out.println(cache.getNativeCache());

//            System.out.println("======================================");

        }
        else {
            System.out.println("No such cache:" + cacheName);
        }


    }
}
