import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { QuantityPriceDetailComponent } from 'app/entities/quantity-price/quantity-price-detail.component';
import { QuantityPrice } from 'app/shared/model/quantity-price.model';

describe('Component Tests', () => {
  describe('QuantityPrice Management Detail Component', () => {
    let comp: QuantityPriceDetailComponent;
    let fixture: ComponentFixture<QuantityPriceDetailComponent>;
    const route = ({ data: of({ quantityPrice: new QuantityPrice(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [QuantityPriceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(QuantityPriceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(QuantityPriceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.quantityPrice).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
