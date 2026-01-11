import type { Consultation } from '@/model/entity/Consultation'
import type {PatientVM} from "@/model/viewmodel/PatientVM.ts";
import type {ConsultationVM} from "@/model/viewmodel/ConsultationVM.ts";
import type { DoctorVM } from './viewmodel/DoctorVM';
import type { SpecialtyVM } from './viewmodel/SpecialtyVM';

export interface ConsultationAccessLayer {
    getConsultationsByPatientId(patientVM: PatientVM): Promise<Consultation[]>
    search(consultationVM: ConsultationVM): Promise<Consultation[]>
    deleteConsultation(consultationVM: ConsultationVM): Promise<void>
}
