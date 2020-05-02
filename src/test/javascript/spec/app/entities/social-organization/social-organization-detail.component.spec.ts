import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { SocialOrganizationDetailComponent } from 'app/entities/social-organization/social-organization-detail.component';
import { SocialOrganization } from 'app/shared/model/social-organization.model';

describe('Component Tests', () => {
  describe('SocialOrganization Management Detail Component', () => {
    let comp: SocialOrganizationDetailComponent;
    let fixture: ComponentFixture<SocialOrganizationDetailComponent>;
    const route = ({ data: of({ socialOrganization: new SocialOrganization(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [SocialOrganizationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SocialOrganizationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SocialOrganizationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.socialOrganization).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
