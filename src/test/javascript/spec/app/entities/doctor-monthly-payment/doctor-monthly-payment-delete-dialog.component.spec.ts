import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { HospitalsystemTestModule } from '../../../test.module';
import { DoctorMonthlyPaymentDeleteDialogComponent } from 'app/entities/doctor-monthly-payment/doctor-monthly-payment-delete-dialog.component';
import { DoctorMonthlyPaymentService } from 'app/entities/doctor-monthly-payment/doctor-monthly-payment.service';

describe('Component Tests', () => {
  describe('DoctorMonthlyPayment Management Delete Component', () => {
    let comp: DoctorMonthlyPaymentDeleteDialogComponent;
    let fixture: ComponentFixture<DoctorMonthlyPaymentDeleteDialogComponent>;
    let service: DoctorMonthlyPaymentService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [DoctorMonthlyPaymentDeleteDialogComponent]
      })
        .overrideTemplate(DoctorMonthlyPaymentDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DoctorMonthlyPaymentDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DoctorMonthlyPaymentService);
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
