package ly.muasica.base.auth;

import static org.junit.Assert.*;

/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test Class for the Authorization.
 * @author Daniel Gabl
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {
    
    /**
     * Mock Object.
     */
    @Autowired
    private MockMvc mockMvc;
    
    /**
     * Object Mapper.
     */
    @Autowired
    private ObjectMapper mapper;
    
    /**
     * Tests if a Post Requests is successful in general.
     * @throws Exception Exception in case something breaks
     */
    @Test
    public void testStaticPost() throws Exception  {
        String json = mapper.writeValueAsString(User.class);
        
        this.mockMvc.perform(post("/verify")
           .contentType(MediaType.APPLICATION_JSON)
           .content(json)
           .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk());
    }
    
    /**
     * Tests a Post Request with a test Value.
     * @throws Exception Exception in case something breaks
     */
    @Test
    public void testCreate() throws Exception  {
        String mail = "gabl@hm.edu";
          
        this.mockMvc.perform(post("/verify")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"mailaddr\": \"" + mail + "\"}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("gabl"))
            .andExpect(jsonPath("$.mailaddr").value("gabl@hm.edu"));
        
        // TODO Check verify() and find() with the ID of the posted User
    }
    
    /**
     * Test on a wrong E-Mail-Address.
     * @throws Exception Exception in case something breaks
     */
    @Test
    public void testCreateInvalidMail() throws Exception  {
        String mail = "gabl@hm.de";
          
        String result = this.mockMvc.perform(post("/verify")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"mailaddr\": \"" + mail + "\"}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        
       assertEquals(result, "");
    }
}
