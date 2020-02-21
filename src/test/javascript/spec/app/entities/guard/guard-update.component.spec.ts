import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { GuardUpdateComponent } from 'app/entities/guard/guard-update.component';
import { GuardService } from 'app/entities/guard/guard.service';
import { Guard } from 'app/shared/model/guard.model';

describe('Component Tests', () => {
  describe('Guard Management Update Component', () => {
    let comp: GuardUpdateComponent;
    let fixture: ComponentFixture<GuardUpdateComponent>;
    let service: GuardService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [GuardUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(GuardUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GuardUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GuardService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Guard(123);
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
        const entity = new Guard();
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
