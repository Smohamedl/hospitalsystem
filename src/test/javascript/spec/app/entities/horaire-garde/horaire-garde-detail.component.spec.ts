import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { Horaire_gardeDetailComponent } from 'app/entities/horaire-garde/horaire-garde-detail.component';
import { Horaire_garde } from 'app/shared/model/horaire-garde.model';

describe('Component Tests', () => {
  describe('Horaire_garde Management Detail Component', () => {
    let comp: Horaire_gardeDetailComponent;
    let fixture: ComponentFixture<Horaire_gardeDetailComponent>;
    const route = ({ data: of({ horaire_garde: new Horaire_garde(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [Horaire_gardeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(Horaire_gardeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(Horaire_gardeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.horaire_garde).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
