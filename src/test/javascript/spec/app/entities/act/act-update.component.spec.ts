import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { ActUpdateComponent } from 'app/entities/act/act-update.component';
import { ActService } from 'app/entities/act/act.service';
import { Act } from 'app/shared/model/act.model';

describe('Component Tests', () => {
  describe('Act Management Update Component', () => {
    let comp: ActUpdateComponent;
    let fixture: ComponentFixture<ActUpdateComponent>;
    let service: ActService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [ActUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ActUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ActUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ActService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Act(123);
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
        const entity = new Act();
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
