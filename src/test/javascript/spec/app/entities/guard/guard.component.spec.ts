import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { HospitalsystemTestModule } from '../../../test.module';
import { GuardComponent } from 'app/entities/guard/guard.component';
import { GuardService } from 'app/entities/guard/guard.service';
import { Guard } from 'app/shared/model/guard.model';

describe('Component Tests', () => {
  describe('Guard Management Component', () => {
    let comp: GuardComponent;
    let fixture: ComponentFixture<GuardComponent>;
    let service: GuardService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [GuardComponent],
        providers: []
      })
        .overrideTemplate(GuardComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GuardComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GuardService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Guard(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.guards[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
