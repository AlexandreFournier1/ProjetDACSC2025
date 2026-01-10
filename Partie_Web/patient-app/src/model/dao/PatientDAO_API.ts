import type { PatientAccessLayer } from "../PatientAccessLayer"
import type { PatientVM } from "../viewmodel/PatientVM"

export class PatientNotFoundError extends Error {
  constructor(message: string) {
    super(message)
  }
}

export class PatientDAO_API implements PatientAccessLayer {
  private API_ENDPOINT = 'http://localhost:8080/api/patients'

  public async login(patientVM: PatientVM): Promise<number> {

    const params = new URLSearchParams()

    if (!patientVM.last_name || !patientVM.first_name || patientVM.newPatient === undefined) {
      throw new PatientNotFoundError('Paramètres manquants')
    }

    params.append('last_name', patientVM.last_name)
    params.append('first_name', patientVM.first_name)
    params.append('newPatient', patientVM.newPatient.toString())

    if (patientVM.birth_date) {
      params.append('birth_date', patientVM.birth_date)
    }

    if (!patientVM.newPatient) {
      if (!patientVM.id) {
        throw new PatientNotFoundError('ID Patient requis')
      }
      params.append('idPatient', patientVM.id)
    }

    const res = await fetch(this.API_ENDPOINT, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: params.toString()
    })

    if (!res.ok) {
      throw new PatientNotFoundError(await res.text())
    }

    const json = await res.json()
    return json.id
  }
}