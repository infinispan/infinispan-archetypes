package infinispan;

import org.infinispan.Cache;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryVisited;
import org.infinispan.notifications.cachelistener.event.CacheEntryEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryVisitedEvent;
import org.infinispan.notifications.cachemanagerlistener.event.Event;
import org.infinispan.commons.util.concurrent.NotifyingFuture;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Sample application code.  For more examples visit the Quick Starts section on http://infinispan.org/documentation/
 */
public class Application {

   public void basicUse() {
      System.out.println("\n\n1.  Demonstrating basic usage of Infinispan.  This cache stores arbitrary Strings.");
      Cache<String, String> cache = SampleCacheContainer.getCache();

      System.out.println("  Storing value 'World' under key 'Hello'");
      String oldValue = cache.put("Hello", "World");
      System.out.printf("  Done.  Saw old value as '%s'\n", oldValue);

      System.out.println("  Replacing 'World' with 'Mars'.");
      boolean worked = cache.replace("Hello", "World", "Mars");
      System.out.printf("  Successful? %s\n", worked);

      assert oldValue == null;
      assert worked == true;
   }

   public void lifespans() throws InterruptedException {
      System.out.println("\n\n2.  Demonstrating usage of Infinispan with expirable entries.");
      Cache<String, Float> stocksCache = SampleCacheContainer.getCache("stockTickers");
      System.out.println("  Storing key 'RHT' for 10 seconds.");
      stocksCache.put("RHT", 45.0f, 10, TimeUnit.SECONDS);
      System.out.printf("  Checking for existence of key.  Is it there? %s\n", stocksCache.containsKey("RHT"));
      System.out.println("  Sleeping for 10 seconds...");
      Thread.sleep(10000);
      System.out.printf("  Checking for existence of key.  Is it there? %s\n", stocksCache.containsKey("RHT"));
      assert stocksCache.get("RHT") == null;
   }

   public void asyncOperations() {
      System.out.println("\n\n3.  Demonstrating asynchronous operations - where writes can be done in a non-blocking fashion.");
      Cache<String, Integer> wineCache = SampleCacheContainer.getCache("wineCache");

      System.out.println("  Put #1");
      NotifyingFuture<Integer> f1 = wineCache.putAsync("Pinot Noir", 300);
      System.out.println("  Put #1");
      NotifyingFuture<Integer> f2 = wineCache.putAsync("Merlot", 120);
      System.out.println("  Put #1");
      NotifyingFuture<Integer> f3 = wineCache.putAsync("Chardonnay", 180);

      // now poll the futures to make sure any remote calls have completed!
      for (NotifyingFuture<Integer> f: Arrays.asList(f1, f2, f3)) {
         try {
            System.out.println("  Checking future... ");
            f.get();
         } catch (Exception e) {
            throw new RuntimeException("Operation failed!", e);
         }
      }
      System.out.println("  Everything stored!");

      // TIP: For more examples on using the asynchronous API, visit http://infinispan.org/docs/8.2.x/user_guide/user_guide.html#_Listeners_and_notifications_section
   }

   public void registeringListeners() {
      System.out.println("\n\n4.  Demonstrating use of listeners.");
      Cache<Integer, String> anotherCache = SampleCacheContainer.getCache("another");
      System.out.println("  Attaching listener");
      MyListener l = new MyListener();
      anotherCache.addListener(l);

      System.out.println("  Put #1");
      anotherCache.put(1, "One");
      System.out.println("  Put #2");
      anotherCache.put(2, "Two");
      System.out.println("  Put #3");
      anotherCache.put(3, "Three");

      // TIP: For more examples on using listeners visit http://community.jboss.org/wiki/ListenersandNotifications
   }

   public static void main(String[] args) throws Exception {
      System.out.println("\n\n\n   ********************************  \n\n\n");
      System.out.println("Hello.  This is a sample application making use of Infinispan.");
      Application a = new Application();
      a.basicUse();
      a.lifespans();
      a.asyncOperations();
      a.registeringListeners();
      System.out.println("Sample complete.");
      System.out.println("\n\n\n   ********************************  \n\n\n");
   }

   @Listener
   @SuppressWarnings("unused")
   public class MyListener {

      @CacheEntryCreated
      @CacheEntryModified
      @CacheEntryRemoved
      public void printDetailsOnChange(CacheEntryEvent e) {
         System.out.printf("Thread %s has modified an entry in the cache named %s under key %s!\n",
                           Thread.currentThread().getName(), e.getCache().getName(), e.getKey());
      }

      @CacheEntryVisited
      public void printDetailsOnVisit(CacheEntryVisitedEvent e) {
         System.out.printf("Thread %s has visited an entry in the cache named %s under key %s!\n",
                           Thread.currentThread().getName(), e.getCache().getName(), e.getKey());
      }
   }
}

