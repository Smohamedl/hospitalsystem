import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { HospitalsystemTestModule } from '../../../test.module';
import { Horaire_gardeComponent } from 'app/entities/horaire-garde/horaire-garde.component';
import { Horaire_gardeService } from 'app/entities/horaire-garde/horaire-garde.service';
import { Horaire_garde } from 'app/shared/model/horaire-garde.model';

describe('Component Tests', () => {
  describe('Horaire_garde Management Component', () => {
    let comp: Horaire_gardeComponent;
    let fixture: ComponentFixture<Horaire_gardeComponent>;
    let service: Horaire_gardeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [Horaire_gardeComponent],
        providers: []
      })
        .overrideTemplate(Horaire_gardeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(Horaire_gardeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(Horaire_gardeService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Horaire_garde(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.horaire_gardes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
