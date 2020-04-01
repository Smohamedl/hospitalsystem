import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { ReceiptActUpdateComponent } from 'app/entities/receipt-act/receipt-act-update.component';
import { ReceiptActService } from 'app/entities/receipt-act/receipt-act.service';
import { ReceiptAct } from 'app/shared/model/receipt-act.model';

describe('Component Tests', () => {
  describe('ReceiptAct Management Update Component', () => {
    let comp: ReceiptActUpdateComponent;
    let fixture: ComponentFixture<ReceiptActUpdateComponent>;
    let service: ReceiptActService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [ReceiptActUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ReceiptActUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReceiptActUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ReceiptActService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ReceiptAct(123);
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
        const entity = new ReceiptAct();
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
