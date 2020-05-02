import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { DoctorMonthlyPaymentDetailComponent } from 'app/entities/doctor-monthly-payment/doctor-monthly-payment-detail.component';
import { DoctorMonthlyPayment } from 'app/shared/model/doctor-monthly-payment.model';

describe('Component Tests', () => {
  describe('DoctorMonthlyPayment Management Detail Component', () => {
    let comp: DoctorMonthlyPaymentDetailComponent;
    let fixture: ComponentFixture<DoctorMonthlyPaymentDetailComponent>;
    const route = ({ data: of({ doctorMonthlyPayment: new DoctorMonthlyPayment(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [DoctorMonthlyPaymentDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DoctorMonthlyPaymentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DoctorMonthlyPaymentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.doctorMonthlyPayment).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
