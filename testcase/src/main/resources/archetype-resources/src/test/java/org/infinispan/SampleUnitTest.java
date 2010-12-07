package org.infinispan;


import org.easymock.EasyMock;
import org.infinispan.config.Configuration;
import org.infinispan.distribution.ch.DefaultConsistentHash;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.remoting.transport.Address;
import org.infinispan.test.fwk.TestCacheManagerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.infinispan.test.SingleCacheManagerTest;

import java.util.Arrays;
import java.util.List;


// ****************************************************************************************
// This sample test should be used as a starting point when writing tests for Infinispan.
//
// See http://community.jboss.org/wiki/ParallelTestSuite for more information.
// ****************************************************************************************


// All tests must be annotated with @Test and contain the groups and testName attributes.
@Test(groups = "unit", testName = "SampleUnitTest")

// This test class extends SingleCacheManagerTest which is designed to test a single node instance.  Use
// MultipleCacheManagerTest for tests designed to test Infinispan in a cluster.
public class SampleUnitTest extends SingleCacheManagerTest {

   @Override
   protected EmbeddedCacheManager createCacheManager() throws Exception {
      // In this method, you should construct your cache manager instance using the TestCacheManagerFactory.

      // First create a Configuration and configure your system.
      Configuration cfg = new Configuration();
      cfg.setLockAcquisitionTimeout(1000);
      cfg.setConcurrencyLevel(100);

      // If you wish to use a transaction manager, you should pass the flag on to the TestCacheManagerFactory.  This
      // allows the factory to pick the appropriate transaction manager at test execution time.
      EmbeddedCacheManager cm = TestCacheManagerFactory.createCacheManager(cfg, true);

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

   // Unit tests should not use multiple invocations.
   public void testCacheOperations() {
      // the cache instance is made available by the superclass and can be used to test public API behavior.

      cache.put("Hello", "World");
      assert "World".equals(cache.get("Hello")) : "Was expecting 'World' but saw something else instead!";

      // no need to clean up.
   }

   // Internal components can be tested in Unit Tests as well.
   public void testSomeInternalComponent() {
      DefaultConsistentHash dch = new DefaultConsistentHash();

      // Making use of Mock Objects for non-critical components helps isolate the problem you are trying to test.
      dch.setCaches(Arrays.asList(EasyMock.createNiceMock(Address.class),
                                  EasyMock.createNiceMock(Address.class),
                                  EasyMock.createNiceMock(Address.class)));

      List<Address> a = dch.locate("somekey", 2);
      assert a.size() == 2 : "Was expecting 2 entries in the location list";
   }
}
