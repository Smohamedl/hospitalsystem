import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { DoctorPartPaymentUpdateComponent } from 'app/entities/doctor-part-payment/doctor-part-payment-update.component';
import { DoctorPartPaymentService } from 'app/entities/doctor-part-payment/doctor-part-payment.service';
import { DoctorPartPayment } from 'app/shared/model/doctor-part-payment.model';

describe('Component Tests', () => {
  describe('DoctorPartPayment Management Update Component', () => {
    let comp: DoctorPartPaymentUpdateComponent;
    let fixture: ComponentFixture<DoctorPartPaymentUpdateComponent>;
    let service: DoctorPartPaymentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [DoctorPartPaymentUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DoctorPartPaymentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DoctorPartPaymentUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DoctorPartPaymentService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new DoctorPartPayment(123);
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
        const entity = new DoctorPartPayment();
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
