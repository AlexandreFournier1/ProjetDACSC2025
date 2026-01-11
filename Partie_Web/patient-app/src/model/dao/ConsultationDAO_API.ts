import type { ConsultationAccessLayer } from '@/model/ConsultationAccessLayer'
import type { Consultation } from '@/model/entity/Consultation'
import type { PatientVM } from '@/model/viewmodel/PatientVM'
import type { ConsultationVM } from '@/model/viewmodel/ConsultationVM'
import type { Specialty } from '../entity/Specialty'
import type { DoctorVM } from '../viewmodel/DoctorVM'
import type { SpecialtyVM } from '../viewmodel/SpecialtyVM'

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

    public async search(consultationVM: ConsultationVM): Promise<Consultation[]> {
        const params = new URLSearchParams()

        if (consultationVM.date) {
            params.append('date', consultationVM.date)
        }

        if (consultationVM.doctor_name) {
            params.append('doctor', consultationVM.doctor_name)
        }

        if (consultationVM.specialty) {
            params.append('specialty', consultationVM.specialty)
        }

        if ([...params.keys()].length === 0) {
            throw new ConsultationNotFoundError('Aucun critère de recherche fourni')
        }

        const url = `${this.API_ENDPOINT}?${params.toString()}`
        const res = await fetch(url, { method: 'GET' })

        if (!res.ok) {
            throw new ConsultationNotFoundError(await res.text())
        }

        return await res.json()
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
