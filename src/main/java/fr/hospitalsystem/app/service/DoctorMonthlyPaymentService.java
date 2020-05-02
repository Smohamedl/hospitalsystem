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
import fr.hospitalsystem.app.domain.DoctorMonthlyPayment;
import fr.hospitalsystem.app.repository.DoctorMonthlyPaymentRepository;
import fr.hospitalsystem.app.repository.DoctorRepository;
import fr.hospitalsystem.app.repository.search.DoctorMonthlyPaymentSearchRepository;

/**
 * Service Implementation for managing {@link DoctorMonthlyPayment}.
 */
@Service
@Transactional
public class DoctorMonthlyPaymentService {

    private final Logger log = LoggerFactory.getLogger(DoctorMonthlyPaymentService.class);

    private final DoctorMonthlyPaymentRepository doctorMonthlyPaymentRepository;

    private final DoctorMonthlyPaymentSearchRepository doctorMonthlyPaymentSearchRepository;

    private final DoctorRepository doctorRepository;

    public DoctorMonthlyPaymentService(DoctorMonthlyPaymentRepository doctorMonthlyPaymentRepository,
            DoctorMonthlyPaymentSearchRepository doctorMonthlyPaymentSearchRepository, DoctorRepository doctorRepository) {
        this.doctorMonthlyPaymentRepository = doctorMonthlyPaymentRepository;
        this.doctorMonthlyPaymentSearchRepository = doctorMonthlyPaymentSearchRepository;
        this.doctorRepository = doctorRepository;
    }

    /**
     * Save a doctorMonthlyPayment.
     *
     * @param doctorMonthlyPayment the entity to save.
     * @return the persisted entity.
     */
    public DoctorMonthlyPayment save(DoctorMonthlyPayment doctorMonthlyPayment) {
        log.debug("Request to save DoctorMonthlyPayment : {}", doctorMonthlyPayment);
        DoctorMonthlyPayment result = doctorMonthlyPaymentRepository.save(doctorMonthlyPayment);
        doctorMonthlyPaymentSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the doctorMonthlyPayments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DoctorMonthlyPayment> findAll(Pageable pageable) {
        log.debug("Request to get all DoctorMonthlyPayments");
        return doctorMonthlyPaymentRepository.findAll(pageable);
    }

    /**
     * Get one doctorMonthlyPayment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DoctorMonthlyPayment> findOne(Long id) {
        log.debug("Request to get DoctorMonthlyPayment : {}", id);
        return doctorMonthlyPaymentRepository.findById(id);
    }

    /**
     * Delete the doctorMonthlyPayment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DoctorMonthlyPayment : {}", id);
        doctorMonthlyPaymentRepository.deleteById(id);
        doctorMonthlyPaymentSearchRepository.deleteById(id);
    }

    /**
     * Search for the doctorMonthlyPayment corresponding to the query.
     *
     * @param query    the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DoctorMonthlyPayment> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DoctorMonthlyPayments for query {}", query);
        return doctorMonthlyPaymentSearchRepository.search(queryStringQuery(query), pageable);
    }

    // @Scheduled(cron = "[Seconds] [Minutes] [Hours] [Day of month] [Month] [Day of week] [Year]")
    // 1 1 0 1 * ?
    @Scheduled(cron = "5 30 * * * ?")
    public void removeNotActivatedUsers() {
        log.debug("Cron to create fixedPaymentDoctor every first of every month");
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DAY_OF_MONTH, 1);
        Date firstOfMonth = date.getTime();
        // 2020-03-31
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = formatter.format(firstOfMonth);

        log.debug("############ date " + strDate);

        List<Doctor> doctors = doctorRepository.findAll();

        for (Doctor doctor : doctors) {
            if (doctor.getSalary() > 0) {
                LocalDate localDate = LocalDate.now().withDayOfMonth(1);
                Optional<DoctorMonthlyPayment> OptDoctorMonthlyPayment = this.doctorMonthlyPaymentRepository.findOneByDate(localDate, doctor);

                if (OptDoctorMonthlyPayment.isPresent()) {
                    continue;
                }

                DoctorMonthlyPayment doctorMonthlyPayment = new DoctorMonthlyPayment();
                doctorMonthlyPayment.setDate(localDate);
                doctorMonthlyPayment.setDoctor(doctor);
                doctorMonthlyPayment.setPaid(false);
                this.doctorMonthlyPaymentRepository.save(doctorMonthlyPayment);
            }
        }
    }
}
