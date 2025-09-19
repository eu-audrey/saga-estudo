package br.com.estudo.common.dto;

import java.math.BigDecimal;

public class OrquestradorRequestDTO {

    private Integer idUsuario;
    private Integer idProduto;
    private String descricao;
    private BigDecimal valor;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "OrquestradorRequestDTO{" +
                "idUsuario=" + idUsuario +
                ", idProduto=" + idProduto +
                ", descricao=" + descricao +
                ", valor=" + valor +
                '}';
    }
}
