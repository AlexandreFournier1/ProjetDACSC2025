import type { PatientVM } from "@/model/viewmodel/PatientVM"
import type { Patient } from "./entity/Patient"

export interface PatientAccessLayer {
    login(patientVM: PatientVM): Promise<number>
}