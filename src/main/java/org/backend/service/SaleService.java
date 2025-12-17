package org.backend.service;

import org.backend.dto.SaleItemRequest;
import org.backend.dto.SaleRequest;
import org.backend.dto.SaleReturnRequest;
import org.backend.model.Sale;
import org.backend.model.SaleItem;
import org.backend.model.SaleReturn;
import org.backend.repository.SaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleService {

    private final SaleRepository repo;

    public SaleService(SaleRepository repo) { this.repo = repo; }

    public List<Sale> list() { return repo.findAll(); }

    public Sale get(Long id) { return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venda não encontrada")); }

    @Transactional
    public Sale createFromRequest(SaleRequest req) {
        Sale s = new Sale();
        s.setDate(LocalDateTime.now());
        s.setClientId(req.getClientId());
        s.setClientName(req.getClientName());
        s.setOrigem(req.getOrigem());
        s.setExecucaoId(req.getExecucaoId());
        s.setPaymentMethod(req.getPaymentMethod());
        s.setPaymentStatus(req.getPaymentStatus());
        s.setObservacoes(req.getObservacoes());
        List<SaleItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        if (req.getItems() != null) {
            for (SaleItemRequest ir : req.getItems()) {
                SaleItem it = new SaleItem();
                it.setNome(ir.getNome());
                it.setQtd(ir.getQtd());
                it.setPreco(ir.getPreco() == null ? BigDecimal.ZERO : ir.getPreco());
                it.setDesconto(ir.getDesconto() == null ? BigDecimal.ZERO : ir.getDesconto());
                it.setBrinde(ir.getBrinde() != null ? ir.getBrinde() : false);
                items.add(it);
                BigDecimal qtd = BigDecimal.valueOf(it.getQtd() != null ? it.getQtd() : 0);
                BigDecimal line = it.getPreco().multiply(qtd).subtract(it.getDesconto() == null ? BigDecimal.ZERO : it.getDesconto());
                total = total.add(line);
            }
        }
        s.setItems(items);
        s.setTotal(total);
        return repo.save(s);
    }

    @Transactional
    public Sale registerReturn(Long saleId, SaleReturnRequest req) {
        Sale s = get(saleId);
        if (req == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requisição inválida");
        Integer idx = req.getItemIndex();
        if (idx == null || idx < 0 || idx >= s.getItems().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item inválido para devolução");
        }
        if (req.getQtd() == null || req.getQtd() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade de devolução inválida");
        }
        SaleReturn r = new SaleReturn();
        r.setItemIndex(idx);
        r.setQtd(req.getQtd());
        r.setMotivo(req.getMotivo());
        r.setData(LocalDateTime.now());
        List<SaleReturn> existing = s.getReturns();
        if (existing == null) existing = new ArrayList<>();
        existing.add(r);
        s.setReturns(existing);
        return repo.save(s);
    }
}
