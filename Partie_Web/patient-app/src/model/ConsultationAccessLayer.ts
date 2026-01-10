import type { Consultation } from '@/model/entity/Consultation'
import type {PatientVM} from "@/model/viewmodel/PatientVM.ts";
import type {ConsultationVM} from "@/model/viewmodel/ConsultationVM.ts";

export interface ConsultationAccessLayer {
    getConsultationsByPatientId(patientVM: PatientVM): Promise<Consultation[]>
    deleteConsultation(consultationVM: ConsultationVM): Promise<void>
}
