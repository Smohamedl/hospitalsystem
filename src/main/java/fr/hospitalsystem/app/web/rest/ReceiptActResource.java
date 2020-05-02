package fr.hospitalsystem.app.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import fr.hospitalsystem.app.domain.Act;
import fr.hospitalsystem.app.domain.Actype;
import fr.hospitalsystem.app.domain.DoctorPartPayment;
import fr.hospitalsystem.app.domain.ReceiptAct;
import fr.hospitalsystem.app.repository.ActRepository;
import fr.hospitalsystem.app.repository.DoctorPartPaymentRepository;
import fr.hospitalsystem.app.repository.PatientRepository;
import fr.hospitalsystem.app.repository.ReceiptActRepository;
import fr.hospitalsystem.app.repository.search.ReceiptActSearchRepository;
import fr.hospitalsystem.app.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.hospitalsystem.app.domain.ReceiptAct}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ReceiptActResource {

    private final Logger log = LoggerFactory.getLogger(ReceiptActResource.class);

    private static final String ENTITY_NAME = "receiptAct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReceiptActRepository receiptActRepository;

    private final ReceiptActSearchRepository receiptActSearchRepository;

    private final PatientRepository patientRepository;

    private final ActRepository actRepository;

    private final DoctorPartPaymentRepository doctorPartPaymentRepository;

    public ReceiptActResource(ReceiptActRepository receiptActRepository, ReceiptActSearchRepository receiptActSearchRepository,
            PatientRepository patientRepository, ActRepository actRepository, DoctorPartPaymentRepository doctorPartPaymentRepository) {
        this.receiptActRepository = receiptActRepository;
        this.receiptActSearchRepository = receiptActSearchRepository;
        this.patientRepository = patientRepository;
        this.actRepository = actRepository;
        this.doctorPartPaymentRepository = doctorPartPaymentRepository;
    }

    /**
     * {@code POST  /receipt-acts} : Create a new receiptAct.
     *
     * @param receiptAct the receiptAct to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new receiptAct, or with status {@code 400 (Bad Request)}
     *         if the receiptAct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    // @PostMapping("/receipt-acts")
    public ResponseEntity<ReceiptAct> createReceiptAct(@RequestBody ReceiptAct receiptAct) throws URISyntaxException {
        log.debug("REST request to save ReceiptAct : {}", receiptAct);
        if (receiptAct.getId() != null) {
            throw new BadRequestAlertException("A new receiptAct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReceiptAct result = receiptActRepository.save(receiptAct);
        receiptActSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/receipt-acts/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * {@code PUT  /receipt-acts} : Updates an existing receiptAct.
     *
     * @param receiptAct the receiptAct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated receiptAct, or with status {@code 400 (Bad Request)}
     *         if the receiptAct is not valid, or with status {@code 500 (Internal Server Error)} if the receiptAct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/receipt-acts")
    public ResponseEntity<ReceiptAct> updateReceiptAct(@RequestBody ReceiptAct receiptAct) throws URISyntaxException {
        log.debug("REST request to update ReceiptAct : {}", receiptAct);
        if (receiptAct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        ReceiptAct oldResult = receiptActRepository.findById(receiptAct.getId()).get();
        Boolean oldReceiptIsPaidDoctor = oldResult.isPaidDoctor();
        receiptAct.setAct(oldResult.getAct());
        ReceiptAct result = receiptActRepository.save(receiptAct);

        /** Update doctor month total if paid is set to true for the first time */
        if (!oldReceiptIsPaidDoctor && result.isPaidDoctor()) {
            LocalDate localDate = LocalDate.now().withDayOfMonth(1);
            DoctorPartPayment doctorPartPayment = this.doctorPartPaymentRepository.findOneByDate(localDate, result.getAct().getDoctor()).get();
            doctorPartPayment.setTotal(doctorPartPayment.getTotal() + result.getTotal());

            this.doctorPartPaymentRepository.save(doctorPartPayment);
        }

        receiptActSearchRepository.save(result);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, receiptAct.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /receipt-acts} : get all the receiptActs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of receiptActs in body.
     */
    @GetMapping("/receipt-acts")
    public ResponseEntity<List<ReceiptAct>> getAllReceiptActs(Pageable pageable) {
        log.debug("REST request to get a page of ReceiptActs");
        Page<ReceiptAct> page = receiptActRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /receipt-acts/:id} : get the "id" receiptAct.
     *
     * @param id the id of the receiptAct to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the receiptAct, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/receipt-acts/{id}")
    public ResponseEntity<ReceiptAct> getReceiptAct(@PathVariable Long id) {
        log.debug("REST request to get ReceiptAct : {}", id);
        Optional<ReceiptAct> receiptAct = receiptActRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(receiptAct);
    }

    /**
     * {@code DELETE  /receipt-acts/:id} : delete the "id" receiptAct.
     *
     * @param id the id of the receiptAct to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/receipt-acts/{id}")
    public ResponseEntity<Void> deleteReceiptAct(@PathVariable Long id) {
        log.debug("REST request to delete ReceiptAct : {}", id);

        ReceiptAct receiptAct = receiptActRepository.findById(id).get();

        if (!receiptAct.isPaid() && !receiptAct.isPaidDoctor()) {
            receiptActRepository.deleteById(id);
            receiptActSearchRepository.deleteById(id);
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/receipt-acts?query=:query} : search for the receiptAct corresponding to the query.
     *
     * @param query    the query of the receiptAct search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/receipt-acts")
    public ResponseEntity<List<ReceiptAct>> searchReceiptActs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ReceiptActs for query {}", query);
        Page<ReceiptAct> page = receiptActSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/receipt-acts/doctor/{service}")
    public ResponseEntity<List<ReceiptAct>> getReceiptActsByDoctor(Pageable pageable, @PathVariable String service) {
        log.debug("REST request to get a page of ReceiptActs");
        Page<ReceiptAct> page = receiptActRepository.findByService(service, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/_search/receipt-acts/doctor/{service}")
    public ResponseEntity<List<ReceiptAct>> searchReceiptActsByDoctor(@RequestParam String query, Pageable pageable, @PathVariable String service) {
        log.debug("REST request to get a page of ReceiptActs");
        Page<ReceiptAct> page = receiptActRepository.findByService(service, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping(path = "/receipt-acts/download/{id}", produces = "application/pdf")
    public @ResponseBody Resource ReceiptPDF(@PathVariable long id) throws URISyntaxException, Exception {
        ReceiptAct receiptAct = this.receiptActRepository.findById(id).get();

        Act act = actRepository.findByReceiptId(id).get();

        // Act act = receiptAct.getAct();

        log.debug("REST request to download receipt by id : {}", id);
        if (act == null) {
            throw new BadRequestAlertException("Cannot generate pdf receiptAct.act is null", ENTITY_NAME, "actmissing");
        }

        final String temperotyFilePath = System.getProperty("java.io.tmpdir");
        String fileName = "recu.pdf";

        createPDF(temperotyFilePath + "\\" + fileName, act);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos = convertPDFToByteArrayOutputStream(temperotyFilePath + "\\" + fileName);

        /*
         * HttpHeaders headers = new HttpHeaders(); headers.setContentType(MediaType.APPLICATION_PDF);
         * 
         * OutputStream os = response.getOutputStream(); baos.writeTo(os); os.flush();
         */

        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(baos.toByteArray()));

        return resource;
        /*
         * return ResponseEntity.created(new URI("/api/acts/" + result.getId())) .headers(HeaderUtil.createEntityCreationAlert(applicationName, true,
         * ENTITY_NAME, result.getId().toString())) .body(baos.toByteArray());
         */
    }

    public Document createPDF(String file, Act act) {

        Document document = null;

        try {
            document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

            // =====================
            // contenu du recu
            String content = "HospitalSystem \n ";
            content += new Date() + "\n";
            if (act.getPatientName() != null)
                content += "Patient : " + act.getPatientName() + "\n";
            for (Actype actype : act.getActypes()) {
                content += actype.getName() + " : " + actype.getPrice() + " UM" + "\n";
            }
            content += "\nTotal : " + act.getReceiptAct().getTotal();
            // =======================

            Paragraph paragraph = new Paragraph(content);
            document.add(paragraph);

            document.close();

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return document;

    }

    private ByteArrayOutputStream convertPDFToByteArrayOutputStream(String fileName) {

        InputStream inputStream = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {

            inputStream = new FileInputStream(fileName);
            byte[] buffer = new byte[1024];
            baos = new ByteArrayOutputStream();

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return baos;
    }

}
