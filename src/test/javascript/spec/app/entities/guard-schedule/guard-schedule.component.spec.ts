import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { HospitalsystemTestModule } from '../../../test.module';
import { GuardScheduleComponent } from 'app/entities/guard-schedule/guard-schedule.component';
import { GuardScheduleService } from 'app/entities/guard-schedule/guard-schedule.service';
import { GuardSchedule } from 'app/shared/model/guard-schedule.model';

describe('Component Tests', () => {
  describe('GuardSchedule Management Component', () => {
    let comp: GuardScheduleComponent;
    let fixture: ComponentFixture<GuardScheduleComponent>;
    let service: GuardScheduleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [GuardScheduleComponent],
        providers: []
      })
        .overrideTemplate(GuardScheduleComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GuardScheduleComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GuardScheduleService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new GuardSchedule(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.guardSchedules[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
