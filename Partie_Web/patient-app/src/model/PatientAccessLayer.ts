import type { PatientVM } from "@/viewmodel/PatientVM"
import type { Patient } from "./entity/Patient"

export interface PatientAccessLayer {
    load(patientVM?: PatientVM): Promise<Array<Patient>> 
    getList(): Array<Patient> 
    save(patient: Patient): Promise<void> 
    delete(item: string | Patient): Promise<void>
}