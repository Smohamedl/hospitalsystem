import { ISocialOrganization } from 'app/shared/model/social-organization.model';

export interface ISocialOrganizationRegimen {
  id?: number;
  percentage?: number;
  socialOrganization?: ISocialOrganization;
}

export class SocialOrganizationRegimen implements ISocialOrganizationRegimen {
  constructor(public id?: number, public percentage?: number, public socialOrganization?: ISocialOrganization) {}
}
