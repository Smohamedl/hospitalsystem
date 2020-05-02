import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { ActypeDetailComponent } from 'app/entities/actype/actype-detail.component';
import { Actype } from 'app/shared/model/actype.model';

describe('Component Tests', () => {
  describe('Actype Management Detail Component', () => {
    let comp: ActypeDetailComponent;
    let fixture: ComponentFixture<ActypeDetailComponent>;
    const route = ({ data: of({ actype: new Actype(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [ActypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ActypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ActypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.actype).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
