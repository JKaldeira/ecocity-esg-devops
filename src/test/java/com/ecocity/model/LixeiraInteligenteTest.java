package com.ecocity.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes unitários da entidade de domínio LixeiraInteligente.
 * Não dependem de infraestrutura (MongoDB/contexto Spring), garantindo
 * execução rápida e determinística na etapa de testes do pipeline de CI.
 */
class LixeiraInteligenteTest {

    private static final int LIMITE_CRITICO_OCUPACAO = 80;

    @Test
    @DisplayName("Construtor deve preencher todos os atributos do sensor")
    void deveCriarLixeiraComAtributosCompletos() {
        LixeiraInteligente lixeira =
                new LixeiraInteligente("LX-001", "RECICLAVEL", "Vila Madalena", 120.0, 45, "ATIVA");

        assertEquals("LX-001", lixeira.getCodigo());
        assertEquals("RECICLAVEL", lixeira.getTipo());
        assertEquals("Vila Madalena", lixeira.getBairro());
        assertEquals(120.0, lixeira.getCapacidadeKg());
        assertEquals(45, lixeira.getOcupacaoAtualPct());
        assertEquals("ATIVA", lixeira.getStatus());
    }

    @Test
    @DisplayName("Ocupação acima do limite crítico deve sinalizar coleta urgente")
    void deveSinalizarColetaUrgenteAcimaDoLimite() {
        LixeiraInteligente lixeira =
                new LixeiraInteligente("LX-002", "ORGANICO", "Pinheiros", 100.0, 92, "ATIVA");

        assertTrue(lixeira.getOcupacaoAtualPct() >= LIMITE_CRITICO_OCUPACAO,
                "Leitura de 92% deve disparar alerta de rota de coleta emergencial.");
    }

    @Test
    @DisplayName("Ocupação abaixo do limite crítico não deve sinalizar urgência")
    void naoDeveSinalizarUrgenciaAbaixoDoLimite() {
        LixeiraInteligente lixeira =
                new LixeiraInteligente("LX-003", "VIDRO", "Butantã", 80.0, 30, "ATIVA");

        assertFalse(lixeira.getOcupacaoAtualPct() >= LIMITE_CRITICO_OCUPACAO,
                "Leitura de 30% não deve gerar rota de coleta.");
    }

    @Test
    @DisplayName("Setters devem permitir a atualização da leitura do sensor")
    void deveAtualizarLeituraDoSensor() {
        LixeiraInteligente lixeira = new LixeiraInteligente();
        lixeira.setCodigo("LX-004");
        lixeira.setOcupacaoAtualPct(10);
        lixeira.setStatus("ATIVA");

        lixeira.setOcupacaoAtualPct(95);
        lixeira.setStatus("COLETA_SOLICITADA");

        assertNotNull(lixeira.getCodigo());
        assertEquals(95, lixeira.getOcupacaoAtualPct());
        assertEquals("COLETA_SOLICITADA", lixeira.getStatus());
    }
}
