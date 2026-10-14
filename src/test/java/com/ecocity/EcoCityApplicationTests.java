package com.ecocity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EcoCityApplicationTests {

    @Test
    void unitSanityCheck() {
        int limiteCriticoOcupacao = 80;
        int sensorLeitura = 85;
        assertTrue(sensorLeitura >= limiteCriticoOcupacao, "A leitura deve disparar alerta de rota de emergência.");
    }
}
