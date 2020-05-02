import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { SocialOrganizationRegimenUpdateComponent } from 'app/entities/social-organization-regimen/social-organization-regimen-update.component';
import { SocialOrganizationRegimenService } from 'app/entities/social-organization-regimen/social-organization-regimen.service';
import { SocialOrganizationRegimen } from 'app/shared/model/social-organization-regimen.model';

describe('Component Tests', () => {
  describe('SocialOrganizationRegimen Management Update Component', () => {
    let comp: SocialOrganizationRegimenUpdateComponent;
    let fixture: ComponentFixture<SocialOrganizationRegimenUpdateComponent>;
    let service: SocialOrganizationRegimenService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [SocialOrganizationRegimenUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(SocialOrganizationRegimenUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SocialOrganizationRegimenUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SocialOrganizationRegimenService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SocialOrganizationRegimen(123);
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
        const entity = new SocialOrganizationRegimen();
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
