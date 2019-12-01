import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { ActypeUpdateComponent } from 'app/entities/actype/actype-update.component';
import { ActypeService } from 'app/entities/actype/actype.service';
import { Actype } from 'app/shared/model/actype.model';

describe('Component Tests', () => {
  describe('Actype Management Update Component', () => {
    let comp: ActypeUpdateComponent;
    let fixture: ComponentFixture<ActypeUpdateComponent>;
    let service: ActypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [ActypeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ActypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ActypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ActypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Actype(123);
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
        const entity = new Actype();
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
