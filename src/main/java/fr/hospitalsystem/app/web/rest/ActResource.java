package fr.hospitalsystem.app.web.rest;

import fr.hospitalsystem.app.domain.Act;
import fr.hospitalsystem.app.domain.Actype;
import fr.hospitalsystem.app.domain.Patient;
import fr.hospitalsystem.app.repository.PatientRepository;
import fr.hospitalsystem.app.service.ActService;
import fr.hospitalsystem.app.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.qrcode.ByteArray;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

/**
 * REST controller for managing {@link fr.hospitalsystem.app.domain.Act}.
 */
@RestController
@RequestMapping("/api")
public class ActResource {

    private final Logger log = LoggerFactory.getLogger(ActResource.class);

    private static final String ENTITY_NAME = "act";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActService actService;
    
    @Autowired
    private PatientRepository patientRepo;

    public ActResource(ActService actService) {
        this.actService = actService;
    }

    /**
     * {@code POST  /acts} : Create a new act.
     *
     * @param act the act to create.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws Exception 
     */
    @PostMapping(path="/acts", produces = "application/pdf")
    public @ResponseBody Resource  createAct(@Valid @RequestBody Act act) throws URISyntaxException, Exception {
        log.debug("REST request to save Act : {}", act);
        if (act.getId() != null) {
            throw new BadRequestAlertException("A new act cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        // when act is not specified get it by its name
        if(act.getPatient() == null && act.getPatientName().contains("-")) {
        	String patientTel = act.getPatientName().split("-")[1].trim();
        	List<Patient> patients = patientRepo.findByTel(patientTel);
        	
        	// TODO save the patient with the right full name not the first!
        	if(!patients.isEmpty()) {
        		act.setPatient(patients.get(0));
        	}
        }
        
        Act result = actService.save(act);
        
        final String temperotyFilePath = System.getProperty("java.io.tmpdir");
        String fileName = "recu.pdf";

        createPDF(temperotyFilePath+"\\"+fileName, act);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos = convertPDFToByteArrayOutputStream(temperotyFilePath+"\\"+fileName);
        
		/*
		 * HttpHeaders headers = new HttpHeaders();
		 * headers.setContentType(MediaType.APPLICATION_PDF);
		 * 
		 * OutputStream os = response.getOutputStream(); baos.writeTo(os); os.flush();
		 */
		 
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(baos.toByteArray()));
        
        return resource;
		/*
		 * return ResponseEntity.created(new URI("/api/acts/" + result.getId()))
		 * .headers(HeaderUtil.createEntityCreationAlert(applicationName, true,
		 * ENTITY_NAME, result.getId().toString())) .body(baos.toByteArray());
		 */
    }

    /**
     * {@code PUT  /acts} : Updates an existing act.
     *
     * @param act the act to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated act,
     * or with status {@code 400 (Bad Request)} if the act is not valid,
     * or with status {@code 500 (Internal Server Error)} if the act couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/acts")
    public ResponseEntity<Act> updateAct(@Valid @RequestBody Act act) throws URISyntaxException {
        log.debug("REST request to update Act : {}", act);
        if (act.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Act result = actService.save(act);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, act.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /acts} : get all the acts.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of acts in body.
     */
    @GetMapping("/acts")
    public ResponseEntity<List<Act>> getAllActs(Pageable pageable) {
        log.debug("REST request to get a page of Acts");
        Page<Act> page = actService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /acts/:id} : get the "id" act.
     *
     * @param id the id of the act to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the act, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/acts/{id}")
    public ResponseEntity<Act> getAct(@PathVariable Long id) {
        log.debug("REST request to get Act : {}", id);
        Optional<Act> act = actService.findOne(id);
        return ResponseUtil.wrapOrNotFound(act);
    }

    /**
     * {@code DELETE  /acts/:id} : delete the "id" act.
     *
     * @param id the id of the act to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/acts/{id}")
    public ResponseEntity<Void> deleteAct(@PathVariable Long id) {
        log.debug("REST request to delete Act : {}", id);
        actService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/acts?query=:query} : search for the act corresponding
     * to the query.
     *
     * @param query the query of the act search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/acts")
    public ResponseEntity<List<Act>> searchActs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Acts for query {}", query);
        Page<Act> page = actService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
	
	  
    public Document createPDF(String file, Act act) {
	  
	  Document document = null;
	  
	  try { document = new Document(); PdfWriter.getInstance(document, new
	  FileOutputStream(file));
	  
	  document.open(); Font font = FontFactory.getFont(FontFactory.COURIER, 16,
	  BaseColor.BLACK);
	  
	  String content = "HospitalSystem" + "\n " + new Date() + "\n --------------------------------------\n" 
	  + act.getPatientName() + " " + act.getActype().getName() ;
	  
	  
	  Paragraph paragraph = new Paragraph(content); document.add(paragraph);
	  
	  document.close();
	  
	  } catch (FileNotFoundException e) {
	  
	  e.printStackTrace(); } catch (DocumentException e) { e.printStackTrace(); }
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
