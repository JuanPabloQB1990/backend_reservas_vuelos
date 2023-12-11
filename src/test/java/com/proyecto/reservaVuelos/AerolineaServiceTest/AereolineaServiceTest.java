package com.proyecto.reservaVuelos.AerolineaServiceTest;

import com.proyecto.reservaVuelos.models.AerolineaModel;
import com.proyecto.reservaVuelos.repositories.AerolineaRepository;
import com.proyecto.reservaVuelos.services.AerolineaService;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class AereolineaServiceTest {

    @Test
    public void testSaveAerolinea() {
        AerolineaModel aerolineaModel = new AerolineaModel();
        AerolineaRepository aerolineaRepository = mock(AerolineaRepository.class);
        AerolineaService aerolineaService = new AerolineaService(aerolineaRepository);
        aerolineaService.saveAerolinea(aerolineaModel);
        verify(aerolineaRepository, times(1)).save(aerolineaModel);
    }
}
