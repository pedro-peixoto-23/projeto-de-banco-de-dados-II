package ifpb.modelo.ingresso;

import ifpb.modelo.pessoa.Espectador;
import ifpb.modelo.proposta_de_aluguel.PropostaDeAluguel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor()

@Table(name = "tb_venda_de_ingresso")
@Entity
public class VendaDeIngresso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "espectador_id", nullable = false)
    private Espectador espectador;

    @ManyToOne
    @JoinColumn(name = "proposta_id", nullable = false)
    private PropostaDeAluguel propostaDeAluguel;

    @Column(nullable = false)
    private LocalDate dataDaPeca;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorUnitario;

    @OneToMany(mappedBy = "vendaDeIngresso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingresso> ingressos = new ArrayList<>();

    public VendaDeIngresso(Espectador espectador, PropostaDeAluguel propostaDeAluguel, LocalDate dataDaPeca, int quantidade, BigDecimal valorUnitario) {
        this.espectador = espectador;
        this.propostaDeAluguel = propostaDeAluguel;
        this.dataDaPeca = dataDaPeca;
        this.valorUnitario = valorUnitario;

        gerarIngressos(quantidade);
    }

    private void gerarIngressos(int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            Ingresso ingresso = new Ingresso(this);
            ingressos.add(ingresso);
        }
    }

    public int getQuantidade() {
        return ingressos.size();
    }

    public BigDecimal calcularValorTotal() {
        return valorUnitario.multiply(BigDecimal.valueOf(getQuantidade())).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof VendaDeIngresso)) {
            return false;
        }

        VendaDeIngresso vendaDeIngresso = (VendaDeIngresso) object;

        return id != null && id.equals(vendaDeIngresso.getId());
    }

    @Override
    public int hashCode() {
        if (id != null) return id.hashCode();
        return 0;
    }
}
