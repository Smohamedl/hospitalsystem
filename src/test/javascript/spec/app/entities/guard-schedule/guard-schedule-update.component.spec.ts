import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { GuardScheduleUpdateComponent } from 'app/entities/guard-schedule/guard-schedule-update.component';
import { GuardScheduleService } from 'app/entities/guard-schedule/guard-schedule.service';
import { GuardSchedule } from 'app/shared/model/guard-schedule.model';

describe('Component Tests', () => {
  describe('GuardSchedule Management Update Component', () => {
    let comp: GuardScheduleUpdateComponent;
    let fixture: ComponentFixture<GuardScheduleUpdateComponent>;
    let service: GuardScheduleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [GuardScheduleUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(GuardScheduleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GuardScheduleUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GuardScheduleService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new GuardSchedule(123);
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
        const entity = new GuardSchedule();
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
