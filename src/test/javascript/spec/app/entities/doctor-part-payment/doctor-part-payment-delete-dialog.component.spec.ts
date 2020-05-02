import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { HospitalsystemTestModule } from '../../../test.module';
import { DoctorPartPaymentDeleteDialogComponent } from 'app/entities/doctor-part-payment/doctor-part-payment-delete-dialog.component';
import { DoctorPartPaymentService } from 'app/entities/doctor-part-payment/doctor-part-payment.service';

describe('Component Tests', () => {
  describe('DoctorPartPayment Management Delete Component', () => {
    let comp: DoctorPartPaymentDeleteDialogComponent;
    let fixture: ComponentFixture<DoctorPartPaymentDeleteDialogComponent>;
    let service: DoctorPartPaymentService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [DoctorPartPaymentDeleteDialogComponent]
      })
        .overrideTemplate(DoctorPartPaymentDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DoctorPartPaymentDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DoctorPartPaymentService);
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
