package org.infinispan;


import org.infinispan.config.Configuration;
import org.infinispan.test.MultipleCacheManagersTest;
import org.infinispan.test.TestingUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.transaction.Transaction;
import java.util.List;


// ****************************************************************************************
// This sample test should be used as a starting point when writing tests for Infinispan.
//
// See http://community.jboss.org/wiki/ParallelTestSuite for more information.
// ****************************************************************************************


// All tests must be annotated with @Test and contain the groups and testName attributes.
@Test (groups = "functional", testName = "SampleFunctionalTest")

// This test class extends MultipleCacheManagersTest which is designed to test a multiple node instances in a cluster.
// Use SingleCacheManagerTest for tests designed to test a single, isolated Infinispan node.
public class SampleFunctionalTest extends MultipleCacheManagersTest {

   @Override
   protected void createCacheManagers() throws Throwable {
      // This method constructs and registers as many cache managers as you wish.
      Configuration cfg = new Configuration();
      cfg.setCacheMode(Configuration.CacheMode.DIST_SYNC);
      cfg.setLockAcquisitionTimeout(100);

      // One easy way to do this is the createCluster method which takes a configuration and the number of nodes to create.
      createCluster(cfg, true, 5);

      // or if you wish to use configuration defaults you could just do:
//      createCluster(Configuration.CacheMode.DIST_SYNC, 5);
//      createCluster(Configuration.CacheMode.DIST_SYNC, true, 5);

      // alternately, you could manually create your own cache managers and register them:
//      EmbeddedCacheManager ecm1 = TestCacheManagerFactory.createClusteredCacheManager(cfg);
//      EmbeddedCacheManager ecm2 = TestCacheManagerFactory.createClusteredCacheManager(cfg);
//      EmbeddedCacheManager ecm3 = TestCacheManagerFactory.createClusteredCacheManager(cfg);
//      registerCacheManager(ecm1, ecm2, ecm3);
   }

   @BeforeMethod
   public void setUp() {
      // You could use this to set up your own init data
      // Note that you don't need to set up any Infinispan resources as this is taken care of by the superclass
   }

   @AfterMethod
   public void tearDown() {
      // You could use this to clean up your own init data
      // Note that you don't need to clean up any Infinispan resources as this is taken care of by the superclass
   }

   // Functional tests should not use multiple invocations.
   public void testCacheOperations() {
      // this is made available by the superclass.  Each cache in this list is a reference to a specific default cache
      // in the cluster.
      List<Cache<String, String>> caches = caches();

      caches.get(0).put("Key", "Value");

      for (Cache c: caches) {
         assert "Value".equals(c.get("Key")): "Was expecting to see state on cache " + address(c);
      }
   }

   public void testWithTransactions() throws Exception {
      tm(0).begin(); // starts a transaction on node 0
      cache(0).put("key", "value");
      Transaction tx = tm(0).suspend();

      assert !cache(1).containsKey("key") : "Should not see uncommitted changes";

      tm(0).resume(tx);
      tm(0).commit();

      assert cache(1).containsKey("key") : "Should see committed changes";
   }

   public void testHelpers() {
      cache(0).putAsync("key", "value");
      TestingUtil.sleepRandom(500); // Try to avoid sleeps in your tests!

      System.out.println("Contents of cache: " + TestingUtil.printCache(cache(0)));
   }
}
