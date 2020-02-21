import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { GuardScheduleDetailComponent } from 'app/entities/guard-schedule/guard-schedule-detail.component';
import { GuardSchedule } from 'app/shared/model/guard-schedule.model';

describe('Component Tests', () => {
  describe('GuardSchedule Management Detail Component', () => {
    let comp: GuardScheduleDetailComponent;
    let fixture: ComponentFixture<GuardScheduleDetailComponent>;
    const route = ({ data: of({ guardSchedule: new GuardSchedule(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [GuardScheduleDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(GuardScheduleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GuardScheduleDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.guardSchedule).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
