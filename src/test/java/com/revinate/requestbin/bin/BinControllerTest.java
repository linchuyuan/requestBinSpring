/*
unit test set
*/

//Note: hasSize does work on map structure
package com.revinate.requestbin.bin;

import com.revinate.requestbin.bin.model.Bin;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BinControllerTest {

    @Autowired
    private BinController controller;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getAllBin() throws Exception {
        create();create(); // create two buckets
        mockMvc.perform(get("/bin"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.bucket1", not(isEmptyOrNullString())))
            .andExpect(jsonPath("$.bucket0", not(isEmptyOrNullString())));
        //two bucket should exsit
        
    }

    
    // create the bin
    @Test
    public void createBin() throws Exception {
        mockMvc.perform(post("/bin"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", not(isEmptyOrNullString())));
    }

    //inspect the bin
    @Test
    public void inspectBin() throws Exception {
        String id = controller.create();
        mockMvc.perform(get("/bin/"+id)).andExpect(status().isOk());
        mockMvc.perform(head("/bin/"+id)).andExpect(status().isOk());
        mockMvc.perform(options("/bin/"+id)).andExpect(status().isOk());
        mockMvc.perform(put("/bin/"+id)).andExpect(status().isOk());
        mockMvc.perform(patch("/bin/"+id)).andExpect(status().isOk());
        mockMvc.perform(post("/bin/"+id)).andExpect(status().isOk());
        mockMvc.perform(delete("/bin/"+id)).andExpect(status().isOk());
        mockMvc.perform(get("/bin/" + id + "/inspect")).andExpect(status().isOk());
    }

    private void create() throws Exception{
        mockMvc.perform(post("/bin")).andExpect(status().isOk());
    }

}
