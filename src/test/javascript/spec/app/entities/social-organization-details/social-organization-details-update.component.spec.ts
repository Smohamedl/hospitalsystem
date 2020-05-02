import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { SocialOrganizationDetailsUpdateComponent } from 'app/entities/social-organization-details/social-organization-details-update.component';
import { SocialOrganizationDetailsService } from 'app/entities/social-organization-details/social-organization-details.service';
import { SocialOrganizationDetails } from 'app/shared/model/social-organization-details.model';

describe('Component Tests', () => {
  describe('SocialOrganizationDetails Management Update Component', () => {
    let comp: SocialOrganizationDetailsUpdateComponent;
    let fixture: ComponentFixture<SocialOrganizationDetailsUpdateComponent>;
    let service: SocialOrganizationDetailsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [SocialOrganizationDetailsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(SocialOrganizationDetailsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SocialOrganizationDetailsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SocialOrganizationDetailsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SocialOrganizationDetails(123);
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
        const entity = new SocialOrganizationDetails();
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
