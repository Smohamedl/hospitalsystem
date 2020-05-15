import { ISocialOrganizationRegimen } from 'app/shared/model/social-organization-regimen.model';

export interface ISocialOrganizationDetails {
  id?: number;
  registrationNumber?: number;
  matriculeNumber?: string;
  socialOrganizationRegimen?: ISocialOrganizationRegimen;
}

export class SocialOrganizationDetails implements ISocialOrganizationDetails {
  constructor(
    public id?: number,
    public registrationNumber?: number,
    public matriculeNumber?: string,
    public socialOrganizationRegimen?: ISocialOrganizationRegimen
  ) {}
}
