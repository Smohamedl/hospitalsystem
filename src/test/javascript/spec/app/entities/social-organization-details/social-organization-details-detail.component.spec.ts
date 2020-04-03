import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { SocialOrganizationDetailsDetailComponent } from 'app/entities/social-organization-details/social-organization-details-detail.component';
import { SocialOrganizationDetails } from 'app/shared/model/social-organization-details.model';

describe('Component Tests', () => {
  describe('SocialOrganizationDetails Management Detail Component', () => {
    let comp: SocialOrganizationDetailsDetailComponent;
    let fixture: ComponentFixture<SocialOrganizationDetailsDetailComponent>;
    const route = ({ data: of({ socialOrganizationDetails: new SocialOrganizationDetails(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [SocialOrganizationDetailsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SocialOrganizationDetailsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SocialOrganizationDetailsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.socialOrganizationDetails).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
