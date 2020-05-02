package fr.hospitalsystem.app.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.hospitalsystem.app.domain.Doctor;
import fr.hospitalsystem.app.domain.DoctorPartPayment;
import fr.hospitalsystem.app.repository.DoctorPartPaymentRepository;
import fr.hospitalsystem.app.repository.DoctorRepository;
import fr.hospitalsystem.app.repository.search.DoctorPartPaymentSearchRepository;

/**
 * Service Implementation for managing {@link DoctorPartPayment}.
 */
@Service
@Transactional
public class DoctorPartPaymentService {

    private final Logger log = LoggerFactory.getLogger(DoctorPartPaymentService.class);

    private final DoctorPartPaymentRepository doctorPartPaymentRepository;

    private final DoctorPartPaymentSearchRepository doctorPartPaymentSearchRepository;

    private final DoctorRepository doctorRepository;

    public DoctorPartPaymentService(DoctorPartPaymentRepository doctorPartPaymentRepository,
            DoctorPartPaymentSearchRepository doctorPartPaymentSearchRepository, DoctorRepository doctorRepository) {
        this.doctorPartPaymentRepository = doctorPartPaymentRepository;
        this.doctorPartPaymentSearchRepository = doctorPartPaymentSearchRepository;
        this.doctorRepository = doctorRepository;
    }

    /**
     * Save a doctorPartPayment.
     *
     * @param doctorPartPayment the entity to save.
     * @return the persisted entity.
     */
    public DoctorPartPayment save(DoctorPartPayment doctorPartPayment) {
        log.debug("Request to save DoctorPartPayment : {}", doctorPartPayment);
        DoctorPartPayment result = doctorPartPaymentRepository.save(doctorPartPayment);
        doctorPartPaymentSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the doctorPartPayments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DoctorPartPayment> findAll(Pageable pageable) {
        log.debug("Request to get all DoctorPartPayments");
        return doctorPartPaymentRepository.findAll(pageable);
    }

    /**
     * Get one doctorPartPayment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DoctorPartPayment> findOne(Long id) {
        log.debug("Request to get DoctorPartPayment : {}", id);
        return doctorPartPaymentRepository.findById(id);
    }

    /**
     * Delete the doctorPartPayment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DoctorPartPayment : {}", id);
        doctorPartPaymentRepository.deleteById(id);
        doctorPartPaymentSearchRepository.deleteById(id);
    }

    /**
     * Search for the doctorPartPayment corresponding to the query.
     *
     * @param query    the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DoctorPartPayment> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DoctorPartPayments for query {}", query);
        return doctorPartPaymentSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Scheduled(cron = "5 * * * * ?")
    public void DoctorPartPaymentMonthlyCreation() {
        log.debug("Cron to create fixedPaymentDoctor every first of every month");
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DAY_OF_MONTH, 1);
        Date firstOfMonth = date.getTime();

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = formatter.format(firstOfMonth);

        log.debug("############ date " + strDate);

        List<Doctor> doctors = doctorRepository.findAll();

        for (Doctor doctor : doctors) {
            if (doctor.isPartOfHospitalIncome()) {
                LocalDate localDate = LocalDate.now().withDayOfMonth(1);
                Optional<DoctorPartPayment> OptDoctorPartPayment = this.doctorPartPaymentRepository.findOneByDate(localDate, doctor);

                if (OptDoctorPartPayment.isPresent()) {
                    continue;
                }

                DoctorPartPayment doctorPartPayment = new DoctorPartPayment();
                doctorPartPayment.setDate(localDate);
                doctorPartPayment.setDoctor(doctor);
                doctorPartPayment.setTotal(0.);
                this.doctorPartPaymentRepository.save(doctorPartPayment);
            }
        }
    }
}
