package es.leanmind.reports.integration;

import es.leanmind.reports.controller.api.EstablishmentsController;
import es.leanmind.reports.domain.EstablishmentDTO;
import es.leanmind.reports.domain.EstablishmentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(value = EstablishmentsController.class, secure = false)
public class ApiEstablishmentControllerShould {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstablishmentService establishmentServiceStub;

    @Test
    public void retrieve_establishment_parse_to_json() throws Exception {
        List<EstablishmentDTO> establishmentDTOList = asList(new EstablishmentDTO(1, "La cuchara sana"));
        given(establishmentServiceStub.retrieveEstablishmentsFor(any())).willReturn(establishmentDTOList);
        String expected = "[{\"id\":1, \"name\":\"La cuchara sana\"}]";

        mockMvc.perform(get("/api/establishments"))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }
}
