import type { DoctorAccessLayer } from "../DoctorAccessLayer";
import type { Doctor } from "../entity/Doctor";
import type { DoctorVM } from "../viewmodel/DoctorVM";

export class DoctorNotFoundError extends Error {
  constructor(message: string) {
    super(message)
  }
}

export class DoctorDAO_API implements DoctorAccessLayer {
    private API_ENDPOINT = 'http://localhost:8080/api/doctors'

    public async load(doctorVM: DoctorVM): Promise<Doctor[]> {
        const res = await fetch(this.API_ENDPOINT, { method: 'GET' })
        
        if (!res.ok) {
            const txt = await res.text()
            throw new DoctorNotFoundError(txt);
        }

        const json = await res.json()

        return json;
    }
}