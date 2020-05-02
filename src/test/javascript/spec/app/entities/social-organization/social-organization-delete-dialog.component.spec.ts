import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { HospitalsystemTestModule } from '../../../test.module';
import { SocialOrganizationDeleteDialogComponent } from 'app/entities/social-organization/social-organization-delete-dialog.component';
import { SocialOrganizationService } from 'app/entities/social-organization/social-organization.service';

describe('Component Tests', () => {
  describe('SocialOrganization Management Delete Component', () => {
    let comp: SocialOrganizationDeleteDialogComponent;
    let fixture: ComponentFixture<SocialOrganizationDeleteDialogComponent>;
    let service: SocialOrganizationService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [SocialOrganizationDeleteDialogComponent]
      })
        .overrideTemplate(SocialOrganizationDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SocialOrganizationDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SocialOrganizationService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
