import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { GuardDetailComponent } from 'app/entities/guard/guard-detail.component';
import { Guard } from 'app/shared/model/guard.model';

describe('Component Tests', () => {
  describe('Guard Management Detail Component', () => {
    let comp: GuardDetailComponent;
    let fixture: ComponentFixture<GuardDetailComponent>;
    const route = ({ data: of({ guard: new Guard(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [GuardDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(GuardDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GuardDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.guard).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
