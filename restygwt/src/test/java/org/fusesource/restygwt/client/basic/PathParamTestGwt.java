/**
 * Copyright (C) 2009-2012 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fusesource.restygwt.client.basic;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestService;
import org.fusesource.restygwt.client.RestServiceProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

/**
 *
 *
 * @author mkristian
 *
 */
public class PathParamTestGwt extends GWTTestCase {

    private TestRestService service;

    @Override
    public String getModuleName() {
        return "org.fusesource.restygwt.EchoTestGwt";
    }

    static interface TestRestService extends RestService {
        
        @Path("/get")
        void get(MethodCallback<Echo> callback);

        @Path("/get/{id}")
        void get(@PathParam(value = "id") int id, MethodCallback<Echo> callback);

        @Path("/get/{id}")
        void get(@PathParam(value = "id") Integer id, MethodCallback<Echo> callback);
    }
    
    class EchoMethodCallback implements MethodCallback<Echo> {
        
        private final String path;

        EchoMethodCallback(String path){
            this.path = path;
        }
        
        @Override
        public void onSuccess(Method method, Echo response) {

            assertEquals(response.path, path);
            finishTest();

        }

        @Override
        public void onFailure(Method method, Throwable exception) {
            fail();
        }
    }
    
    @Override
    protected void gwtSetUp() throws Exception {
        super.gwtSetUp();        
        service = GWT.create(TestRestService.class);  
        Resource resource = new Resource(GWT.getModuleBaseURL() + "echo");
        ((RestServiceProxy) service).setResource(resource);
    }

    public void testGet() {

        service.get(new EchoMethodCallback("/get"));

    }

    public void testGetWithInteger() {
    
        service.get(new Integer(2), new EchoMethodCallback("/get/2"));

    }

    public void testGetWithNull() {
    
        service.get(null, new EchoMethodCallback("/get/null"));

    }

    public void testGetWithInt() {
    
        service.get(123, new EchoMethodCallback("/get/123"));

    }

    public void gwtTearDown() {

        // wait... we are in async testing...
        delayTestFinish(10000);
        
    }
}