import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { SocialOrganizationUpdateComponent } from 'app/entities/social-organization/social-organization-update.component';
import { SocialOrganizationService } from 'app/entities/social-organization/social-organization.service';
import { SocialOrganization } from 'app/shared/model/social-organization.model';

describe('Component Tests', () => {
  describe('SocialOrganization Management Update Component', () => {
    let comp: SocialOrganizationUpdateComponent;
    let fixture: ComponentFixture<SocialOrganizationUpdateComponent>;
    let service: SocialOrganizationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [SocialOrganizationUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(SocialOrganizationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SocialOrganizationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SocialOrganizationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SocialOrganization(123);
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
        const entity = new SocialOrganization();
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
