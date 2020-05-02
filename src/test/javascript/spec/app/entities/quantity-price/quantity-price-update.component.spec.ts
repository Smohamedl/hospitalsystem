import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { QuantityPriceUpdateComponent } from 'app/entities/quantity-price/quantity-price-update.component';
import { QuantityPriceService } from 'app/entities/quantity-price/quantity-price.service';
import { QuantityPrice } from 'app/shared/model/quantity-price.model';

describe('Component Tests', () => {
  describe('QuantityPrice Management Update Component', () => {
    let comp: QuantityPriceUpdateComponent;
    let fixture: ComponentFixture<QuantityPriceUpdateComponent>;
    let service: QuantityPriceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [QuantityPriceUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(QuantityPriceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QuantityPriceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(QuantityPriceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new QuantityPrice(123);
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
        const entity = new QuantityPrice();
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
