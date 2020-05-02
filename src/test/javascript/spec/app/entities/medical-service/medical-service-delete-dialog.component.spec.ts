import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { HospitalsystemTestModule } from '../../../test.module';
import { MedicalServiceDeleteDialogComponent } from 'app/entities/medical-service/medical-service-delete-dialog.component';
import { MedicalServiceService } from 'app/entities/medical-service/medical-service.service';

describe('Component Tests', () => {
  describe('MedicalService Management Delete Component', () => {
    let comp: MedicalServiceDeleteDialogComponent;
    let fixture: ComponentFixture<MedicalServiceDeleteDialogComponent>;
    let service: MedicalServiceService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [MedicalServiceDeleteDialogComponent]
      })
        .overrideTemplate(MedicalServiceDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MedicalServiceDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MedicalServiceService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
