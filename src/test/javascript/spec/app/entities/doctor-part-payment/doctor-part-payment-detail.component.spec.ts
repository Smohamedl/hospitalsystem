import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { DoctorPartPaymentDetailComponent } from 'app/entities/doctor-part-payment/doctor-part-payment-detail.component';
import { DoctorPartPayment } from 'app/shared/model/doctor-part-payment.model';

describe('Component Tests', () => {
  describe('DoctorPartPayment Management Detail Component', () => {
    let comp: DoctorPartPaymentDetailComponent;
    let fixture: ComponentFixture<DoctorPartPaymentDetailComponent>;
    const route = ({ data: of({ doctorPartPayment: new DoctorPartPayment(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [DoctorPartPaymentDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DoctorPartPaymentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DoctorPartPaymentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.doctorPartPayment).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
