package fr.hospitalsystem.app.repository.search;

import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;

import fr.hospitalsystem.app.domain.ReceiptAct;

/**
 * Spring Data Elasticsearch repository for the {@link ReceiptAct} entity.
 */
public interface ReceiptActSearchRepository extends ElasticsearchRepository<ReceiptAct, Long> {
    /*
     * @Query("select a.receiptAct from Act a where a.doctor.name = ?1") Page<ReceiptAct> searchByDoctor(String doctor, QueryBuilder query, Pageable
     * pageable);
     */
}
