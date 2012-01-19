package org.infinispan;


import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.test.fwk.TestCacheManagerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.infinispan.test.SingleCacheManagerTest;


// ****************************************************************************************
// This sample test should be used as a starting point when writing tests for Infinispan.
//
// See http://community.jboss.org/wiki/ParallelTestSuite for more information.
// ****************************************************************************************


// All tests must be annotated with @Test and contain the groups and testName attributes.
@Test(groups = "stress", testName = "SampleStressTest")

// This test class extends SingleCacheManagerTest which is designed to test a single node instance.  Use
// MultipleCacheManagerTest for tests designed to test Infinispan in a cluster.
public class SampleStressTest extends SingleCacheManagerTest {

   @Override
   protected EmbeddedCacheManager createCacheManager() throws Exception {

      // First create a ConfigurationBuilder and configure your cache.
      ConfigurationBuilder builder = new ConfigurationBuilder();
      

      // create non-clustered cache manager
      EmbeddedCacheManager cm = TestCacheManagerFactory.createCacheManager(builder);

      // the superclass will register this EmbeddedCacheManager and handle cleanup of resources for you.  It will also
      // create the default cache instance and make it available to your tests.
      return cm;
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

   // This is where you write your test(s).  Stress tests would typically require multiple invocations
   @Test(invocationCount = 5)
   public void testCacheOperations() {
      // the cache instance is made available by the superclass.

      cache.put("Hello", "World");
      assert "World".equals(cache.get("Hello")) : "Was expecting 'World' but saw something else instead!";

      // no need to clean up.
   }
}
