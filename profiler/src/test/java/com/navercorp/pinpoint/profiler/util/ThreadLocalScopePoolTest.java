package com.navercorp.pinpoint.profiler.util;

import com.navercorp.pinpoint.bootstrap.instrument.AttachmentFactory;
import com.navercorp.pinpoint.bootstrap.instrument.DefaultScopeDefinition;
import com.navercorp.pinpoint.bootstrap.instrument.Scope;
import com.navercorp.pinpoint.bootstrap.instrument.ScopeDefinition;
import junit.framework.Assert;
import org.junit.Test;

public class ThreadLocalScopePoolTest {

    @Test
    public void testGetScope_Type_SIMPLE() throws Exception {

        ScopePool pool = new ThreadLocalScopePool();
        Scope scope = pool.getScope(new DefaultScopeDefinition("test", ScopeDefinition.Type.SIMPLE));
        Assert.assertTrue(scope instanceof ThreadLocalScope);

        Assert.assertEquals("name", scope.getName(), "test");
    }

    @Test
    public void testGetScope_Type_ATTACHMENT() throws Exception {

        ScopePool pool = new ThreadLocalScopePool();
        Scope scope = pool.getScope(new DefaultScopeDefinition("test", ScopeDefinition.Type.ATTACHMENT));
        Assert.assertTrue(scope instanceof AttachmentThreadLocalScope);

        Assert.assertEquals("name", scope.getName(), "test");
    }


    @Test
     public void testAttachment() throws Exception {

        ScopePool pool = new ThreadLocalScopePool();
        Scope test = pool.getScope(new DefaultScopeDefinition("test", ScopeDefinition.Type.ATTACHMENT));
        @SuppressWarnings("unchecked")
        AttachmentThreadLocalScope<Object> scope = (AttachmentThreadLocalScope<Object>) test;
        scope.push();

        scope.push();
        Assert.assertNull(scope.getAttachment());
        scope.setAttachment("test");
        scope.pop();
        Assert.assertEquals(scope.getAttachment(), "test");
        Assert.assertTrue(scope.pop() == Scope.ZERO);
        Assert.assertNull(scope.getAttachment());

        Assert.assertEquals("name", scope.getName(), "test");
    }


    @Test
    public void testGetOrCreate() throws Exception {

        ScopePool pool = new ThreadLocalScopePool();
        Scope test = pool.getScope(new DefaultScopeDefinition("test", ScopeDefinition.Type.ATTACHMENT));
        @SuppressWarnings("unchecked")
        AttachmentThreadLocalScope<Object> scope = (AttachmentThreadLocalScope<Object>) test;
        scope.push();

        scope.push();
        Assert.assertNull(scope.getAttachment());
        Assert.assertEquals(scope.getOrCreate(new AttachmentFactory<Object>() {
            @Override
            public Object createAttachment() {
                return "test";
            };
        }), "test");
        scope.pop();
        Assert.assertEquals(scope.getAttachment(), "test");
        Assert.assertTrue(scope.pop() == Scope.ZERO);
        Assert.assertNull(scope.getAttachment());

        Assert.assertEquals("name", scope.getName(), "test");
    }




}