import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { ReceiptActDetailComponent } from 'app/entities/receipt-act/receipt-act-detail.component';
import { ReceiptAct } from 'app/shared/model/receipt-act.model';

describe('Component Tests', () => {
  describe('ReceiptAct Management Detail Component', () => {
    let comp: ReceiptActDetailComponent;
    let fixture: ComponentFixture<ReceiptActDetailComponent>;
    const route = ({ data: of({ receiptAct: new ReceiptAct(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [ReceiptActDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ReceiptActDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ReceiptActDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.receiptAct).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
