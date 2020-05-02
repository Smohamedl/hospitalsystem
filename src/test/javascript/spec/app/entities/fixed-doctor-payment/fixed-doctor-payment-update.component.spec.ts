import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { FixedDoctorPaymentUpdateComponent } from 'app/entities/fixed-doctor-payment/fixed-doctor-payment-update.component';
import { FixedDoctorPaymentService } from 'app/entities/fixed-doctor-payment/fixed-doctor-payment.service';
import { FixedDoctorPayment } from 'app/shared/model/fixed-doctor-payment.model';

describe('Component Tests', () => {
  describe('FixedDoctorPayment Management Update Component', () => {
    let comp: FixedDoctorPaymentUpdateComponent;
    let fixture: ComponentFixture<FixedDoctorPaymentUpdateComponent>;
    let service: FixedDoctorPaymentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [FixedDoctorPaymentUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(FixedDoctorPaymentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FixedDoctorPaymentUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FixedDoctorPaymentService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FixedDoctorPayment(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new FixedDoctorPayment();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
