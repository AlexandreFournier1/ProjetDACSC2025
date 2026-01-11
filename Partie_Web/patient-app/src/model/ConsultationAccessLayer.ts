import type { Consultation } from '@/model/entity/Consultation'
import type {PatientVM} from "@/model/viewmodel/PatientVM.ts";
import type {ConsultationVM} from "@/model/viewmodel/ConsultationVM.ts";

export interface ConsultationAccessLayer {
    getConsultationsByPatientId(patientVM: PatientVM): Promise<Consultation[]>
    search(consultationVM: ConsultationVM): Promise<Consultation[]>
    reserveConsultation(consultationVM: ConsultationVM, patientVM: PatientVM): Promise<void>
    deleteConsultation(consultationVM: ConsultationVM): Promise<void>
}
