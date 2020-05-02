import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { ActDetailComponent } from 'app/entities/act/act-detail.component';
import { Act } from 'app/shared/model/act.model';

describe('Component Tests', () => {
  describe('Act Management Detail Component', () => {
    let comp: ActDetailComponent;
    let fixture: ComponentFixture<ActDetailComponent>;
    const route = ({ data: of({ act: new Act(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [ActDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ActDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ActDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.act).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
