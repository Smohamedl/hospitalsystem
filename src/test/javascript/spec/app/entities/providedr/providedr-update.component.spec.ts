import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { ProviderUpdateComponent } from 'app/entities/provider/provider-update.component';
import { ProviderService } from 'app/entities/provider/provider.service';
import { Providedr } from 'app/shared/model/provider.model';

describe('Component Tests', () => {
  describe('Provider Management Update Component', () => {
    let comp: ProviderUpdateComponent;
    let fixture: ComponentFixture<ProviderUpdateComponent>;
    let service: ProviderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [ProviderUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ProviderUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProviderUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProviderService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Providedr(123);
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
        const entity = new Providedr();
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
