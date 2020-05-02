import { ISocialOrganizationRegimen } from 'app/shared/model/social-organization-regimen.model';

export interface ISocialOrganization {
  id?: number;
  name?: string;
  socialOrganizationRegimen?: ISocialOrganizationRegimen[];
}

export class SocialOrganization implements ISocialOrganization {
  constructor(public id?: number, public name?: string, public socialOrganizationRegimen?: ISocialOrganizationRegimen[]) {}
}
