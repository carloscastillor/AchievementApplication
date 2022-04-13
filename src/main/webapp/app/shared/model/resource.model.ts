export interface IResource {
  id?: number;
  title?: string | null;
  description?: string | null;
  urlSource?: string | null;
  resourceType?: string | null;
}

export const defaultValue: Readonly<IResource> = {};
