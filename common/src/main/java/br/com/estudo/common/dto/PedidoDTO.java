package br.com.estudo.common.dto;

import br.com.estudo.common.enums.StatusOrquestrador;
import java.math.BigDecimal;

public class PedidoDTO {

    private String idPedido;
    private Integer idUsuario;
    private Integer idProduto;
    private String descricao;
    private BigDecimal valor;
    private StatusOrquestrador status;

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public StatusOrquestrador getStatus() {
        return status;
    }

    public void setStatus(StatusOrquestrador status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PedidoDTO{" +
                "idPedido='" + idPedido + '\'' +
                ", idUsuario=" + idUsuario +
                ", idProduto=" + idProduto +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", status=" + status +
                '}';
    }
}
