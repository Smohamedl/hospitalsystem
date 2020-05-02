import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { FixedDoctorPaymentDetailComponent } from 'app/entities/fixed-doctor-payment/fixed-doctor-payment-detail.component';
import { FixedDoctorPayment } from 'app/shared/model/fixed-doctor-payment.model';

describe('Component Tests', () => {
  describe('FixedDoctorPayment Management Detail Component', () => {
    let comp: FixedDoctorPaymentDetailComponent;
    let fixture: ComponentFixture<FixedDoctorPaymentDetailComponent>;
    const route = ({ data: of({ fixedDoctorPayment: new FixedDoctorPayment(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [FixedDoctorPaymentDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(FixedDoctorPaymentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FixedDoctorPaymentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fixedDoctorPayment).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
