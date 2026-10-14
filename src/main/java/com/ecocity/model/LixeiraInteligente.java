package com.ecocity.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "lixeiras_inteligentes")
public class LixeiraInteligente {
    @Id
    private String id;
    private String codigo;
    private String tipo;
    private String bairro;
    
    @Field("capacidade_kg")
    private Double capacidadeKg;
    
    @Field("ocupacao_atual_pct")
    private Integer ocupacaoAtualPct;
    
    private String status;

    public LixeiraInteligente() {}

    public LixeiraInteligente(String codigo, String tipo, String bairro, Double capacidadeKg, Integer ocupacaoAtualPct, String status) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.bairro = bairro;
        this.capacidadeKg = capacidadeKg;
        this.ocupacaoAtualPct = ocupacaoAtualPct;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public Double getCapacidadeKg() { return capacidadeKg; }
    public void setCapacidadeKg(Double capacidadeKg) { this.capacidadeKg = capacidadeKg; }
    public Integer getOcupacaoAtualPct() { return ocupacaoAtualPct; }
    public void setOcupacaoAtualPct(Integer ocupacaoAtualPct) { this.ocupacaoAtualPct = ocupacaoAtualPct; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
