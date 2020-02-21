import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { Horaire_gardeUpdateComponent } from 'app/entities/horaire-garde/horaire-garde-update.component';
import { Horaire_gardeService } from 'app/entities/horaire-garde/horaire-garde.service';
import { Horaire_garde } from 'app/shared/model/horaire-garde.model';

describe('Component Tests', () => {
  describe('Horaire_garde Management Update Component', () => {
    let comp: Horaire_gardeUpdateComponent;
    let fixture: ComponentFixture<Horaire_gardeUpdateComponent>;
    let service: Horaire_gardeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [Horaire_gardeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(Horaire_gardeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(Horaire_gardeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(Horaire_gardeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Horaire_garde(123);
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
        const entity = new Horaire_garde();
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
