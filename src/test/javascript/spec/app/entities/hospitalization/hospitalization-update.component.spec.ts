import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { HospitalizationUpdateComponent } from 'app/entities/hospitalization/hospitalization-update.component';
import { HospitalizationService } from 'app/entities/hospitalization/hospitalization.service';
import { Hospitalization } from 'app/shared/model/hospitalization.model';

describe('Component Tests', () => {
  describe('Hospitalization Management Update Component', () => {
    let comp: HospitalizationUpdateComponent;
    let fixture: ComponentFixture<HospitalizationUpdateComponent>;
    let service: HospitalizationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [HospitalizationUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(HospitalizationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HospitalizationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HospitalizationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Hospitalization(123);
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
        const entity = new Hospitalization();
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
