import type { Specialty } from "../entity/Specialty";
import type { SpecialtyAccessLayer } from "../SpecialtyAccessLayer";
import type { SpecialtyVM } from "../viewmodel/SpecialtyVM";

export class SpecialtyNotFoundError extends Error {
  constructor(message: string) {
    super(message)
  }
}

export class SpecialtyDAO_API implements SpecialtyAccessLayer {
    private API_ENDPOINT = 'http://localhost:8080/api/specialties'

    public async load(specialtyVM: SpecialtyVM): Promise<Specialty[]> {
        const res = await fetch(this.API_ENDPOINT, { method: 'GET' })
                
        if (!res.ok) {
            const txt = await res.text()
            throw new SpecialtyNotFoundError(txt);
        }

        const json = await res.json()

        return json;
    }
}