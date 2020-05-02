import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { DoctorMonthlyPaymentUpdateComponent } from 'app/entities/doctor-monthly-payment/doctor-monthly-payment-update.component';
import { DoctorMonthlyPaymentService } from 'app/entities/doctor-monthly-payment/doctor-monthly-payment.service';
import { DoctorMonthlyPayment } from 'app/shared/model/doctor-monthly-payment.model';

describe('Component Tests', () => {
  describe('DoctorMonthlyPayment Management Update Component', () => {
    let comp: DoctorMonthlyPaymentUpdateComponent;
    let fixture: ComponentFixture<DoctorMonthlyPaymentUpdateComponent>;
    let service: DoctorMonthlyPaymentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [DoctorMonthlyPaymentUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DoctorMonthlyPaymentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DoctorMonthlyPaymentUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DoctorMonthlyPaymentService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new DoctorMonthlyPayment(123);
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
        const entity = new DoctorMonthlyPayment();
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
