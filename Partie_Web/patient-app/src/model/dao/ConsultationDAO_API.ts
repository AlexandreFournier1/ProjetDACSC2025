import type { ConsultationAccessLayer } from '@/model/ConsultationAccessLayer'
import type { Consultation } from '@/model/entity/Consultation'
import type { PatientVM } from '@/model/viewmodel/PatientVM'
import type { ConsultationVM } from '@/model/viewmodel/ConsultationVM'

export class ConsultationNotFoundError extends Error {
    constructor(message: string) {
        super(message)
    }
}

export class ConsultationDAO_API implements ConsultationAccessLayer {
    private API_ENDPOINT = 'http://localhost:8080/api/consultations'

    public async getConsultationsByPatientId(patientVM: PatientVM): Promise<Consultation[]> {

        if (!patientVM.id) {
            throw new ConsultationNotFoundError('ID patient manquant')
        }

        const params = new URLSearchParams()
        params.append('patientId', patientVM.id)

        const url = `${this.API_ENDPOINT}?${params.toString()}`

        const res = await fetch(url, { method: 'GET' })

        if (!res.ok) {
            const txt = await res.text()
            throw new ConsultationNotFoundError(txt)
        }

        const json = await res.json()

        return json
    }



    public async deleteConsultation(consultationVM: ConsultationVM): Promise<void> {

        if (!consultationVM.id) {
            throw new ConsultationNotFoundError('ID consultation manquant')
        }

        const params = new URLSearchParams()
        params.append('id', consultationVM.id)

        const res = await fetch(
            `${this.API_ENDPOINT}?${params.toString()}`,
            { method: 'DELETE' }
        )

        if (!res.ok) {
            throw new ConsultationNotFoundError(await res.text())
        }
    }

}
