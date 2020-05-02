package fr.hospitalsystem.app.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.hospitalsystem.app.domain.FixedDoctorPayment;
import fr.hospitalsystem.app.repository.DoctorRepository;
import fr.hospitalsystem.app.repository.FixedDoctorPaymentRepository;
import fr.hospitalsystem.app.repository.search.FixedDoctorPaymentSearchRepository;

/**
 * Service Implementation for managing {@link FixedDoctorPayment}.
 */
@Service
@Transactional
public class FixedDoctorPaymentService {

    private final Logger log = LoggerFactory.getLogger(FixedDoctorPaymentService.class);

    private final FixedDoctorPaymentRepository fixedDoctorPaymentRepository;

    private final FixedDoctorPaymentSearchRepository fixedDoctorPaymentSearchRepository;

    private final DoctorRepository doctorRepository;

    public FixedDoctorPaymentService(FixedDoctorPaymentRepository fixedDoctorPaymentRepository,
            FixedDoctorPaymentSearchRepository fixedDoctorPaymentSearchRepository, DoctorRepository doctorRepository) {
        this.fixedDoctorPaymentRepository = fixedDoctorPaymentRepository;
        this.fixedDoctorPaymentSearchRepository = fixedDoctorPaymentSearchRepository;
        this.doctorRepository = doctorRepository;
    }

    /**
     * Save a fixedDoctorPayment.
     *
     * @param fixedDoctorPayment the entity to save.
     * @return the persisted entity.
     */
    public FixedDoctorPayment save(FixedDoctorPayment fixedDoctorPayment) {
        log.debug("Request to save FixedDoctorPayment : {}", fixedDoctorPayment);
        FixedDoctorPayment result = fixedDoctorPaymentRepository.save(fixedDoctorPayment);
        fixedDoctorPaymentSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the fixedDoctorPayments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FixedDoctorPayment> findAll(Pageable pageable) {
        log.debug("Request to get all FixedDoctorPayments");
        return fixedDoctorPaymentRepository.findAll(pageable);
    }

    /**
     * Get one fixedDoctorPayment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FixedDoctorPayment> findOne(Long id) {
        log.debug("Request to get FixedDoctorPayment : {}", id);
        return fixedDoctorPaymentRepository.findById(id);
    }

    /**
     * Delete the fixedDoctorPayment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FixedDoctorPayment : {}", id);
        fixedDoctorPaymentRepository.deleteById(id);
        fixedDoctorPaymentSearchRepository.deleteById(id);
    }

    /**
     * Search for the fixedDoctorPayment corresponding to the query.
     *
     * @param query    the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FixedDoctorPayment> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FixedDoctorPayments for query {}", query);
        return fixedDoctorPaymentSearchRepository.search(queryStringQuery(query), pageable);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */

}
