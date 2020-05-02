import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { HospitalizationDetailComponent } from 'app/entities/hospitalization/hospitalization-detail.component';
import { Hospitalization } from 'app/shared/model/hospitalization.model';

describe('Component Tests', () => {
  describe('Hospitalization Management Detail Component', () => {
    let comp: HospitalizationDetailComponent;
    let fixture: ComponentFixture<HospitalizationDetailComponent>;
    const route = ({ data: of({ hospitalization: new Hospitalization(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [HospitalizationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(HospitalizationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(HospitalizationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.hospitalization).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
