export interface ISocialOrganization {
  id?: number;
  name?: string;
}

export class SocialOrganization implements ISocialOrganization {
  constructor(public id?: number, public name?: string) {}
}
